package com.example.indhan;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.indhan.login.latitude;
import static com.example.indhan.login.longitude;

public class PlannerFragment extends Fragment {
    EditText fromEditText;
    EditText destinationEditText;
    Button planTripButton;
    double Price = 76.46;
    TextView resultTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_travel, container, false);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    StringRequest getDistanceRequest(final LatLng fromLatLng, final LatLng destinationLatLng) {
        String serverURL = login.BASE_URL + "/plan_trip";
        return new StringRequest(Request.Method.POST, serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // Request Code
//                            textView.setText("Response is: "+ response.substring(0,500));
                        Log.i("trip_response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            double average_speed = object.getDouble("average_speed");
//                            hours = 3;
                            double petrolNeeded = object.getDouble("petrol");
                            double time = object.getDouble("time");
                            double distance = object.getDouble("distance");

                            double petrol_price = 73.73;



                            if (petrolNeeded > HomeFragment.volumeReading) {
                                double cost = petrolNeeded*(petrol_price-HomeFragment.volumeReading);
                                resultTextView.setText("Warning your fuel level does not seem enough for the journey."
                                 + "\n" + "Petrol Needed: " + petrolNeeded + "L"
                                + "\n" + "Current Level: " + HomeFragment.volumeReading + "L"
                                        + "\n" + "Fuel Worth: " + cost + " Rs.");
                            }

                            else {
                                resultTextView.setText("You are good to go. Your Travel Details: " + "\n"
                                        + "\n" + "Petrol Needed: " + petrolNeeded + "L"
                                        + "\n" + "Current Level: " + HomeFragment.volumeReading + "L");
                            }

                            resultTextView.append("\n Please Click the message to open navigation");

                            resultTextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String uri = "http://maps.google.com/maps?saddr="+fromLatLng.latitude + "," + fromLatLng.longitude
                                            + "+" + "&daddr="+destinationLatLng.latitude + "," + destinationLatLng.longitude;
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                    startActivity(intent);
                                }
                            });
                            resultTextView.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "That didn't work!" + error, Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", String.valueOf(login.authKey));
                params.put("lat1", String.valueOf(fromLatLng.latitude));
                params.put("lon1", String.valueOf(fromLatLng.longitude));
                params.put("lat2", String.valueOf(destinationLatLng.latitude));
                params.put("lon2", String.valueOf(destinationLatLng.longitude));
                //Add the data you'd like to send to the server.
                return params;
            }
        };


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        planTripButton = getActivity().findViewById(R.id.planTripButton);
        fromEditText = getActivity().findViewById(R.id.fromEditText);
        destinationEditText = getActivity().findViewById(R.id.destinationEditText);
        resultTextView = getActivity().findViewById(R.id.resultTextView);


        planTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromAddress = fromEditText.getText().toString();
                String destinationAddress = destinationEditText.getText().toString();
                if (destinationEditText.length() == 0 || fromEditText.length() == 0) {
                    Toast.makeText(getContext(), "Please Enter all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    LatLng fromLatLng = getLocationFromAddress(getContext(), fromAddress);
                    LatLng destinationLatLng = getLocationFromAddress(getContext(), destinationAddress);
                    StringRequest travelPlanRequest = getDistanceRequest(fromLatLng, destinationLatLng);
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(travelPlanRequest);

                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
