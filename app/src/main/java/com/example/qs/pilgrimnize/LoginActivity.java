package com.example.qs.pilgrimnize;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText emailLogin, passwordLogin;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.singupTF).setOnClickListener(this);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.forgotPassword).setOnClickListener(this);


        ArrayList<Double> location = (ArrayList<Double>) getIntent().getSerializableExtra("LOCATION");
        System.out.println("locationSignUP " + location);

        emailLogin = findViewById(R.id.emailTFLogin);
        passwordLogin = findViewById(R.id.passwordTFLogin);
        progressBar = findViewById(R.id.progressBarLogin);
    }
    public void login(){
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        if (email.isEmpty()) {
            emailLogin.setError("Email is required");
            emailLogin.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLogin.setError("Please enter a valid Email");
            emailLogin.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordLogin.setError("Email is required");
            passwordLogin.requestFocus();
            return;
        }
        if (password.length() < 6 ){
            passwordLogin.setError("Minmum length of password should be 6");
            passwordLogin.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            // handle error
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            System.out.println("Successful login");
                            finish();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
        System.out.println("login");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.singupTF:
                startActivity(new Intent(this,SignUpActivity.class));
                finish();
                break;
            case R.id.loginButton:
                login();
                break;
            case R.id.forgotPassword:
                handleForgotPassword();
        }
    }
    public void handleForgotPassword(){
        startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
        finish();
    }
}
