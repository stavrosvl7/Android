package com.unipi.stavrosvl7.exercise_p14015;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private ArrayList<GpsData> dataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView textView_longitude;
        private TextView textView_latitude;
        private TextView textView_speed;
        private TextView textView_time;
        public MyViewHolder(View v) {
            super(v);
            textView_longitude = v.findViewById(R.id.longitude);
            textView_latitude = v.findViewById(R.id.latitude);
            textView_speed = v.findViewById(R.id.speed);
            textView_time = v.findViewById(R.id.time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<GpsData> myDataset) {
        dataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView_longitude.setText(dataset.get(position).dataLongitude);
        holder.textView_latitude.setText(dataset.get(position).dataLatitude);
        holder.textView_speed.setText(dataset.get(position).dataSpeed);
        holder.textView_time.setText(dataset.get(position).dataTime);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
    }
