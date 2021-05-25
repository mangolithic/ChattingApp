package com.example.chattingapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEt,passwordEt1,passwordEt2;
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth=FirebaseAuth.getInstance();
        emailEt=findViewById(R.id.email);
        passwordEt1=findViewById(R.id.password1);
        passwordEt2=findViewById(R.id.password2);
        Button signUpButton = findViewById(R.id.register);
        builder = new AlertDialog.Builder(this);
        TextView signInTv = findViewById(R.id.signInTv);

        signUpButton.setOnClickListener(v -> Register());

        signInTv.setOnClickListener(v -> {
            Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void Register(){
        String email=emailEt.getText().toString();
        String password1=passwordEt1.getText().toString();
        String password2=passwordEt2.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEt.setError("Enter your email");
            return;
        }
        else if(TextUtils.isEmpty(password1)){
            passwordEt1.setError("Enter your password");
            return;
        }
        else if(TextUtils.isEmpty(password2)){
            passwordEt2.setError("Confirm your password");
            return;
        }
        else if(!password1.equals(password2)){
            passwordEt2.setError("Password does not match");
            return;
        }
        else if(password1.length()<4){
            passwordEt1.setError("Length should be > 4");
            return;
        }
        else if(!isValidEmail(email)){
            emailEt.setError("Invalid email");
            return;
        }

        builder.setMessage("Please wait...");
        builder.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(SignUpActivity.this,"Successfully registered!",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SignUpActivity.this,SecondActivity.class);
                startActivity(intent);
                builder.setCancelable(false);
                finish();
            }
            else{
                Toast.makeText(SignUpActivity.this,"Failed to sign up!",Toast.LENGTH_LONG).show();
                builder.setCancelable(false);
            }
        });
    }

    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
