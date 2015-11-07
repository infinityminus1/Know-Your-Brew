package com.kurtalang.knowyourbrew.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kurtalang.knowyourbrew.R;
import com.kurtalang.knowyourbrew.model.PlaceItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by kurtalang on 11/5/15.
 */
public class MapListAdapter extends RecyclerView.Adapter<MapListAdapter.ViewHolder> {

    List<PlaceItem> data = Collections.emptyList();
    Context mContext;
    private LayoutInflater inflater;

    // 2
    public MapListAdapter(Context context, List<PlaceItem> data) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void addList(List<PlaceItem> data){
        this.data = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.place_list_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceItem current = data.get(position);
        holder.placeName.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // 3
    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout placeHolder;
        public TextView placeAddress;
        public TextView placeName;


        public ViewHolder(View itemView) {
            super(itemView);

            placeName = (TextView) itemView.findViewById(R.id.place_name);
            placeAddress = (TextView) itemView.findViewById(R.id.place_address);
            placeHolder = (RelativeLayout) itemView.findViewById(R.id.placeHolder);

        }
    }

}
