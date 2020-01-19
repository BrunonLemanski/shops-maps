package com.brunonlemanski.shops;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static com.brunonlemanski.shops.App.CHANNEL_1_ID;

/**
 * Intent service for Geofence
 */
public class GeofenceIntentService extends IntentService {

    private NotificationManagerCompat notificationManager;

    public GeofenceIntentService() {
        super("GeofenceIntentService");
    }


    /**
     * On Create method
     */
    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = NotificationManagerCompat.from(this);
    }



    /**
     * On handle intent which handle action with markers
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("a", "geofencing event error");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );
            Log.e("-------------------------------------------------", geofenceTransitionDetails);
            // Send notification and log the transition details.


            if(geofenceTransition == 1) {
                sendNotification(geofenceTransitionDetails, "Shop enter");
            }else if ((geofenceTransition == 2)) {
                sendNotification(geofenceTransitionDetails, "Shop exit");
            }

            Log.i("c", geofenceTransitionDetails);

        } else {
            Log.e("d", "geofence transition invalid type");
        }
    }



    /**
     * Get Geofence Transition Details
     * @param context
     * @param geofenceTransition
     * @param triggeringGeofences
     * @return
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }



    /**
     * Sending notification
     * @param notificationDetails
     * @param action
     */
    private void sendNotification(String notificationDetails, String action){

        Log.e("------------------------------------------------- NOTIFICATION", "method: sendNotification()");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_marker)
                .setContentTitle(notificationDetails)
                .setContentText(action)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }



    /**
     * Get Transition string type
     * @param transitionType
     * @return
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "transition - enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "transition - exit";
            default:
                return "unknown transition";
        }
    }
}
