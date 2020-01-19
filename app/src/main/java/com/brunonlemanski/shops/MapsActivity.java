package com.brunonlemanski.shops;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.brunonlemanski.shops.database.DbAdapter;
import com.brunonlemanski.shops.database.DbHelper;
import com.brunonlemanski.shops.database.MarkerObject;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


/**
 * Maps Activity
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SQLiteDatabase database;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;


    private String latitude = "";
    private String longitude = "";

    private ArrayList<MarkerObject> objects;
    private ArrayList<LatLng> objectsLocalization;


    /**
     * On Create method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DbHelper dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();

        geofencingClient = LocationServices.getGeofencingClient(this);

        objects = new ArrayList<>();
        objectsLocalization = new ArrayList<>();

        // ----- Methods stack ----- \\
        getDataFromDatabase();

    }


    /**
     * On Stop method
     */
    @Override
    protected void onStop() {
        super.onStop();
        database.close();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        setMarker();
        addGeofence();

    }


    /**
     * Adding geofence to localizations.
     */
    @SuppressLint("MissingPermission")
    public void addGeofence(){

        for(int i = 0; i < objectsLocalization.size(); i++) {

            MarkerObject temp = objects.get(i);
            LatLng latLng = objectsLocalization.get(i);

            Geofence geofence = new Geofence.Builder().setRequestId("Geo" + i)
                    .setCircularRegion(latLng.latitude, latLng.longitude, Integer.valueOf(temp.getRadius()))
                    .setExpirationDuration(1000*60*60)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            geofencingClient.addGeofences(getGeofencingRequest(geofence), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("LOG-APP","Geofence added.");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("LOG-APP","Geofence failed to add."+e);
                        }
                    });
        }

        Log.i("LOG-GEO------- ", String.valueOf(geofencingClient));
    }



    /**
     * Geofencing request
     * @param geo
     * @return
     */
    @SuppressLint("MissingPermission")
    private GeofencingRequest getGeofencingRequest(Geofence geo) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geo);
        return builder.build();
    }



    /**
     * Pending intent for geofence
     * @return
     */
    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceIntentService.class);
        geofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }



    /**
     * Set marker on Maps
     */
    public void setMarker() {

        for (int i = 0; i < objectsLocalization.size(); i++) {

            MarkerObject temp = objects.get(i);

            mMap.addMarker(new MarkerOptions()
                    .position(objectsLocalization.get(i))
                    .title(temp.getName())
                    .snippet(temp.getDesc()));
            mMap.addCircle(new CircleOptions()
                    .center(objectsLocalization.get(i))
                    .radius(Integer.valueOf(temp.getRadius()))
                    .strokeColor(Color.argb(80, 84, 161, 255))
                    .fillColor(Color.argb(60, 84, 161, 255)));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(15.0f));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(objectsLocalization.get(i)));
        }
    }



    /**
     * Get data from database
     */
    public void getDataFromDatabase(){

        String name;
        String desc;
        String radius;
        String location;

        Cursor cursor = getMarker();
        if(cursor.getCount() == 0){
            showMessage("Database error", "There is no data!");
            return;
        }

        while(cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME));
            desc = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_DESC));
            radius = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_RADIUS));
            location = cursor.getString(cursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_LOCATION));

            String[] location_temp = location.split(";", 2);

            latitude = location_temp[0];
            longitude = location_temp[1];

            objects.add(new MarkerObject(name, desc, radius));
            objectsLocalization.add(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
        }
    }



    /**
     * Query for data getting
     * @return
     */
    public Cursor getMarker() {
        Cursor cursor = database.query(DbAdapter.DbEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DbAdapter.DbEntry.COLUMN_TIMESTAMP + " DESC"
        );

        return cursor;
    }



    /**
     * Push alert dialog on the screen
     * @param title
     * @param message
     */
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
