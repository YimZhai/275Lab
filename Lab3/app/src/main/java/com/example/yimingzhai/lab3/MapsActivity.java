package com.example.yimingzhai.lab3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    View mMapView;

    // vars
    private static final String TAG = "MapsActivity";
    private static final float DEFAULT_ZOOM = 15f;
    private static final int USER_REQUEST_LOCATION_CODE = 99;
    private double lat, lng;
    private int proximityRadius = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void onClick(View v) {

        String coffee = "coffee";
        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        switch (v.getId()) {
            case R.id.ic_search:
                EditText addressField = (EditText) findViewById(R.id.location_search);
                String address = addressField.getText().toString();

                List<Address> addressList = new LinkedList<>();
                MarkerOptions markerOptions = new MarkerOptions();
                if (!TextUtils.isEmpty(address)) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(address, 6);

                        if (addressList.size() > 0) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());
                                lat = userAddress.getLatitude();
                                lng = userAddress.getLongitude();

                                markerOptions.position(latLng);
                                markerOptions.title(address);
                                // set marker color
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                mMap.addMarker(markerOptions);

                                // move camera
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                            }
                        } else {
                            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Location can't be empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.coffee_nearby:
                mMap.clear();
                String url = getUrl(lat, lng, coffee);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                /*
                Toast.makeText(this, "searching for nearby coffee", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "showing nearby coffee", Toast.LENGTH_SHORT).show();
                */
                break;
        }
    }


    private String getUrl(double latitude, double longitude, String coffee) {

        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=" + latitude + "," + longitude);
        googleUrl.append("&radius=" + proximityRadius);
        googleUrl.append("&type=" + coffee);
        googleUrl.append("&sensor=true");
        googleUrl.append("&key=AIzaSyCP-NqdgarQLPSihmaBIvrQ_M4n4I03QBg");

        Log.d(TAG, "url = " + googleUrl.toString());

        return googleUrl.toString();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }

    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, USER_REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, USER_REQUEST_LOCATION_CODE);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case USER_REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient: I'm here");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.d(TAG, "onLocationChanged");
        lastLocation = location;

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My location");
        // set marker color
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        // move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        Log.d(TAG, "onConnected");


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
