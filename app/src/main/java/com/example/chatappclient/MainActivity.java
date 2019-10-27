package com.example.chatappclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatappclient.Common.Common;
import com.example.chatappclient.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    EditText edtname,edtpassword;
    Button btnlogin,btnsingin;
    FirebaseDatabase database =FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /////////////////Anh xa///////////////
        edtname = (MaterialEditText)findViewById(R.id.edtname);
        edtpassword = (MaterialEditText)findViewById(R.id.edtpassword);
        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnsingin = (Button)findViewById(R.id.btnsignin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Name = edtname.getText().toString();
                        //Kiểm tra người dùng có tồn tại trong database
                        if (dataSnapshot.child(Name).exists()) {
                            // Lấy thông tin người dùng
                            User user = dataSnapshot.child(Name).getValue(User.class);
                            user.setName(Name); // set Name cho người dùng
                            if(user.getPassword()== null ||edtname.getText().toString().isEmpty()){
                                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            }
                            else if (user.getPassword().equals(edtpassword.getText().toString())) {
                                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Common.currentUser = user;
                                Intent homeIntent = new Intent(MainActivity.this,Home.class);
                                startActivity(homeIntent);
                                finish();//Chuyển qua activity mới và hủy activity hiện tại

                            } else {
                                Toast.makeText(MainActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this,"Người dùng không tồn tại",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        btnsingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(edtname.getText().toString().isEmpty()||edtpassword.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }
                        //Kiểm tra name đã có sẵn
                        else if(dataSnapshot.child(edtname.getText().toString()).exists()){
                            Toast.makeText(MainActivity.this, "Tên đăng nhập đã đăng ký", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                            User user = new User(edtname.getText().toString(),edtpassword.getText().toString());
                            table_user.child(edtname.getText().toString()).setValue(user);
                            Intent homeIntent = new Intent(MainActivity.this,Home.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
