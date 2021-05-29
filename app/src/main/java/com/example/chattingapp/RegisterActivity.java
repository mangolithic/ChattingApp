package com.example.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button Register;
    EditText UserEmail,UserPassword, Username;
    TextView SignIn;
    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initialize
        auth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        SignIn = (TextView) findViewById(R.id.signIn);
        Register = (Button) findViewById(R.id.register);
        UserEmail = (EditText) findViewById(R.id.email);
        Username = (EditText) findViewById(R.id.username);
        UserPassword = (EditText) findViewById(R.id.password1);

        //Login
        SignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        //Sign up
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Username.getText().toString();
                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();
                String about = "I am a new user";

                if(TextUtils.isEmpty(email)){
                    UserEmail.setError("Enter your email");
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    UserPassword.setError("Enter your password");
                    return;
                }
                else if(password.length()<4){
                   UserPassword.setError("Password need to contain more than 4 alphanumerics");
                    return;
                }
                else if(!isVallidEmail(email)){
                    UserEmail.setError("Invalid email");
                    return;
                }
                else {
                    Register(username, email, password, about);
                }
            }
        });
    }
    private boolean isVallidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //Register()
        private void Register(final String username, String email, String password, String about){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance()
                                    .getReference("MyUsers").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username",username);
                            hashMap.put("password",password);
                            hashMap.put("email",email);
                            hashMap.put("imageURL","default");
                            hashMap.put("about", "I am a new user");

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      Toast.makeText(RegisterActivity.this, "Please wait", Toast.LENGTH_SHORT).show();

                                      if (task.isSuccessful()){
                                          Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                          mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                          startActivity(mainIntent);
                                          finish();
                                          Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              });
                        }
                        else{
                            String message = Objects.requireNonNull(task.getException()).toString();
                            Toast.makeText(RegisterActivity.this, "Failed to register! Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
}


