package com.example.qs.pilgrimnize;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button sendLink;
    EditText backToLogin;
    EditText emailForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailForgotPassword = findViewById(R.id.emailForgetPassword);
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to login
                backToLoginActivity();
            }
        });
        findViewById(R.id.sendLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check email Valdion
                String email = emailForgotPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    emailForgotPassword.setError("Email is required");
                    emailForgotPassword.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailForgotPassword.setError("Please enter a valid Email");
                    emailForgotPassword.requestFocus();
                    return;
                }
                // Tack Email
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"We send the Link to you by email",Toast.LENGTH_SHORT).show();
                                    backToLoginActivity();
                                }else {

                                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });
    }
    public void backToLoginActivity(){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}
