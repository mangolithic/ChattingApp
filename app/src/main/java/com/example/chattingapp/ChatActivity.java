package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.chattingapp.Adapter.MessageAdapter;
import com.example.chattingapp.Model.Chat;
import com.example.chattingapp.Model.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
    TextView username;
    ImageView imageView;

    EditText sendText;
    ImageButton sendbtn, imageButton;
    Chat chat;

    FirebaseUser firebaseUser;
    DatabaseReference ref, rootRef;
    StorageReference storageReference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> myChat;

    RecyclerView recycler_View;
    String url = "", userid, imgReceiver, imgSender;
    UploadTask uploadTask;

    Uri uri;
    private static final int PICK_IMAGE = 1;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat = new Chat();
        imageView = findViewById(R.id.profileimage);
        username = findViewById(R.id.user_name);
        sendbtn = findViewById(R.id.sendbtn);
        sendText = findViewById(R.id.sendtext);
        imageButton = findViewById(R.id.imagebtn);

        //RecyclerView
        recycler_View = findViewById(R.id.recyclerview);
        recycler_View.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycler_View.setLayoutManager(linearLayoutManager);

        loadingBar = new ProgressDialog(this);

        intent = getIntent();
        userid = intent.getStringExtra("userid");

        storageReference=FirebaseStorage.getInstance().getReference("Message Images");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {*/
            //url = bundle.getString("URL"); //wth is this supposed to be hhhh
        /*}else
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }*/

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Chat chat = snapshot.getValue(Chat.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default")) {
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

        sendbtn.setOnClickListener((v) ->  {
                String msg = sendText.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), userid, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "Please send a non-empty message", Toast.LENGTH_SHORT).show();
                }
                sendText.setText("");
        });

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

    }

    private void sendMessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("type", "TEXT");

        reference.child("Chats").push().setValue(hashMap);

        //adding user to chat fragment(display the username)
        final DatabaseReference chatRef=FirebaseDatabase.getInstance()
                .getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                data != null || data.getData() != null) {

            loadingBar.setTitle("Uploading image");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            uri = data.getData();

            imgReceiver = userid;
            imgSender = firebaseUser.getUid();
            rootRef = FirebaseDatabase.getInstance().getReference("Chats");

            DatabaseReference userMessageKeyRef = rootRef.child("Chats").child(imgSender).child(imgReceiver).push();
            /*String msgSender = "Chats/" + sender + "/" + receiver;
            String msgReceiver = "Chats/" + receiver + "/" + sender;*/

            String messagePushID = userMessageKeyRef.getKey();

            final StorageReference reference = storageReference.child (System.currentTimeMillis()+ "."+getFileExt(uri));
            uploadTask=reference.putFile(uri);

            //Task<Uri>urlTask =
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>(){
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot>task) throws Exception{
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>(){
                @Override
                public void onComplete(@NonNull Task<Uri> task){

                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        url = downloadUri.toString();

                        HashMap<String, Object> imageMessage = new HashMap<>();
                        imageMessage.put("URL", url);
                        imageMessage.put("sender", imgSender);
                        imageMessage.put("receiver", imgReceiver);
                        imageMessage.put("message", messagePushID);
                        imageMessage.put("type", "IMAGE");

                        HashMap<String, Object> imageDetails = new HashMap<>();
                        imageDetails.put(messagePushID, imageMessage);
                        //imageDetails.put(msgReceiver + "/" + messagePushID, imageMessage);

                        rootRef.updateChildren(imageDetails).addOnCompleteListener((task1) -> {
                            if(task1.isSuccessful()){
                                loadingBar.dismiss();
                                Toast.makeText(ChatActivity.this, "Message sent successfully",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                loadingBar.dismiss();
                                Toast.makeText(ChatActivity.this, "Failed to send",
                                        Toast.LENGTH_SHORT).show();
                            }
                            sendText.setText("");
                        });

                        /*chat.setReceiver(receiver);
                        chat.setSender(sender);
                        chat.setMessage(url);
                        chat.setType("IMAGE");
                        chat.setURL(imageurl.toString());


                        String id1 = database.getReference("Chats").child(sender).push().getKey();
                        database.getReference("Chats").child(sender).child(id1).setValue(chat);

                        String id2 = database.getReference("Chats").child(receiver).push().getKey();
                        database.getReference("Chats").child(receiver).child(id2).setValue(chat);*/

                    }else {
                        loadingBar.dismiss();
                        Toast.makeText(ChatActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    /*private void sendImage()
    {
        if (imageurl!= null){
            rootRef = FirebaseDatabase.getInstance().getReference("Chats");

            DatabaseReference userMessageKeyRef = rootRef.child("Chats").child(sender).child(receiver).push();
            String msgSender = "Chats/" + sender + "/" + receiver;
            String msgReceiver = "Chats/" + receiver + "/" + sender;

            String messagePushID = userMessageKeyRef.getKey();

            final StorageReference reference = storageReference.child (System.currentTimeMillis()+ "."+getFileExt(imageurl));
            uploadTask=reference.putFile(imageurl);

            //Task<Uri>urlTask =
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>(){
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot>task) throws Exception{
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>(){
                @Override
                public void onComplete(@NonNull Task<Uri> task){

                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        url = downloadUri.toString();

                        HashMap<String, Object> imageMessage = new HashMap<>();
                        imageMessage.put("URL", url);
                        imageMessage.put("sender", sender);
                        imageMessage.put("receiver", receiver);
                        imageMessage.put("message", messagePushID);
                        imageMessage.put("type", "IMAGE");

                        HashMap<String, Object> imageDetails = new HashMap<>();
                        imageDetails.put(msgSender + "/" + messagePushID, imageMessage);
                        imageDetails.put(msgReceiver + "/" + messagePushID, imageMessage);

                        rootRef.updateChildren(imageDetails).addOnCompleteListener((task1) -> {
                                if(task1.isSuccessful()){
                                    loadingBar.dismiss();
                                    Toast.makeText(ChatActivity.this, "Message sent successfully",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    loadingBar.dismiss();
                                    Toast.makeText(ChatActivity.this, "Failed to send",
                                            Toast.LENGTH_SHORT).show();
                                }
                                sendText.setText("");
                        });

                        *//*chat.setReceiver(receiver);
                        chat.setSender(sender);
                        chat.setMessage(url);
                        chat.setType("IMAGE");
                        chat.setURL(imageurl.toString());
*//*

                        *//*String id1 = database.getReference("Chats").child(sender).push().getKey();
                        database.getReference("Chats").child(sender).child(id1).setValue(chat);

                        String id2 = database.getReference("Chats").child(receiver).push().getKey();
                        database.getReference("Chats").child(receiver).child(id2).setValue(chat);
*//*
                    }else {
                        loadingBar.dismiss();
                        Toast.makeText(ChatActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else
        {
            Toast.makeText(this, "Please select something", Toast.LENGTH_SHORT).show();
        }

    }*/

}

    /*private void setSupportActionBar(Toolbar toolbar){

    }*/

    