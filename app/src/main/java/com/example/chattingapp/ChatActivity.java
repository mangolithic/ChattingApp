package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.chattingapp.Adapter.MessageAdapter;
import com.example.chattingapp.Model.Chat;
import com.example.chattingapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
    TextView username;
    ImageView imageView;

    RecyclerView recyclerView;
    EditText sendText;
    ImageButton sendbtn;

    FirebaseUser firebaseUser;
    DatabaseReference ref;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> myChat;

    RecyclerView recycler_View;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageView = findViewById(R.id.profileimage);
        username = findViewById(R.id.user_name);

        sendbtn = findViewById(R.id.sendbtn);
        sendText = findViewById(R.id.sendtext);

        //RecyclerView
        recycler_View = findViewById(R.id.recyclerview);
        recycler_View.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycler_View.setLayoutManager(linearLayoutManager);


        intent = getIntent();
        String userid = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("defaults")) {
                    imageView.setImageResource(R.drawable.profile);
                } else {
                    Glide.with(ChatActivity.this).load(user.getImageURL()).into(imageView);
                }

                readMessages(firebaseUser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendText.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), userid, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "Please send a non empty message.", Toast.LENGTH_SHORT).show();
                }

                sendText.setText("");
            }

            private void sendMessage(String sender, String receiver, String message) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("sender", sender);
                hashMap.put("receiver", receiver);
                hashMap.put("Message", message);

                reference.child("Chats").push().setValue(hashMap);
            }
        });


    }

    private void readMessages(String myid, String userid, String imageURL) {
        myChat = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myChat.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {

                    Chat chat = datasnapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        myChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this, myChat, imageURL);
                    recycler_View.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*private void setSupportActionBar(Toolbar toolbar){

    }*/
}
    