package com.example.chattingapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chattingapp.Adapter.UserAdapter;
import com.example.chattingapp.Model.Users;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {
        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<Users> myUsers;

        EditText userSearch;

    public UsersFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_users,container,false);

     recyclerView = view.findViewById(R.id.recycle_view);
     recyclerView.setHasFixedSize(true);
     recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

     myUsers = new ArrayList<>();

     ReadUsers();

     userSearch = view.findViewById(R.id.searchUser);

     userSearch.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchUName(s.toString().toLowerCase());
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });

     return view;
    }

    private void searchUName(String s) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance()
                .getReference("MyUsers").orderByChild("search")
                .startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                myUsers.clear();
                for (DataSnapshot ss : snapshot.getChildren()) {
                    Users user = ss.getValue(Users.class);

                    assert user != null;
                    assert fUser != null;
                    if (!user.getId().equals(fUser.getUid())) {
                        myUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter(getContext(), myUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


    private void ReadUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                if(userSearch.getText().toString().equalsIgnoreCase("")){

                myUsers.clear();

                for (DataSnapshot ss : Snapshot.getChildren()) {
                    Users user = ss.getValue(Users.class);

                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        myUsers.add(user);
                    }
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