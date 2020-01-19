package com.brunonlemanski.shops;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.brunonlemanski.shops.database.DbAdapter;
import com.brunonlemanski.shops.database.DbHelper;

public class AddShopActivity extends Activity {

    //UI elements
    private EditText shop;
    private EditText desc;
    private EditText radius;
    private Button saveShop;

    private SQLiteDatabase database;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private String locationTemp;
    private boolean gpsEnable = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form_shop);

        DbHelper dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        shop = findViewById(R.id.eTshopName);
        desc = findViewById(R.id.eTdescription);
        radius = findViewById(R.id.eTRadius);
        saveShop = findViewById(R.id.saveNewShop);


        saveShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationTemp == null || locationTemp == ""){
                    Toast.makeText(AddShopActivity.this, "Nie pobrano lokalizacji!", Toast.LENGTH_LONG).show();
                }else {
                    saveData(shop.getText().toString(), desc.getText().toString(), radius.getText().toString());
                    Toast.makeText(AddShopActivity.this, "Dodano nowy sklep!", Toast.LENGTH_LONG).show();
                    backToShopActivity();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                locationTemp = location.getLatitude() + ";" + location.getLongitude();

                Log.i("-----------locationListener: -------- ", locationTemp);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                showMessage("GPS", "Lokalizacja nie została włączona. \nZostaniesz przeniesiony do ustawień systemowych.");
            }
        };

        getActualLocation();
    }

    /**
     * Method checking if module GPS is active.
     */
    private void checkGPSActive(){
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }


    /**
     * Method saving data to database.
     * @param name
     * @param desc
     * @param radius
     */
    private void saveData(String name, String desc, String radius) {
        ContentValues cv = new ContentValues();

        cv.put(DbAdapter.DbEntry.COLUMN_NAME, name);
        cv.put(DbAdapter.DbEntry.COLUMN_DESC, desc);
        cv.put(DbAdapter.DbEntry.COLUMN_RADIUS, radius);
        cv.put(DbAdapter.DbEntry.COLUMN_LOCATION, locationTemp);

        database.insert(DbAdapter.DbEntry.TABLE_NAME, null, cv);
    }

    /**
     * Back to ShopActivity
     */
    private void backToShopActivity() {
        Intent intent = new Intent(this, ShopsActivity.class);
        startActivity(intent);
    }

    /**
     * Getting actual location
     */
    private void getActualLocation() {
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10 );
                }

            String provider = locationManager.getBestProvider(criteria,false);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(provider);

            if(location != null) {
                onLocationChanged(location);
            } else {
                locationManager.requestSingleUpdate(provider,locationListener,null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLocationChanged(Location location) {
        Double lat = (Double) (location.getLatitude());
        Double lng = (Double) (location.getLongitude());
    }

    private void showMessage(String title, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alert.show();
    }

}
