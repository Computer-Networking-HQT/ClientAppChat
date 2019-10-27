package com.example.chatappclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.chatappclient.Adapter.MessageAdapter;
import com.example.chatappclient.Common.Common;
import com.example.chatappclient.Model.Chat;
import com.example.chatappclient.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView user_image;
    TextView username;
    DatabaseReference reference;
    Intent intent;

    ImageButton btnSend;
    EditText textSend;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        user_image = findViewById(R.id.user_image);
        username = findViewById(R.id.username);
        btnSend = findViewById(R.id.btnSend);
        textSend = findViewById(R.id.textSend);

        recyclerView = findViewById(R.id.recycleview_mess);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mchat = new ArrayList<>();

        intent = getIntent();
        final String userid = intent.getStringExtra("username");
        Log.i("MyTagMess",userid);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = textSend.getText().toString();
                if (!msg.equals("")){
                    sendMessage(Common.currentUser.getName(),userid,msg);
                }else{
                    Toast.makeText(MessageActivity.this,"Không thể gửi tin nhắn này",Toast.LENGTH_SHORT).show();
                }
                textSend.setText("");
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getName());
                user_image.setImageResource(R.drawable.ic_account_circle_black_24dp);
                readMessage(userid,Common.currentUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MessageActivity","Error Firebase");
            }
        });
    }
    private void sendMessage(String sender, String receiver,String chat){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("chat",chat);
        reference.child("Chats").push().setValue(hashMap);
    }
    private void readMessage(final String userid,final String myid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Chat chat = child.getValue(Chat.class);
                    Log.i("TagReadMess",chat.getChat());
                    if((chat.getReceiver().equals(myid) && chat.getSender().equals(userid))||
                            (chat.getReceiver().equals(userid)&& chat.getSender().equals(myid))){
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
