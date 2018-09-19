package com.example.qs.pilgrimnize;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private ProgressBar progressBar;
    private EditText emailTF, passwordTF, usernameTF ;
    private TextView singupWithPhone;

    private ArrayList<Double> location;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

//        getWindow().setStatusBarColor(R.color.myBlue);

        findViewById(R.id.singupButton).setOnClickListener(this);
        findViewById(R.id.loginTV).setOnClickListener(this);
        singupWithPhone = (TextView) findViewById(R.id.signupWithPhone);
        singupWithPhone.setOnClickListener(this);
        progressBar =  findViewById(R.id.progressBar);


        emailTF = findViewById(R.id.emailTF);
        passwordTF = findViewById(R.id.passwordTF);
        usernameTF = findViewById(R.id.usernameTF);

        findViewById(R.id.singupButton).setOnClickListener(this);
        findViewById(R.id.loginTV).setOnClickListener(this);
    }
    public void regesterAUser(){

        String email = emailTF.getText().toString().trim();
        String password = passwordTF.getText().toString().trim();
        final String username = usernameTF.getText().toString().trim();

        if (email.isEmpty()) {
            emailTF.setError("Email is required");
            emailTF.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTF.setError("Please enter a valid Email");
            emailTF.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordTF.setError("Email is required");
            passwordTF.requestFocus();
            return;
        }
        if (username.isEmpty()){
            usernameTF.setError("Email is required");
            usernameTF.requestFocus();
            return;
        }
        if (password.length() < 6 ){
            passwordTF.setError("Minmum length of password should be 6");
            passwordTF.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

//        requestLocationUpdate();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            // handle the unsuccessful
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(),"This email is used",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            Toast.makeText(getApplicationContext(),"User Registered Successful",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            System.out.println(user.getUid().toString().trim());

                            Map<String, User> map = new HashMap<String, User>();


                            String uid = user.getUid().toString();

                            User user3 = new User(2);
//                            if ((lat != null) && (log != null)) {
//                                user3.location = new ArrayList<Double>();
//
//                                user3.location.add(lat);
//                                user3.location.add(log);
//                            }else {
//                                user3.location = new ArrayList<Double>();
//                                user3.location.add(0.0);
//                                user3.location.add(0.0);
//                            }
                            user3.officeNumber = 1;
                            user3.uid = uid;
                            user3.username = username;
                            user3.location = location;
                            System.out.println("User   "+user3);
                            map.put(uid,user3);
//
                            Map<String, Object> userValues = user3.toMap();
                            System.out.println("userValues   "+ userValues);
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(uid,userValues);
                            myRef.updateChildren(childUpdates);
//                    myRef.setValue(user1);
//                    fetchUserFormDatabase();

//                    getTheRootActivity(user3);
                            getTheRootActivity();
                            finish();

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
    public void getTheRootActivity(){
        startActivity(new Intent(getApplicationContext(),TheRootActivity.class));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.singupButton:
                // preform sing up
                regesterAUser();
                break;
            case R.id.loginTV:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.signupWithPhone:
                /// hidde the layout and show the otherLayout
                signupWithPhone();
        }
    }
    public void signupWithPhone(){
        startActivity(new Intent(getApplicationContext(),PhoneSignup.class));
        finish();
    }
}
