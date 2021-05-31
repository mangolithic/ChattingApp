package com.example.chattingapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattingapp.Adapter.UserAdapter;
import com.example.chattingapp.Model.Chatlist;
import com.example.chattingapp.Model.Users;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private UserAdapter userAdapter;
    private List<Users> MyUser;

    FirebaseUser fuser;
    DatabaseReference ref;

    private List<Chatlist> cl;

    RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_chat,container,false);

        recyclerView = view.findViewById(R.id.recycle_view1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        cl=new ArrayList<>();

        ref= FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cl.clear();

                //loop for all users
                for (DataSnapshot ss: snapshot.getChildren()){

                    Chatlist clist= ss.getValue(Chatlist.class);
                    cl.add(clist);

                }
                chatList();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void chatList() {

        //getting all users
        MyUser = new ArrayList<>();
        ref=FirebaseDatabase.getInstance().getReference("MyUsers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                MyUser.clear();

                for (DataSnapshot ss: snapshot.getChildren()){
                    Users user = ss.getValue(Users.class);
                    for(Chatlist chatlist:cl){

                        if (user.getId().equals(chatlist.getId())) {
                            MyUser.add(user);
                        }
                    }
                }

                userAdapter= new UserAdapter(getContext(),MyUser,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}