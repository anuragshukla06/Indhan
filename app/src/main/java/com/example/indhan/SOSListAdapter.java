package com.example.indhan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.indhan.PumpContentClass;
import com.example.indhan.R;

import java.util.ArrayList;

public class SOSListAdapter extends RecyclerView.Adapter<SOSListAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;

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
    public SOSListAdapter(ArrayList<String> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SOSListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sos_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v, parent);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        TextViewholder.view.setText(mDataset[position]);

        final String phoneNumber = mDataset.get(position);
        TextView phoneTextView = holder.view.findViewById(R.id.phoneTextView);
        ImageView removeItemImageView = holder.view.findViewById(R.id.removeItemImageView);
        ImageView sosImageView = holder.view.findViewById(R.id.sosImageView);


        removeItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SOS_activity.sos_contacts.remove(position);
                SOS_activity.adapter.notifyDataSetChanged();
            }
        });

        sosImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (holder.parent.getContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                holder.parent.getContext().startActivity(intent);
            }
        });

        phoneTextView.setText(phoneNumber);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}