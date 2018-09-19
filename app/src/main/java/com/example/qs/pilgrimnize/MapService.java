package com.example.qs.pilgrimnize;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapService {

    private FirebaseDatabase database;

    public void addMarkerForReport(GoogleMap gmap, Report report) {
        // =====---------- Add Macrkerc
        if (report.location != null){
            LatLng reportLocation = new LatLng(report.location.get(0), report.location.get(1));
            gmap.addMarker(new MarkerOptions().position(reportLocation).title(report.describe));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(reportLocation));
        }
        // ===----------- - - - - - -  - -Show Deriction

    }

//    public List<Marker> addMarkrsToReports(List<Report> reports, GoogleMap gmap) {
//        System.out.println("addMarkrsToReports "+ reports);
//        List<Marker> markers = new ArrayList<>();
//        for (int i = 0; i < reports.size(); i++) {
//            Report report = reports.get(i);
//            if (report.location != null){
//                LatLng reportLocation = new LatLng(report.location.get(0), report.location.get(1));
//                Marker marker = gmap.addMarker(new MarkerOptions().position(reportLocation).title(report.describe).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
//                gmap.moveCamera(CameraUpdateFactory.newLatLng(reportLocation));
//                markers.add(marker);
//            }
//        }
//        return markers;
//    }
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }
    public List<Marker> addMarkersToUsers(List<User> users, GoogleMap gmap) {
        List<Marker> markers = new ArrayList<>();
//        System.out.println("22222222--- " + users);
//        System.out.println("22222222--- " + users);
        System.out.println(users.size());
//        System.out.println("[[[[[[[[[[[[[[]][][][]]["+users);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
//            System.out.println(user);
//
//            System.out.println(user.location);
            System.out.println(user.location.get(0).getClass());
            LatLng reportLocation = new LatLng((double)user.location.get(0), (double)user.location.get(1)); // the problem is here
            System.out.println(reportLocation);
            Marker marker = gmap.addMarker(new MarkerOptions().position(reportLocation).title(user.username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            markers.add(marker);
            gmap.moveCamera(CameraUpdateFactory.newLatLng(reportLocation));
//
        }
        return markers;
    }
    //final List<Marker> markers
    public void changTheMarkerStatus(final GoogleMap mMap, final User user){
//        System.out.println("markers "+ markers);
        final List<Marker> markers2 = new ArrayList<>();
        final List<Report> reports= new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Reports").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                /// Admin
                HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : object.keySet()) {
                    Map<String, Object> opbb = (Map<String, Object>) object.get(key);
                    Report report = new Report(dataSnapshot.getKey(), opbb);
                    reports.add(report);
//                    System.out.println("-------------/"+report.uid);
//                    System.out.println("-------------+"+report.activicaion);
                    if (report.activicaion != 2 && report.officeNumber == user.officeNumber) {
//                        reports.add(report);
                        if (report.location != null){
                            LatLng reportLocation = new LatLng(report.location.get(0),report.location.get(1));
                            Marker ma = mMap.addMarker(new MarkerOptions().position(reportLocation));
                            if (report.activicaion == 0 ){
                                // Red Marker
                                ma.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            }else {
                                // green
                                ma.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            }
//                            LatLng reportLocation = new LatLng(report.location.get(0),report.location.get(1));
//                            Marker marker = mMap.addMarker(new MarkerOptions().position(reportLocation).title(report.describe).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            ma.setTitle(report.describe);
                            markers2.add(ma);
                        }else {
                            LatLng reportLocation = new LatLng(20.4,20.1);

                            Marker ma = mMap.addMarker(new MarkerOptions().position(reportLocation));
                            if (report.activicaion == 0) {
                                ma.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            }else {
                                ma.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            }
//                            / here we add the reports that's dont have location
//
//                            Marker marker = mMap.addMarker(new MarkerOptions().position(reportLocation).title(report.describe).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            ma.setTitle(String.valueOf(report.phnoeNumber));
                            markers2.add(ma);
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("onChildChangeMAP"+ dataSnapshot);

                Map<String,Object> dict = (Map<String, Object>) dataSnapshot.getValue();
                String key = dict.keySet().toString();
                Report report = null;
                for(int i=0;i<reports.size();i++){
                    Report re = reports.get(i);
                    if (re.uid.equals(dataSnapshot.getKey())){
                        report = re;
                    }
                }
                System.out.println(report);

                for(int i=0;i<markers2.size();i++){
                    Marker marker = markers2.get(i);
                    if (marker.getTitle().toString().equals(report.describe)){
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("onChildRemoved");
                //
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("onChildMoved");
                // Here we remove The MARKER

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void changeUsersLoactionOnTheMap(final List<Marker> markers, final GoogleMap mMap){
        database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("dataSnapshot.getKey() "+ dataSnapshot.getKey());
                HashMap<String,Object> dict = (HashMap<String,Object>) dataSnapshot.getValue();
                User user = new User(dataSnapshot.getKey(),dict);
                System.out.println(user.uid);
                for (int i=0;i<markers.size();i++){
                    Marker marker = markers.get(i);
                    System.out.println("Marker "+marker.getTitle().toString()+" "+ user.username+" ");
                    if (user.username.equals(marker.getTitle().toString())){
                        System.out.println(user.location);
                        LatLng latLng = new LatLng(user.location.get(0),user.location.get(1));
                        marker.setPosition(latLng);
                    }
                }
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
    }
    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath) {

        fromPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                System.out.println("onDataChange "+ dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        fromPath.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                System.out.println("dataSnapshot=====  "+ dataSnapshot);
//                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isComplete()){
////                            Toast.makeText()
//                            System.out.println("Success");
//                        }else {
//                            System.out.println("Copy failed");
//                        }
//                    }
//                });
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

}