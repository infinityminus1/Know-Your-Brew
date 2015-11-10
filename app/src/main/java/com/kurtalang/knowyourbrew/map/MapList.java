package com.kurtalang.knowyourbrew.map;


import android.os.Bundle;
import android.app.Fragment;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MapList extends Fragment {

    CardView mCardView;   //The CardView widget
    MapListAdapter adapter;
    RecyclerView recyclerView;

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

        adapter = new MapListAdapter(this.getActivity(), initializeData()); //TODO: write get data func
        recyclerView.setAdapter(adapter);

        return rootview;
    }

    public void updateAdapter(List<HashMap<String, String>> placeList){

        List<PlaceItem> list = new ArrayList<>();
        //update data in adapter with placeList
        for (int i = 0; i < placeList.size(); i++) {
            HashMap<String, String> googlePlace = placeList.get(i);
            PlaceItem placeItem = new PlaceItem();
            placeItem.setName(googlePlace.get("place_name"));
            placeItem.setVicinity(googlePlace.get("vicinity"));

            System.out.println("******FROM UPDATE ADAPTER" + placeItem);
            list.add(placeItem);
        }
        adapter.addList(list);
        adapter.notifyDataSetChanged();
    }
    private List<PlaceItem> initializeData() {
        List<PlaceItem> placeList = new ArrayList<>();

        PlaceItem first = new PlaceItem();
        first.setName("first name");
        first.setVicinity("First Vicinity");

        placeList.add(first);

        return placeList;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview);
    }
}
