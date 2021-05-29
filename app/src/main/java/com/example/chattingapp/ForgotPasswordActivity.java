package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailFP;
    private Button sendBtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        emailFP = (EditText) findViewById(R.id.emailrpET);
        sendBtn = (Button) findViewById(R.id.sendBtn);

        auth = FirebaseAuth.getInstance();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resetpass();
            }
        });
    }

    private void Resetpass() {
        String email = emailFP.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailFP.setError("Enter your email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailFP.setError("Invalid email");
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email inbox!", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send. Please enter a registered email!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
