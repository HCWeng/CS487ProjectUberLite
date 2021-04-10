package com.example.uberlite_cs487_project;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity<GPS_PROVIDER> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;;
    private double myLatitude, myLongitude;
    private String myLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void placeMarkerOnPosition() {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationManager locationManager = (LocationManager)

        getSystemService(Context.LOCATION_SERVICE);


        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria,true);
        Location location=locationManager.getLastKnownLocation(bestProvider);

           //////////   load your location from last recent locations ////////
        if (location != null) {
            Geocoder geocoder = new Geocoder(getApplicationContext());

            try {
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                LatLng myPosition = new LatLng(myLatitude, myLongitude);
                List<Address> places =
                        geocoder.getFromLocation(myLatitude, myLongitude, 1);
                myLocation = places.get(0).getSubLocality() + " " + places.get(0).getLocality();

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(myPosition).title(myLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 40.5f));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker at IIT and move the camera
        while(true){
            placeMarkerOnPosition();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}