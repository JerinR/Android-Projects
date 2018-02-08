package com.example.app.gapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Location currLocation, startLocation;
    List<LatLng> positionTrack;
    Boolean trackingStarted, trackingInProgress;
    PolylineOptions polylineOptions;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    String str = "";
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        trackingStarted = false;
        trackingInProgress = true;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        polylineOptions = new PolylineOptions();
        positionTrack = new ArrayList<>();
        currLocation = null;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS not enables")
                    .setMessage("Would you like to enable GPS settings?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("DEMO", "MAPS ready");
        mMap = googleMap;
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currLocation = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                positionTrack.add(new LatLng(latitude, longitude));
                if (trackingInProgress) {
                    startLocation = location;
                    mMap.clear();
                    polylineOptions = new PolylineOptions();
                    positionTrack = new ArrayList<>();

                    try {
                        geocoder = new Geocoder(getApplicationContext());
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        str = addressList.get(0).getLocality() + " " + addressList.get(0).getCountryName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title(str)).setSnippet("Start Location");
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
                    trackingInProgress = false;
                }
                //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(str)).setSnippet("Start Location");
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
                polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
                mMap.addPolyline(polylineOptions);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (!trackingStarted) {
                    Log.d("DEMO","Started");
                    Toast.makeText(MapsActivity.this, "Started Location Tracking", Toast.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    } else {


                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
                        if(currLocation!=null) {
                            LatLng startLoc = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
                            try {
                                geocoder = new Geocoder(getApplicationContext());
                                List<Address> addressList = geocoder.getFromLocation(startLoc.latitude, startLoc.longitude, 1);
                                str = addressList.get(0).getLocality() + " " + addressList.get(0).getCountryName();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mMap.addMarker(new MarkerOptions().position(startLoc).title(str)).setSnippet("Start Location");
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 15));
                            //setBoundaries();

                        }
                        trackingStarted = true;
                    }
                }
                else{
                    Log.d("DEMO","Not started");
                    trackingStarted=false;
                    trackingInProgress=true;
                    locationManager.removeUpdates(locationListener);
                    Toast.makeText(MapsActivity.this, "Stopped Location Tracking", Toast.LENGTH_LONG).show();
                    if(currLocation!=null) {
                        LatLng endLoc = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
                        try {
                            geocoder = new Geocoder(getApplicationContext());
                            List<Address> addressList = geocoder.getFromLocation(endLoc.latitude, endLoc.longitude, 1);
                            str = addressList.get(0).getLocality() + " " + addressList.get(0).getCountryName();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMap.addMarker(new MarkerOptions().position(endLoc).title(str)).setSnippet("End Location");
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                        setBoundaries();

                    }
                }
            }
        });

    }



    private void setBoundaries(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()));
        builder.include(new LatLng(currLocation.getLatitude(), currLocation.getLongitude()));

        int zoomWidth = getResources().getDisplayMetrics().widthPixels;
        int zoomHeight = getResources().getDisplayMetrics().heightPixels;
        int zoomPadding = (int) (zoomWidth * 0.10);

        //positionTrack

        LatLngBounds bounds = builder.build();

        LatLngBounds currentBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        if(!currentBounds.contains(new LatLng(currLocation.getLatitude(), currLocation.getLongitude())))
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,zoomWidth,zoomHeight,zoomPadding));

    }


}
/*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted

                } else {
                    // permission was denied
                }
                return;
            }
        }
    }*/