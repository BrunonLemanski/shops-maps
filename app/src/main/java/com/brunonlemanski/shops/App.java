package com.brunonlemanski.shops;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;


/**
 * App class
 */
public class App extends Application {

    //Notifications
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_1_NAME = "New product";



    /**
     * On Create method
     */
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }



    /**
     * Creating new notification when product will be added.
     *
     */
    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    CHANNEL_1_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Show new product which was added to database.");
            channel1.setImportance(NotificationManager.IMPORTANCE_HIGH);

            Log.w("------TEST: ", "utworzono notyfikacje");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
