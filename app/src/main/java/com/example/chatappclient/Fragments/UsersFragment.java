package com.example.chatappclient.Fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatappclient.Adapter.UserAdapter;
import com.example.chatappclient.Model.User;
import com.example.chatappclient.PageViewModel;
import com.example.chatappclient.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    private static final String TAG = "Users";

    private PageViewModel pageViewModel;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment UsersFragment.
     */
    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        readUsers();
        return view;
    }

    private void readUsers() {
        //doc du lieu tu database va add vao list user
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot  child : dataSnapshot.getChildren()){
                    User user = child.getValue(User.class);
                    assert user != null;
                    mUsers.add(user);
                }
//                //set adapter cho recycle view
//                mUsers.add(new User("hieu","1"));
//                mUsers.add(new User("hieu1","2"));
//                mUsers.add(new User("hieu3","3"));
//                mUsers.add(new User("hieu4","4"));
//                mUsers.add(new User("tan","5"));
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

