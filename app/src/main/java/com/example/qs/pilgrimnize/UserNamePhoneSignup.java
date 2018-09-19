package com.example.qs.pilgrimnize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserNamePhoneSignup extends AppCompatActivity implements View.OnClickListener {

    private EditText userNamePhone;
    private Button nextToValdion;
    private TextView backToPhone;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_phone_signup);

        findViewById(R.id.backBtnToPhoneNumber).setOnClickListener(this);
        findViewById(R.id.nextToValdion).setOnClickListener(this);

        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");

        userNamePhone = (EditText) findViewById(R.id.userNamePhoneSignup);
        System.out.println("phoneNumber"+ phoneNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtnToPhoneNumber:
                // getBack To Phone
                backToPhnoeNumber();
                break;
            case R.id.nextToValdion:
                // go to Valdion
                nextToValdionActivity();
                break;
        }
    }
    public void backToPhnoeNumber(){
        startActivity(new Intent(getApplicationContext(),PhoneSignup.class));
        finish();
    }
    public void nextToValdionActivity(){
        String userName = userNamePhone.getText().toString().trim();

        if (userName.isEmpty()){
            userNamePhone.setError("Please Enter user name");
            userNamePhone.requestFocus();
            return;
        }else {
//            start Activity
            activionActivity(userName);
        }
    }
    public void activionActivity(String userName) {
        Intent actionvActivity = new Intent(getApplicationContext(),ValidationActivity.class);
        actionvActivity.putExtra("PHONE_NUMBER",phoneNumber);
        actionvActivity.putExtra("USER_NAME",userName);
        startActivity(actionvActivity);
    }
}
