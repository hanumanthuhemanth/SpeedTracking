package com.hemanth.poc.speedtracking;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eam.arcgis.speedtracking.R;

public class MainActivity extends Activity implements GPSCallback{
    private GPSManager gpsManager = null;
    private double speed = 0.0;
    Boolean isGPSEnabled=false;
    LocationManager locationManager;
    double currentSpeed,kmphSpeed;
    TextView txtview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtview=(TextView)findViewById(R.id.info);
        try {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getCurrentSpeed(View view){
        txtview.setText("Waiting for GPS...");
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        gpsManager = new GPSManager(MainActivity.this);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled) {
            gpsManager.startListening(getApplicationContext());
            gpsManager.setGPSCallback(this);
        } else {
            gpsManager.showSettingsAlert();
        }
    }

    @Override
    public void onGPSUpdate(Location location) {
        speed = location.getSpeed();
        currentSpeed = round(speed,3,BigDecimal.ROUND_HALF_UP);
        kmphSpeed = round((currentSpeed*3.6),3,BigDecimal.ROUND_HALF_UP);
        txtview.setText(kmphSpeed+" km/h");
    }

    @Override
    protected void onDestroy() {
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
        gpsManager = null;
        super.onDestroy();
    }

    public static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
}