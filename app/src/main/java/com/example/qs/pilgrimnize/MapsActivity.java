package com.example.qs.pilgrimnize;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private User user;
    private FirebaseDatabase database;
    private Report report;

    private Button goBtn;
    private Button endBtn;

    private Button centerBtn;
    private List<Report> reports;
    private List<User> users;
    private Location userLocation;
    private MapService mapService;
    private List<Marker> markers;
    Object dataTransfer[] = new Object[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);


        user = (User) getIntent().getParcelableExtra("USER");
        if (user.userType == 1 ){
            report = (Report) getIntent().getParcelableExtra("REPORT");
            if (report.location != null){
                System.out.println(report.location);
            }
        }

        centerBtn = findViewById(R.id.centerBtn);
        centerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drivMood();
            }
        });
        goBtn = findViewById(R.id.goButton);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// uddate the camera to the Zoom AND
                System.out.println(report.uid);
                System.out.println(user.uid);

                updateReportStatus(1,user.username);
                // Update Camera to the Start Trip mood
//                drivMood();
                Location myLocation = mMap.getMyLocation();
                if (myLocation != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(myLocation.getLatitude(), myLocation
                                    .getLongitude()), 19));
                }
            }
        });
        endBtn = findViewById(R.id.endButton);
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // the (Employ write the report and the activion 3 Completed)
                beludAlert();
                System.out.println("-------");
            }
        });
        mapService = new MapService();
        database = FirebaseDatabase.getInstance();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void drivMood() {
        flage = true;
        LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(userLatLng, 19)));
    }

    public void beludAlert() {
        System.out.println("beludAlert");

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_employ_report, null);
        System.out.println("beludAlert");

        final EditText massage = (EditText) mView.findViewById(R.id.employMassage);
        Button sendButton = (Button) mView.findViewById(R.id.sendButton);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!massage.getText().toString().isEmpty()) {
                    final String message = massage.getText().toString().trim();
                    System.out.println(message);
                    Toast.makeText(MapsActivity.this,
                            "Your report had been sent.Thank you",
                            Toast.LENGTH_SHORT).show();
                    updateReportStatus(2, message);

//                    startActivity(new Intent(getApplicationContext(), TheRootActivity.class));
//                    finish();
                } else {
                    Toast.makeText(MapsActivity.this,
                            "Please fill up all the Fields",
                            Toast.LENGTH_SHORT).show();
                    massage.requestFocus();
                }
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        System.out.println("BackButtonPressed");
//        startActivity(new Intent(getApplicationContext(), TheRootActivity.class));
//    }

    public void updateReportStatus(int activion, String employAction) {


        /// Create alert with Edite Text
        HashMap<String, Object> dict = new HashMap<String, Object>();
        if (activion == 1) {
            // Activ
            dict.put("active", activion);
            dict.put("userInCharge", employAction);
            System.out.println("First======");
        }
        if (activion == 2) {
            // completed
            System.out.println("2======");
            dict.put("active", activion);
            //create EditeText
            dict.put("employReport", employAction);
            dict.put("endDate", System.currentTimeMillis());
            dict.put("userId",user.username);

//            Map<String,Object> reportMap = report.toMap();
//            reportMap.put("activicaion", activion);
//            reportMap.put("employReport", employAction);
//            reportMap.put("endDate", System.currentTimeMillis());
        }
        DatabaseReference from = database.getReference().child("Reports");
        System.out.println(from);
        DatabaseReference to = database.getReference().child("FinshedReport");
        database.getReference().child("Reports").child(report.uid).child(report.reportedUser).updateChildren(dict);
        mapService.moveFirebaseRecord(from,to);

        System.out.println("3======");
//        database.getReference().child("Reports").child(report.uid).removeValue();
    }

    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        System.out.println("LOCATION CHANGE");
        /// NETWORK PROVIDER
        if (user.userType == 1) {
            /// add marker to employ User and show direcion
            if (report.location != null){
                mapService.addMarkerForReport(mMap, report);
            }
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (report.location != null) {
                System.out.println("userLocation "+ userLocation);
                LatLng userLatlng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                LatLng reportLatlng = new LatLng(report.location.get(0), report.location.get(1));
                System.out.println("report.location " + report.location);
                String url = getDirectionsUrl(userLatlng, reportLatlng);

                dataTransfer = new Object[3];
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(reportLatlng.latitude, reportLatlng.longitude);
                getDirectionsData.execute(dataTransfer);
                updateCameraView(userLatlng);
            }
            System.out.println("LOCATION CHANGE Enterd");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    System.out.println("LOCATION CHANGE MAPS " + location);
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (flage == true) {
//                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(latlng, 19)));
                    }
                    if (user.userType == 1) {
                        updateUserLocationOnDatabase(location.getLatitude(), location.getLongitude());
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }
    private String getDirectionsUrl(LatLng userLatLng, LatLng destLatLng) {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + userLatLng.latitude + "," + userLatLng.longitude);
        googleDirectionsUrl.append("&destination=" + destLatLng.latitude + "," + destLatLng.longitude);
        googleDirectionsUrl.append("&key=" + "AIzaSyCmh6ljC5MlnSOIZWy_8eziBjeMXhM2xHk");

        return googleDirectionsUrl.toString();
    }

    private Boolean flage = false;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        updateLocation();
//        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                System.out.println("OnCameraIdleListener");
//            }
//        });
//        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int i) {
//                System.out.println("onCameraMoveStarted " + i);
//                // show a button to center the camera
////                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
////                System.out.println(location);
//            }
//        });
//        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
//            @Override
//            public void onCameraMoveCanceled() {
//                flage = false;
//                System.out.println("onCameraMoveCanceled");
//            }
//        });
        if (user.userType == 0){
            users = getIntent().getParcelableArrayListExtra("USERS");
            System.out.println("usersusersusers"+users.size());
            //     =======================================================================================
            //     ====================================== ADMIN ==========================================
            //     =======================================================================================
            goBtn.setVisibility(View.GONE);
            endBtn.setVisibility(View.GONE);
            // SET UP CAMERA
//            reports = (List<Report>) getIntent().getParcelableExtra("REPORTS");
            reports = getIntent().getParcelableArrayListExtra("REPORTS");
//            System.out.println("------------"+reports);
//            List<Marker> markrs = mapService.addMarkrsToReports(reports,mMap);
//            System.out.println("markrs----- "+markrs);
//            mapService.changTheMarkerStatus(markrs,mMap);
            mapService.changTheMarkerStatus(mMap,user);
            System.out.println("------------"+users);
            markers = new ArrayList<>();

            markers = mapService.addMarkersToUsers(users,mMap);

            mapService.changeUsersLoactionOnTheMap(markers,mMap);
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    flage = false;
                }
            });
        }
    }

    public void updateCameraView(LatLng reportLatlng) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(reportLatlng,15)));
    }
    public void updateUserLocationOnDatabase(Double lat,Double log) {
//        String uid = mAuth.getUid().toString();

        List location = new ArrayList<>();
        String uid = user.uid;
        location.add(lat);
        location.add(log);
        System.out.println("updateUserLocationOnDatabase");
        HashMap<String,Object> dict = new HashMap<String,Object>();
        dict.put("location",location);
        database.getReference("Users").child(uid).child("location").setValue(location);
    }
}
