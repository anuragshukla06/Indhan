package com.example.indhan;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.indhan.login.latitude;
import static com.example.indhan.login.longitude;


/**
 * A simple {@link Fragment} subclass./
 * Activities that contain this fragment must implement the
 * {@link NearbyPumps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearbyPumps#newInstance} factory method to
 * create an instance of this fragment.
 */


public class NearbyPumps extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView pumpRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton filterFloatButton;
    LinearLayout filterLinearLayout;
    CardView washroomCardView;
    CardView airCardView;
    CardView cashlessCardView;
    CardView restaurantCardView;
    boolean clickedArray[];
    ArrayList<PumpContentClass> myDataset;

//    StringRequest getNearbyPumpsRequest() {
//        String serverURL = login.BASE_URL + "/petrol_pump_ratings_recommendation";
//        return new StringRequest(Request.Method.POST, serverURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        ArrayList<PumpContentClass> myDataset = new ArrayList<>();
////                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
//                        try {
//                            JSONObject responseObject = new JSONObject(response);
//
//                            for (int i = 0; i < responseObject.length(); i++) {
//                                JSONObject object = responseObject.getJSONObject(Integer.toString(i));
//                                String pumpName = object.getString("name");
//                                String googleRating = object.getString("rating");
//                                String indhanRating = object.getString("app_ratings");
//                                JSONObject locationObject = object.getJSONObject("location");
//                                double latitude = locationObject.getDouble("lat");
//                                double longitude = locationObject.getDouble("lng");
//
//                                Random rand = new Random();
//
//                                myDataset.add(new PumpContentClass(pumpName,
//                                        googleRating, indhanRating, latitude, longitude,
//                                        String.valueOf(rand.nextInt(5)),
//                                        String.valueOf(rand.nextInt(5)),
//                                        rand.nextBoolean(),
//                                        rand.nextBoolean(),
//                                        rand.nextBoolean()));
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext(), "Server Error.", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        MyAdapter mAdapter = new MyAdapter(myDataset);
//                        pumpRecyclerView.setAdapter(mAdapter);
//
////                            textView.setText("Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), "That didn't work!" + error, Toast.LENGTH_LONG).show();
//            }
//        }) {
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("lat", String.valueOf(latitude));
//                params.put("lon", String.valueOf(longitude));
//                //Add the data you'd like to send to the server.
//                return params;
//            }
//        };
//
//
//    }

    void fillRandomPumpData(int n) {

        myDataset = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            myDataset.add(new PumpContentClass("ABC" + rand.nextInt(),
                    rand.nextInt(5) + "", rand.nextInt(5) + "", rand.nextFloat(), rand.nextFloat(),
                    String.valueOf(rand.nextInt(5)),
                    String.valueOf(rand.nextInt(5)),
                    rand.nextBoolean(),
                    rand.nextBoolean(),
                    rand.nextBoolean()));
        }
        MyAdapter mAdapter = new MyAdapter(myDataset);
        pumpRecyclerView.setAdapter(mAdapter);
    }

    void filterDataService() {
        ArrayList<PumpContentClass> filterArrayList = new ArrayList<>();
        if (!(clickedArray[0] || clickedArray[1] || clickedArray[2] || clickedArray[3])) {
            MyAdapter adapter = new MyAdapter(myDataset);
            pumpRecyclerView.setAdapter(adapter);
            return;
        }

        int flag = 0;
        for(boolean b: clickedArray) {
            if (b) {flag++;}
        }

        for (PumpContentClass p: myDataset) {
            int mScore = 0;
            if (clickedArray[0] && p.isWashroom()) {
                mScore++;
            }
            if (clickedArray[1] && p.isAir()) {
                mScore++;
            }
            if (clickedArray[2] && p.isCashless()) {
                mScore++;
            }
            if (clickedArray[3] && p.isRestaurant()) {
                mScore++;
            }

            if (mScore == flag){

                filterArrayList.add(p);
            }

        }
        MyAdapter adapter = new MyAdapter(filterArrayList);
        pumpRecyclerView.setAdapter(adapter);
        return;

    }

    void changeColour(View view, int id) {

        if (clickedArray[id]) {
            clickedArray[id] = false;
            view.setBackgroundColor(getResources().getColor(R.color.white));
            filterDataService();
        } else {


            clickedArray[id] = true;
            view.setBackgroundColor(getResources().getColor(R.color.blue));
            filterDataService();
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pumpRecyclerView = getView().findViewById(R.id.pumpRecyclerView);
        filterFloatButton = getView().findViewById(R.id.filterFloatButton);
        filterLinearLayout = getView().findViewById(R.id.filterLinearLayout);
        washroomCardView = getView().findViewById(R.id.washroomCardView);
        airCardView = getView().findViewById(R.id.airCardView);
        cashlessCardView = getView().findViewById(R.id.cashlessCardView);
        restaurantCardView = getView().findViewById(R.id.restaurantCardView);
        clickedArray = new boolean[4];


        washroomCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColour(view, 0);
            }
        });
        airCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColour(view, 1);
            }
        });
        cashlessCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColour(view, 2);
            }
        });
        restaurantCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColour(view, 3);
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        pumpRecyclerView.setLayoutManager(layoutManager);
        RequestQueue queue = Volley.newRequestQueue(getContext());


//        StringRequest nearbyPumpsRequest = getNearbyPumpsRequest();

        fillRandomPumpData(15);

        filterFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterLinearLayout.getVisibility() == View.VISIBLE) filterLinearLayout.setVisibility(View.GONE);
                else filterLinearLayout.setVisibility(View.VISIBLE);
            }
        });


//        queue.add(nearbyPumpsRequest);


    }

    private OnFragmentInteractionListener mListener;

    public NearbyPumps() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyPumps.
     */
    // TODO: Rename and change types and number of parameters
    public static NearbyPumps newInstance(String param1, String param2) {
        NearbyPumps fragment = new NearbyPumps();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_nearby_pumps, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
