package com.kurtalang.knowyourbrew.map;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.SeekBar;

import com.kurtalang.knowyourbrew.R;
import com.kurtalang.knowyourbrew.activity.Main2Activity;
import com.kurtalang.knowyourbrew.adapter.MapListAdapter;
import com.kurtalang.knowyourbrew.model.PlaceItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapList extends Fragment {

    CardView mCardView;   //The CardView widget
    MapListAdapter adapter;
    RecyclerView recyclerView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    /*public static MapList newInstance() {
        MapList fragment = new MapList();
        fragment.setRetainInstance(true);
        return fragment;
    }*/



    public MapList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_map_list, container, false);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        //RecyclerView needs a LayoutManager to manage the positioning of its items.
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //Recycler view created, now what for main2's asynctask to return data before call to getData()
        //adapter = new MapListAdapter(this.getActivity(), getData()); //TODO: write get data func
        //recyclerView.setAdapter(adapter);

        return rootview;
    }


    public List<PlaceItem> getData() {
        List<PlaceItem> data = new ArrayList<>();
        List<HashMap<String, String>> googlePlaceList;

        googlePlaceList = ((Main2Activity)this.getActivity()).getGooglePlacesList();

        //TODO: googlePlaceList might be null bc of AsyncTask not finishing...


        // Parse through googlePlaceList and prepare data
        for (int i = 0; i < googlePlaceList.size(); i++) {
            HashMap<String, String> googlePlace = googlePlaceList.get(i);

            PlaceItem placeItem = new PlaceItem();

            placeItem.setName(googlePlace.get("place_name"));
            //placeItem.setOpen(Boolean.parseBoolean(googlePlace.get("open_now")));
            //placeItem.setPrice_level(Integer.parseInt(googlePlace.get("price_level")));
            //placeItem.setRating(Float.parseFloat(googlePlace.get("rating")));
            placeItem.setId(googlePlace.get("place_id"));
            placeItem.setVicinity(googlePlace.get("vicinity"));
            placeItem.setLatitude(Double.parseDouble(googlePlace.get("lat")));
            placeItem.setLongitude(Double.parseDouble(googlePlace.get("lng")));

            data.add(placeItem);
        }
        return data;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview);



    }

    public void setAdapterData() {
        //Recycler view created, now wait for main2's asynctask to return data before call to getData()
        adapter = new MapListAdapter(this.getActivity(), getData()); //TODO: write get data func
        recyclerView.setAdapter(adapter);
    }
}
