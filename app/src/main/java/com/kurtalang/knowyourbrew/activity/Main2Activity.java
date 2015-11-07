package com.kurtalang.knowyourbrew.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kurtalang.knowyourbrew.R;
import com.kurtalang.knowyourbrew.map.GooglePlacesReadTask;
import com.kurtalang.knowyourbrew.map.Map;
import com.kurtalang.knowyourbrew.map.MapList;
import com.kurtalang.knowyourbrew.map.OnTaskCompleted;
import com.kurtalang.knowyourbrew.map.ReadResponse;

public class Main2Activity extends AppCompatActivity
        implements ReadResponse, LocationListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    List<HashMap<String, String>> googlePlacesList = null;
    GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();

    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    private static final String GOOGLE_API_KEY = "AIzaSyAq9qqBbHYqKUs0EmX6-VQjyAwD97g8uK0";
    public static final String TYPE_CAFE = "cafe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //this to set delegate/listener back to this class
        googlePlacesReadTask.delegate = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        findCafesJson();


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Map(), "Map View");
        adapter.addFragment(new MapList(), "List View");
        viewPager.setAdapter(adapter);
    }



    private void findCafesJson() {

        setLatAndLong();

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + TYPE_CAFE);
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);


        //googlePlacesReadTask = new GooglePlacesReadTask(googlePlacesReadTask.delegate);
        Object[] toPass = new Object[2];
        Map map = (Map) getSupportFragmentManager().findFragmentById(R.id.map);
        toPass[0] = map;
        toPass[1] = googlePlacesUrl.toString();

        googlePlacesReadTask.execute(toPass);
    }

    /* Returned value when AsyncTask is done. */
    @Override
    public void processFinish(List<HashMap<String, String>> output) {

        for(int i = 0;  i < output.size(); i++){
            System.out.println(output.get(i));
        }

        Map map = (Map) getSupportFragmentManager().findFragmentById(R.id.map);
        map.setMarkers(output);
        MapList mapList = (MapList) getSupportFragmentManager().findFragmentById(R.id.map_list);
        if (mapList == null){
            System.out.println("HEEEEEEERRRR**********");
        }
        this.googlePlacesList = output;
        mapList.setAdapterData();

    }

    //Function to return googlePlaceList to fragments.
    public List<HashMap<String, String>> getGooglePlacesList(){
        return this.googlePlacesList;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

       if(id == android.R.id.home) {
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setLatAndLong() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        //Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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
}



