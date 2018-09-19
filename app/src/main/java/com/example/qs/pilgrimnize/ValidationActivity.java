package com.example.qs.pilgrimnize;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ValidationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signupPhoneNumber;
    private TextView backToUserName;
    private EditText activionCode;

    private String phoneNumber,userName;
    private String verificationId;
    private FirebaseAuth mAuth;


    private FirebaseDatabase database;
    private DatabaseReference myRef;

//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        findViewById(R.id.phoneSignup).setOnClickListener(this);
        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        userName = getIntent().getStringExtra("USER_NAME");
        System.out.println("phone number UserName "+phoneNumber +" "+ userName);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        activionCode = findViewById(R.id.activiationCode);
        String number = "+"+phoneNumber;
        progressBar = findViewById(R.id.progressBar);

        sendVerfictionCode(number);
        mAuth = FirebaseAuth.getInstance();
    }

    private void sendVerfictionCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            System.out.println("PhoneAuthProvider onCodeSent "+ s);
            verificationId = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            // HERE IF THE VERIFICTION IS DONE AUTOMATUCLEY
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                System.out.println("PhoneAuthProvider onVerificationCompleted "+phoneAuthCredential);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ValidationActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    public void verifyCode(String code ){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        singnInWithCredential(credential);
    }
    public void singnInWithCredential(PhoneAuthCredential credential){
        System.out.println("singnInWithCredential");
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("singnInWithCredential Task");
                if (task.isComplete()){
                    Intent intent = new Intent(ValidationActivity.this,TheRootActivity.class);
                    System.out.println("singnInWithCredential Complete "+ task.getResult().getUser().getUid().toString());

                    Toast.makeText(getApplicationContext(),"User Registered Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    System.out.println(user.getUid().toString().trim());

                    Map<String, User> map = new HashMap<String, User>();


                    String uid = user.getUid().toString();

                    User user3 = new User(2);
                    user3.officeNumber = 1;
                    user3.uid = uid;
                    user3.username = userName;
//                    user3.location = location;
                    System.out.println("User   "+user3);
                    map.put(uid,user3);
//
                    Map<String, Object> userValues = user3.toMap();
                    System.out.println("userValues   "+ userValues);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(uid,userValues);
                    myRef.updateChildren(childUpdates);

                    getTheRootActivity();
                    finish();
                }else {
                    Toast.makeText(ValidationActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println("singnInWithCredential Complete" + task.getException().getLocalizedMessage());
                }
            }
        });
    }
    public void getTheRootActivity(){
        startActivity(new Intent(getApplicationContext(),TheRootActivity.class));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phoneSignup:
                handleSignUserWithPhoneNumber();
                break;
        }
    }
    public void handleSignUserWithPhoneNumber(){

        String code = activionCode.getText().toString().trim();
        if (code.isEmpty() || code.length() <6 ){
            activionCode.setError("Enter code");
            activionCode.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        verifyCode(code);
    }
}
