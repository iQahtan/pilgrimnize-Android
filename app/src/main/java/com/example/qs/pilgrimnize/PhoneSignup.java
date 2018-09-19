package com.example.qs.pilgrimnize;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneSignup extends AppCompatActivity implements View.OnClickListener {

    private Button nextBtn;
    private TextView back;
    private EditText phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_signup);

        findViewById(R.id.nextBtn).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextBtn:
                showUsernameActivity();
                break;
            case R.id.backButton:
                backToSignUpActivity();
                break;
        }
    }
    public void showUsernameActivity(){

        // check the phone number
        String  phonenumber = phoneNumber.getText().toString().trim();

        Boolean flag = false;
        System.out.println(phoneNumber.getText().toString());
        if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumber.setError("Phone number is required");
            phoneNumber.requestFocus();
            return;
        } else if (phonenumber.length() < 10 ) {
                phoneNumber.setError("Please Enter valid phone number");
                phoneNumber.requestFocus();
            } else {
                flag = true;
                Intent userNameActivity = new Intent(getApplicationContext(),UserNamePhoneSignup.class);
                userNameActivity.putExtra("PHONE_NUMBER",phonenumber);
                startActivity(userNameActivity);
                finish();
            }
            System.out.println(flag);
    }
    public void backToSignUpActivity(){
        startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
        finish();
    }
}
