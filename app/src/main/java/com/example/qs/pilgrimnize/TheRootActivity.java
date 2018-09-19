package com.example.qs.pilgrimnize;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheRootActivity extends AppCompatActivity implements View.OnClickListener {


    private FusedLocationProviderClient fusedLoactionClient;
    private LocationRequest locationRequest;
    private List<Report> reports = new ArrayList<>();
    private ProgressBar progressBar;
    private ServicesClass service;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    private NotificationManagerCompat notificationManager;
    List<String > titles = new ArrayList<>();
    private LocationManager locationManger;
    private User userObject;
    private ArrayList<Intent> arrayOfActivivty;
    private Double lat;
    private Double log;

    private RecyclerView recyclerView;
    private ReportAdapter adapter;

    private int notificationId = 5 ;
    private  List<Marker> markers = new ArrayList<>();

//    private NotificationCompat.Builder mBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_root);

        progressBar = findViewById(R.id.progressBarTheRoot);
        progressBar.getIndeterminateDrawable().setColorFilter(R.color.colorPrimary, PorterDuff.Mode.MULTIPLY);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        service = new ServicesClass();
        System.out.println("onCreate");
        findViewById(R.id.loginButton);
        database =  FirebaseDatabase.getInstance();
        System.out.println("Hello Form The Root");

        checkTheUserStatus();
        System.out.println("userObject   "+ userObject);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("userObject   "+ userObject);
        return super.onCreateOptionsMenu(menu);
    }

    private void checkTheUserStatus() {
        mAuth = FirebaseAuth.getInstance();

//        handleUserLogout();
        if (mAuth.getCurrentUser() == null) {
            System.out.println("the user in null");
            // if the user is null we move to login activity
            getLoginActivity();
            finish();
        } else {
            // if user not null we keep going in bulding
            FirebaseUser user = mAuth.getCurrentUser();
            System.out.println("USER IS NOT NULL ");
            // Fetch the user data from datebase
//            progressBar.setVisibility(View.GONE);
            fetchUser(mAuth);
            // Here, thisActivity is the current activity
        }
    }
    public void getLoginActivity() {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }
    private void fetchUser(FirebaseAuth mAuth){
        if (mAuth.getCurrentUser() != null) {
            // fetch user from data base
//            progressBar.setVisibility(View.VISIBLE);
            FirebaseDatabase database =  FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users");
            final String uid = mAuth.getUid().toString();
            myRef.child(uid);

             myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    HashMap<String,Object> dict = (HashMap<String,Object>) dataSnapshot.getValue();

                    userObject = new User(uid,dict);
                    System.out.println("userObject  "+ userObject);
                    System.out.println("==== 2 ");
                    updateViewWithUser(user);
//                    createIntent(user);
                    System.out.println(userObject.uid + " "+ userObject.userType +" "+ userObject.location+" "+userObject.officeNumber);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void updateUserLocationOnDatabase(Double lat,Double log) {

        String uid = mAuth.getUid().toString();
        List location = new ArrayList<>();
        location.add(lat);
        location.add(log);
        System.out.println("updateUserLocationOnDatabase");
        HashMap<String,Object> dict = new HashMap<String,Object>();
        dict.put("location",location);
        database.getReference("Users").child(uid).child("location").setValue(location);
    }


    public void updateViewWithUser(final User user){
        final DatabaseReference finshedRef = database.getReference().child("FinshedReports");
//        final TableLayout ll = findViewById(R.id.tableView);

        if (user.userType == 2 ){
            List titles = new ArrayList<>();
            requestLocationUpdate();

            titles.add("Report");
            adapter = new ReportAdapter(TheRootActivity.this,reports,userObject,service);
            recyclerView.setAdapter(adapter);
//            progressBar.setVisibility(View.GONE);
            }else if (user.userType == 0 ){
            adapter = new ReportAdapter(TheRootActivity.this,reports,userObject,service);
            recyclerView.setAdapter(adapter);
//            progressBar.setVisibility(View.GONE);
            }else {
            database.getReference().child("Reports").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //

                    System.out.println("We're done loading the initial "+dataSnapshot.getChildrenCount()+" items "+ titles);
//                    if (user.userType == 1 ){
//                        init(titles);
                        adapter = new ReportAdapter(TheRootActivity.this,reports,userObject,service);
                        recyclerView.setAdapter(adapter);
//                        progressBar.setVisibility(View.GONE);
                    System.out.println(adapter+"fdfd    ffff "+ reports.size());
//                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

                public void onCancelled(FirebaseError firebaseError) { }
            });
            database.getReference().child("Reports").orderByChild("creationDate").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key: object.keySet()){
                        Map<String,Object> opbb = (Map<String, Object>) object.get(key);
                        Report report = new Report(dataSnapshot.getKey(), opbb);
//                    System.out.println("-------------/"+report.uid);
//                    System.out.println("-------------+"+report.activicaion);

                        if (report.officeNumber == userObject.officeNumber){
                            if (report.location != null){
//                                markers.add()
                            }
                            System.out.println("Enter here finshed");
                            if (report.activicaion == 0){
                                titles.add(report.describe);
                                reports.add(report);
                                sendNotifiction(report);
                            }
                        }
                    }
                    System.out.println("00999 onChildAdded 1 "+ reports);
                }
                @Override
                public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
//                    Report re = null;
                    System.out.println("DATASNAPSHOT "+ dataSnapshot);
                    HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : object.keySet()) {
                        Map<String, Object> opbb = (Map<String, Object>) object.get(key);
                        Report report = new Report(dataSnapshot.getKey(), opbb);

                        if (report.activicaion == 2 ){
                            System.out.println("YYYYYYYYYYAHO");
                            finshedRef.child(report.uid).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null){
                                        System.out.println("Field to move the data");
                                    }else {
                                        System.out.println("COPY DONE");
                                        database.getReference().child("Reports").child(dataSnapshot.getKey()).removeValue();
                                    }
                                }
                            });
                            System.out.println(opbb);
                        }
                    }

                    updateReports(dataSnapshot.getKey().toString());
                    String uid = dataSnapshot.getKey();

                    System.out.println("init reports "+ reports.size());
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            /// Here we fill the Reports For the emoly
            // ============================================= this update the Location
            requestLocationUpdate(); //=====================
            // =============================================
        }
    }
    public void sendNotifiction(Report report){
        notificationManager = NotificationManagerCompat.from(this);

        Notification notification = new NotificationCompat.Builder(this,App.channelId)
                .setSmallIcon(R.drawable.notification_icon).setContentText(report.describe).setContentTitle("Hello")
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
        notificationManager.notify(1,notification);
    }
    public void updateReports(String uid) {

        for (int i=0;i<reports.size();i++){
            Report re = reports.get(i);
            System.out.println("if this "+re.uid+" == "+ "this ? "+uid);
            if (re.uid.equals(uid)){
                            reports.remove(re);

                titles.remove(re.describe);
                adapter = new ReportAdapter(TheRootActivity.this,reports,userObject,service);
                recyclerView.setAdapter(adapter);
                System.out.println(reports.size()+"  "+titles.size());
                System.out.println("re.uid == dataSnapshot.getKey() "+ reports.size());
            }
        }
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {

        System.out.println("onCreatePanelMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_button, menu);

        return super.onCreatePanelMenu(featureId, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        handleUserLogout();
        return super.onOptionsItemSelected(item);
    }
    public void handleUserLogout(){
        mAuth.signOut();
        getLoginActivity();

    }
    @Override
    public void onClick(View v) {
    }
    public void requestLocationUpdate() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PermissionChecker.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                        == PermissionChecker.PERMISSION_GRANTED ) {

            fusedLoactionClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(4000);

            fusedLoactionClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    lat = task.getResult().getLatitude();
                    log = task.getResult().getLongitude();
                    updateUserLocationOnDatabase(lat,log);
                    System.out.println("Locationn" + lat + " "+ log);
                }
            });
            fusedLoactionClient.requestLocationUpdates(locationRequest, new LocationCallback(){
                @Override
                public void  onLocationResult(LocationResult locationResult){
                    super.onLocationResult(locationResult);

                    lat = locationResult.getLastLocation().getLatitude();
                    log = locationResult.getLastLocation().getLongitude();
                    System.out.println("LOCATION CHANGE TheROOT");
                }
            }, getMainLooper());
        }else callPermission();
    }
    public void callPermission() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        com.nabinbhandari.android.permissions.Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                requestLocationUpdate();
            }
            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                callPermission();
            }
        });
    }
}
