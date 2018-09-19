package com.example.qs.pilgrimnize;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ServicesClass extends AppCompatActivity {

    private FirebaseDatabase database;

    List<Report> reports = new ArrayList<>();
    private List titles = new ArrayList<>();

    public void getAddressFromLocation(
            final Location location, final Context context) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality();
                    }
                } catch (IOException e) {
                    Log.e("TAG", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
//                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public void getLocationName(Double lat, Double lng) {
        try {
            URL url = new URL("maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true");
            // making connection
//            System.out.println("OUT"+ url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            // Reading data's from url
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            String out = "";
//            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                out += output;
            }
//            System.out.println("OUT"+out);
            // Converting Json formatted string into JSON object
//            JsonParser parser = new JsonParser();
//            JsonObject json = (JsonObject) parser.parse(out);
//            JsonArray results = json.getAsJsonArray("results");

            //codes for getting sublocality...

//            System.out.println(results);
            conn.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void tableViewInit(List<String> titles) {
//        TableLayout ll = findViewById(R.id.tableView);
        System.out.println(titles);

    }
    public void addOnClickListner() {
        System.out.println("ON CLick LIstrent");
    }

    public TableRow add(Context context,TableLayout ll ,int color, int ii, List<String> titles,User userObject){

        final TableRow tr = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1000);
        tr.setLayoutParams(lp);

        TextView tv = new TextView(context);
        tv.setText("GGGGEEGE");
        tr.setBackgroundColor(color);

        final LinearLayout layout = new LinearLayout(context);
        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(llp);
        llp.setMargins(10, 10, 10, 10);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.CYAN);

        if (userObject.userType == 0){
            tv.setText(titles.get(ii));
            layout.addView(tv);
            tr.addView(layout);
        }else if (userObject.userType == 1){

            TextView descrption = new TextView(context);
            descrption.setHeight(200);
            descrption.setTextSize(20);
            descrption.setPadding(10, 10, 10, 10);

            TextView phone = new TextView(context);
            phone.setHeight(80);
            phone.setTextSize(19);
            phone.setPadding(10, 10, 10, 10);

            TextView locationName = new TextView(context);

            locationName.setTextSize(19);
            locationName.setHeight(70);
            locationName.setPadding(10, 10, 10, 10);

            TextView dateTime = new TextView(context);
//        dateTime.setText(Long.toString(report.createionDate));
            dateTime.setText("dateTime");
            dateTime.setHeight(90);
            dateTime.setPadding(10, 10, 10, 10);

            layout.addView(descrption);
            layout.addView(phone);
            layout.addView(locationName);
            layout.addView(dateTime);
            tr.addView(layout);
        }

        return  tr;
    }
    public void initTheTableRows( List<String> titles ){
//        TableLayout ll = (TableLayout) findViewById(R.id.tableView);
//        ll.setBackgroundColor(Color.GREEN);
//        ll.removeAllViews();

    }
    public List<Report> fetchAllTheRepoorts(final int i) {

        final List<Report> reports = new ArrayList<>();
        final List<Report> finshedReports = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);
        DatabaseReference ref = database.getReference().child("Reports");
        ChildEventListener creationDate = ref.orderByChild("creationDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                System.out.println(s);

                HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : object.keySet()) {
                    Map<String, Object> opbb = (Map<String, Object>) object.get(key);
                    Report report = new Report(dataSnapshot.getKey(), opbb);
//                    System.out.println("-------------/"+report.uid);
//                    System.out.println("-------------+"+report.activicaion);

                    if (report.activicaion == 0 && report.officeNumber == i) {
                        reports.add(report);
                    } else {
                        finshedReports.add(report);
                    }
                }
                System.out.println("00999 onChildAdded 1 " + reports);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
                for (int i = 0; i < reports.size(); i++) {
                    Report re = reports.get(i);
                    if (re.uid == dataSnapshot.getKey()) {
                        reports.remove(re);
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
        System.out.println("ddddd dd d " + reports.size());
        return reports;
    }

    public List<User> fetchAllUsers() {
        final List<User> users = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        database.getReference().child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                System.out.println("--USERS---"+dataSnapshot);
                HashMap<String, Object> dict = (HashMap<String, Object>) dataSnapshot.getValue();
//                for (String key: dict.keySet()){
//                    System.out.println();
//                }
//                HashMap<String,Object> dict = (HashMap<String,Object>) dataSnapshot.getValue();

                String uid = dataSnapshot.getKey();
                User user = new User(uid, dict);
                System.out.println(uid);
//                System.out.println("kdfvm + "+user.userType);
                if (user.userType == 1) {
//                    System.out.println("--++--");
                    users.add(user);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        System.out.println("dsdsdd   dddd d " + users.size());
        return users;
    }

    public List<Report> fetchFinshedReports() {
        final List<Report> finshedReports = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        database.getReference().child("FinshedReports").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("fetchFinshedReports "+ dataSnapshot);

                HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : object.keySet()) {
                    Map<String, Object> opbb = (Map<String, Object>) object.get(key);
                    Report report = new Report(dataSnapshot.getKey(), opbb);
                    System.out.println("finshedReports "+ report.endDate);
                    finshedReports.add(report);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        return finshedReports;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        System.out.println(time);
//        if (time < 1000000000000L) {
        System.out.println("Enter here ");
        //if timestamp given in seconds, convert to millis time *= 1000; }
        long now = System.currentTimeMillis();

//            if (time > now || time <= 0) {
//                return null;
//            }
// TODO: localize final long diff = now - time;

        final long diff = now - time;
        System.out.println("diff "+ diff +" "+"now "+ time);
        if (diff < MINUTE_MILLIS) {

            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
//        }
//        return "0";

    }
}
