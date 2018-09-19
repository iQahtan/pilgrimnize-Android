package com.example.qs.pilgrimnize;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String channelId = "Channel 1 ";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            NotificationChannel channel1 = new NotificationChannel(channelId,"Report Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("enable to recive the Reports notifiction");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
