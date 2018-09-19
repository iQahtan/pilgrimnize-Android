package com.example.qs.pilgrimnize;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener{

    EditText phoneNumber,officeNumber,reportDecrip;
    User user;
    private FirebaseDatabase database;
    Button reportButton;
    private String locationName;
    Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        database = FirebaseDatabase.getInstance();
        phoneNumber = findViewById(R.id.phoneTF);
        officeNumber = findViewById(R.id.officeTF);
        reportDecrip = findViewById(R.id.reportDescrip);
        findViewById(R.id.reportButton).setOnClickListener(this);

        reportButton = findViewById(R.id.reportButton);
        user = (User) getIntent().getParcelableExtra("USER");

        System.out.println("user     + "+user);
        if (user.location != null){

            geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(user.location.get(0),user.location.get(1),1);
                System.out.println("addresses "+ addresses);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
//            System.out.println("city "+ city);
                String street = addresses.get(0).getThoroughfare();
//                System.out.println();
//                locationName = street;
                List<String> myList = new ArrayList<String>(Arrays.asList(address.split(",")));
                System.out.println("myList "+myList.get(1));
                locationName = myList.get(1);

                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                System.out.println("Location Name"+ locationName);
            } catch (IOException e) {
                System.out.println("Field to get ");
                e.printStackTrace();
            }
        }else {
            locationName = "Unknow";
        }
        System.out.println("Location Name"+ locationName);
        ServicesClass servicesClass = new ServicesClass();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reportButton:
                System.out.println("reportButton");
                saveReportToDataBase();
                break;
        }
    }
    public void saveReportToDataBase() {


        int phone = Integer.parseInt(phoneNumber.getText().toString());
        int office = Integer.parseInt(officeNumber.getText().toString());
        String discripe = reportDecrip.getText().toString();

        System.out.println(phone);
        Report report = new Report(phone,discripe,0);
        report.location = user.location;
        report.officeNumber = office;
        report.createionDate = System.currentTimeMillis();
        report.reportedUser = user.uid;
        report.locationName = locationName;
        // save it to database
        Map<String,Object> reportMap = new HashMap<>();

        reportMap = report.toMap();
        System.out.println(reportMap);
        database.getReference().child("Reports").push().child(user.uid).setValue(reportMap);

        Toast.makeText(getApplicationContext(),"We recive your report,And we will contact with you soon.Thank you",Toast.LENGTH_SHORT).show();
        reportButton.setEnabled(false);
    }
}
