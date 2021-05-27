package com.example.chattingapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView edit_userPhoto;
    EditText edit_name, edit_about;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    DatabaseReference reference;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        edit_userPhoto = view.findViewById(R.id.profile_image);
        edit_name = view.findViewById(R.id.uName);
        edit_about = view.findViewById(R.id.uDesc);

        return view; //placeholder
    }

}