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

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    String googlePlacesData = null;
    GoogleMap googleMap;
    List<HashMap<String, String>> googlePlacesList = null; //Todo: make this a class or something maplist can access
    public ReadResponse delegate = null;

    public GooglePlacesReadTask(){

    }

    public GooglePlacesReadTask(ReadResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {
        try {

            googleMap = (GoogleMap) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];

            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);

            Places placeJsonParser = new Places();
            googlePlacesJson = new JSONObject((String) googlePlacesData);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);

        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        //access mapList by setters?

        delegate.processFinish(list, googleMap); //set markers in Map fragment

        /*googleMap.clear();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = list.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                //BitmapDescriptor icon = googlePlace.get("icon");
                String placeName = googlePlace.get("place_name");
                String PlaceID = googlePlace.get("place_id");
                String vicinity = googlePlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(PlaceID + placeName + " : " + vicinity);
                googleMap.addMarker(markerOptions);
            }
        }*/
    }
}