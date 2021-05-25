package com.example.chattingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText emailEt, passwordEt;
    private AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        Button signInButton = findViewById(R.id.login);
        builder = new AlertDialog.Builder(this);
        TextView signUpTv = findViewById(R.id.signUpTv);

        signInButton.setOnClickListener(v -> Login());

        signUpTv.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
        private void Login(){

            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            if(TextUtils.isEmpty(email)){
                emailEt.setError("Enter your email");
                return;
            }
            else if(TextUtils.isEmpty(password)){
                passwordEt.setError("Enter your password");
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {

                if(task.isSuccessful()){
                    builder.setMessage("Please wait...");
                    builder.show();
                    Toast.makeText(MainActivity.this,"Login successfully!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    Toast.makeText(MainActivity.this,"Failed to sign in, password may be incorrect!",Toast.LENGTH_LONG).show();
                }
            });
        }
}