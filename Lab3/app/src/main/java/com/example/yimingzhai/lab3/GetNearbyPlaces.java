package com.example.yimingzhai.lab3;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {

    private String googlePlaceData;
    private String url;
    private GoogleMap mMap;


    @Override
    protected String doInBackground(Object... objects) {

        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlaceData = downloadUrl.readTheUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlaceData;
    }


    @Override
    protected void onPostExecute(String s) {

        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(s);

        displayNearbyPlaces(nearbyPlacesList);

    }


    private void displayNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        for (int i = 0; i< nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googleNearbyPlace = nearbyPlacesList.get(i);
            String name = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            Double lat = Double.parseDouble(googleNearbyPlace.get("latitude"));
            Double lng = Double.parseDouble(googleNearbyPlace.get("longitude"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(name + " " + vicinity);
            // set marker color
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mMap.addMarker(markerOptions);

            // move camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        }

    }
}
