package com.example.indhan;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.indhan.PumpContentClass;
import com.example.indhan.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<PumpContentClass> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewGroup parent;
        public MyViewHolder(View v, ViewGroup parent) {
            super(v);
            view = v;
            this.parent = parent;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<PumpContentClass> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pump_content, parent, false);

        MyViewHolder vh = new MyViewHolder(v, parent);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        TextViewholder.view.setText(mDataset[position]);

        PumpContentClass pump = mDataset.get(position);
        TextView pumpName = holder.view.findViewById(R.id.pumpName);
        TextView behaviorRatingTextView = holder.view.findViewById(R.id.behaviorRatingTextView); // Change the textView here
        TextView googleRatingTextView = holder.view.findViewById(R.id.googleRatingTextView);
        TextView sanityRatingTextView = holder.view.findViewById(R.id.sanityRatingTextView);
        TextView restaurantRatingTextView = holder.view.findViewById(R.id.restaurantRatingTextView);
        TextView airServiceTextView = holder.view.findViewById(R.id.airServiceTextView);
        TextView paymentServiceTextView = holder.view.findViewById(R.id.paymentServiceTextView);
        TextView washroomServiceTextView = holder.view.findViewById(R.id.washroomServiceTextView);
        TextView restaurantServiceTextView = holder.view.findViewById(R.id.restaurantServiceTextView);
        TextView navigateTextView = holder.view.findViewById(R.id.navigateTextView);
        Uri gmmIntentUri1 =
                Uri.parse("google.navigation:q=" + pump.latitude + "," + pump.longitude);
        final Intent mapIntent1 = new Intent(Intent.ACTION_VIEW, gmmIntentUri1);
        mapIntent1.setPackage("com.google.android.apps.maps");
        navigateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.parent.getContext().startActivity(mapIntent1);
            }
        });

        pumpName.setText(pump.getPumpName());
        behaviorRatingTextView.setText(pump.getBehaviour());
        googleRatingTextView.setText(pump.getGoogleRating());
        sanityRatingTextView.setText(pump.getSanity());
        restaurantRatingTextView.setText(pump.getrestaurantRating());
        airServiceTextView.setText(String.valueOf(pump.isAir()));
        paymentServiceTextView.setText(String.valueOf(pump.isCashless()));
        washroomServiceTextView.setText(String.valueOf(pump.isWashroom()));
        restaurantServiceTextView.setText(String.valueOf(pump.isRestaurant()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}