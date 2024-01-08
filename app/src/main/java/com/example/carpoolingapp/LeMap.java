package com.example.carpoolingapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class LeMap extends AppCompatActivity implements OnMapReadyCallback{

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        gMap = googleMap;

        if(locationsPermissionsGranted){
            Log.d("TAG", "onMapReady: Location permission granted");
            getDeviceLocation();
        }
    }

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private  static final float DEFAULT_ZOOM = 15f;

    Boolean locationsPermissionsGranted = false;

    GoogleMap gMap;


    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_map);

        Intent intent = getIntent();
        String sourceLocation = intent.getStringExtra("sourceLoc");
        String destinationLocation = intent.getStringExtra("destLoc");

        Log.d(TAG, "onCreate: " + sourceLocation + "dest " + destinationLocation);

        if (sourceLocation != null && destinationLocation != null) {
            Geocoder geocoder = new Geocoder(LeMap.this);
            try {
                List<Address> sourceAddresses = geocoder.getFromLocationName(sourceLocation, 1);
                List<Address> destinationAddresses = geocoder.getFromLocationName(destinationLocation, 1);
                Log.d(TAG, "addresses: " + sourceAddresses + " dest " + destinationAddresses);

                if (sourceAddresses != null && !sourceAddresses.isEmpty() && destinationAddresses != null && !destinationAddresses.isEmpty()) {
                    Address sourceAddress = sourceAddresses.get(0);
                    Address destinationAddress = destinationAddresses.get(0);

                    LatLng sourceLatLng = new LatLng(sourceAddress.getLatitude(), sourceAddress.getLongitude());
                    LatLng destinationLatLng = new LatLng(destinationAddress.getLatitude(), destinationAddress.getLongitude());
                    Log.d(TAG, "LatLng: " + sourceLatLng + " dest " + destinationLatLng);

                    float[] results = new float[1];
                    Location.distanceBetween(sourceLatLng.latitude, sourceLatLng.longitude,
                            destinationLatLng.latitude, destinationLatLng.longitude, results);

                    float distanceInMeters = results[0];
                    float distanceInKm = distanceInMeters / 1000f;

                    Log.d("DISTANCE", "Distance between " + sourceLocation + " and " + destinationLocation + " is " + distanceInKm + " km");
                    Toast.makeText(LeMap.this, "Distance between " + sourceLocation + " and " + destinationLocation + " is " + distanceInKm + " km", Toast.LENGTH_SHORT).show();

                    Intent dist = new Intent();
                    dist.putExtra("distance", distanceInKm);
                    setResult(Activity.RESULT_OK, dist);
                    finish();

                } else {
                    //Log.d("DISTANCE", "Could not find the location");
                    Toast.makeText(LeMap.this, "Could not find the location", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Log.d("DISTANCE", "Source or destination location is null");
            Toast.makeText(LeMap.this, "Source or destination location is null", Toast.LENGTH_SHORT).show();
        }

        getLocationPermission();
    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationsPermissionsGranted) {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            Log.d(TAG, "onComplete: current location " + currentLocation);
                            moveCamera(new LatLng(-1.276316, 36.793910), DEFAULT_ZOOM);

                        }
                        else{
                            Log.d(TAG, "onComplete: current location is null!");
                            Toast.makeText(LeMap.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.e("TAG", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }



    private void moveCamera(LatLng latLng, float zoom){
        Log.d("Tag", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        //Log.d("TAG", "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(LeMap.this);
    }

    private void getLocationPermission() {
        //Log.d("TAG", "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationsPermissionsGranted = true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d("Tag", "onRequestPermissionsResult: called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationsPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationsPermissionsGranted = false;
                            //Log.d("Tag", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    //Log.d("Tag", "onRequestPermissionsResult: permission granted");
                    locationsPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }

}