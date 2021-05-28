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
import com.example.chattingapp.Model.Users;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {
        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<Users> myUsers;


    public UsersFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_users,container,false);

     recyclerView= view.findViewById(R.id.recycle_view);
     recyclerView.setHasFixedSize(true);
     recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

     myUsers = new ArrayList<>();

     ReadUsers();
     return view;
    }

    private void ReadUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                myUsers.clear();

                for (DataSnapshot ss : Snapshot.getChildren()){
                    Users user = ss.getValue(Users.class);

                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())){
                        myUsers.add(user);
                    }

                    userAdapter = new UserAdapter(getContext(),myUsers);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}