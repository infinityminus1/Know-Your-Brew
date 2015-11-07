package com.kurtalang.knowyourbrew.map;

/**
 * Created by kurtalang on 11/2/15.
 */
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PlacesDisplayListTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }
    /**
     * TODO: Change this to populate the list.
     * TODO: Use MD Cards bc youll have a nav button, rating, descrip, and pic.
     *       looks like
     *       https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY
     */
    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        if(googleMap != null) {
            googleMap.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googlePlace = list.get(i);

            System.out.println("HERE!!!!!!: " + googlePlace);

            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String PlaceID = googlePlace.get("place_id");
            String vicinity = googlePlace.get("vicinity");

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(PlaceID + placeName + " : " + vicinity);
            googleMap.addMarker(markerOptions);
        }
    }
}