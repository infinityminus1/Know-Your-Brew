package com.kurtalang.knowyourbrew.map;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.kurtalang.knowyourbrew.R;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.kurtalang.knowyourbrew.activity.Main2Activity;

import java.util.HashMap;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class Map extends Fragment
        implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private MapFragment mMapFragment;
    protected GoogleApiClient mGoogleApiClient; //Provides the entry point to Google Play services.
    protected Location mLastLocation; //Represents a geographical location.
    GoogleMap mMap; // Might be null if Google Play services APK is not available
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    private static final String GOOGLE_API_KEY = "AIzaSyAq9qqBbHYqKUs0EmX6-VQjyAwD97g8uK0";
    public static final String TYPE_CAFE = "cafe";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //show error dialog if GooglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        Button btnFind = (Button) rootView.findViewById(R.id.btnFind);

        mMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        /*mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_frag);
        if (mMapFragment == null) {
            System.out.println("***********MappFragg NULLLLLL!!!!");
        }
        mMapFragment.getMapAsync(this);


        FragmentManager fm = getChildFragmentManager();
        mMapFragment =  SupportMapFragment.newInstance();
        mMapFragment.getMapAsync(this);
        fm.beginTransaction().replace(R.id.map, mMapFragment).commit();
        */

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String type = placeText.getText().toString();
                findCafesJson();
                //TODO: might not need this button or switch to "Search Here" if map is not centered at user location
            }
        });

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();

        return rootView;

    }

    private void findCafesJson() {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + TYPE_CAFE);
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);


        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        System.out.println("**********OnMapReadu!!!!!!!!!!!!!!!!!!");
        System.out.println("**********OnMapReadu!!!!!!!!!!!!!!!!!!");
        System.out.println("**********OnMapReadu!!!!!!!!!!!!!!!!!!");
        System.out.println("**********OnMapReadu!!!!!!!!!!!!!!!!!!");


        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(32.7150, -117.1625)) //San Diego
                .title("San Diego"));


        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }

        findCafesJson();
        //setMarkers(((Main2Activity)this.getActivity()).getGooglePlacesList(), mMap); //get data(is data ready?) from main2 and code from PlacesDisplayTask....

    }

//    /* Returned value when AsyncTask is done. */
//    @Override
//    public void processFinish(List<HashMap<String, String>> output) {
//        setMarkers(output);
//    }


    //called from main2 after asyncTask is done.
    public void setMarkers(List<HashMap<String, String>> list, GoogleMap googleMap){

        /*List<HashMap<String, String>> list = ((Main2Activity)this.getActivity()).getGooglePlacesList();*/

        this.mMap.clear();
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            System.out.println("HERE!!!!!!: " + googlePlace);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            //BitmapDescriptor icon = googlePlace.get("icon");
            String placeName = googlePlace.get("place_name");
            String PlaceID = googlePlace.get("place_id");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(PlaceID + placeName + " : " + vicinity);
            this.mMap.addMarker(markerOptions);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
//
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//
//        LatLng latLng = new LatLng(latitude, longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), 0).show();
            return false;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
