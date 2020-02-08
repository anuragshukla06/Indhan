package com.example.indhan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.indhan.login.latitude;
import static com.example.indhan.login.longitude;


public class HomeFragment extends Fragment {
    static  double volumeReading, before, after, diff, distance, mileage;
    static String hieghtReading;
    public String rpiURL = "http://192.168.137.232:8080/";
    static RequestQueue queue;
    static JSONArray dist, mile, fuelCom;
    GraphView mileageGraph, distanceGraph, fuelGraph;
    TextView fuelView, distView, mileageView, userView, groupMileageView;
    RelativeLayout back;
    SharedPreferences sharedPref;
    Button logoutButton;
    Button sos_button;
    String authKey;
    boolean bathroom, food, cashless, air;
    CheckBox check1, check2, check3, check4;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder DangerNotiBuilder;

    void ServerGraphRequest(){
        String serverUrl = login.BASE_URL + "/index";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            dist = jsonObject.getJSONArray("distance");
                            for (int i = 0; i < dist.length(); i++) {
                                Log.d("dist", dist.getString(i));
                            }
                            mile = jsonObject.getJSONArray("mileage");
                            for (int i = 0; i < mile.length(); i++) {
                                Log.d("MILE", mile.getString(i));
                            }
                            fuelCom = jsonObject.getJSONArray("fuel");
                            for (int i = 0; i < fuelCom.length(); i++) {
                                Log.d("Fuel", fuelCom.getString(i));
                            }


                            DataPoint[] dataPoints = new DataPoint[mile.length()];
                            for (int i = 0; i < mile.length(); i++) {
                                try {
                                    dataPoints[i] = new DataPoint(i + 1, mile.getDouble(i));
                                }catch (JSONException e){}
                            }
                            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
                            mileageGraph = getView().findViewById(R.id.MileageGraph);
//                            mileageGraph.getViewport().setScrollable(true);
//                            mileageGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//                            mileageGraph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
                            mileageGraph.addSeries(series);
                            series.setTitle("Mileage");
                            series.setColor(Color.MAGENTA);
                            series.setDrawDataPoints(true);
                            series.setDataPointsRadius(10);
                            series.setThickness(8);
                            mileageGraph.getLegendRenderer().setVisible(true);
                            mileageGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);



                            DataPoint[] dataPoints2 = new DataPoint[dist.length()]; // declare an array of DataPoint objects with the same size as your list
                            for (int i = 0; i < dist.length(); i++) {
                                // add new DataPoint object to the array for each of your list entries
                                try {
                                    dataPoints2[i] = new DataPoint(i + 1, dist.getDouble(i));
                                }catch (JSONException e){}// not sure but I think the second argument should be of type double
                            }
                            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPoints2);
                            distanceGraph = getView().findViewById(R.id.DistanceGraph);
//                            distanceGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//                            distanceGraph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
                            distanceGraph.addSeries(series2);
                            series2.setTitle("Distance");
                            series2.setColor(Color.RED);
                            series2.setDrawDataPoints(true);
                            series2.setDataPointsRadius(10);
                            series2.setThickness(8);
                            distanceGraph.getLegendRenderer().setVisible(true);
                            distanceGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);



                            DataPoint[] dataPoints3 = new DataPoint[fuelCom.length()]; // declare an array of DataPoint objects with the same size as your list
                            for (int i = 0; i < fuelCom.length(); i++) {
                                // add new DataPoint object to the array for each of your list entries
                                try {
                                    dataPoints3[i] = new DataPoint(i + 1, fuelCom.getDouble(i));
                                }catch (JSONException e){}// not sure but I think the second argument should be of type double
                            }
                            LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(dataPoints3);
                            fuelGraph = getView().findViewById(R.id.FuelGraph);
//                            fuelGraph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//                            fuelGraph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
                            fuelGraph.addSeries(series3);
                            series3.setTitle("Fuel");
                            series3.setColor(Color.BLACK);
                            series3.setDrawDataPoints(true);
                            series3.setDataPointsRadius(10);
                            series3.setThickness(8);
                            fuelGraph.getLegendRenderer().setVisible(true);
                            fuelGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                            series3.setOnDataPointTapListener(new OnDataPointTapListener() {
                                @Override
                                public void onTap(Series series, DataPointInterface dataPoint) {
                                    Toast.makeText(getActivity(), "Data Point Clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
                                }
                            });



//                            Log.d("GRAPH"+response, response);
//                            Toast.makeText(getContext(), response+"", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(getActivity(), "Current Volume" + volumeReading, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Some error occured!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", "onErrorResponse: " + error);
                Toast.makeText(getActivity(), "DATAPOINTS Can't be fetched!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authKey);
                params.put("days", "7");
                //Add the data you'd like to send to the server.
                return params;
            }
        };
        queue.add(stringRequest);
    }

    void ServerRequest(){

        String serverUrl = login.BASE_URL + "/current_stats";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            distance = jsonObject.getDouble("distance");
                            mileage = jsonObject.getDouble("mileage");
                            Log.d(" "+distance+" DIS", mileage+" MIL");
                            Log.d(response, response);
//                            Toast.makeText(getContext(), response+"", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(getActivity(), "Current Volume" + volumeReading, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
//                            Toast.makeText(getActivity(), "Some error occured!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", "onErrorResponse: " + error);
//                Toast.makeText(getActivity(), "Distance Can't be fetched!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authKey);
                //Add the data you'd like to send to the server.
                return params;
            }
        };
        queue.add(stringRequest);
    }

    double APIRequest() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, rpiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            hieghtReading = jsonObject.getString("height");
                            volumeReading = jsonObject.getDouble("volume");
                            Log.d(response, "onResponse: " + volumeReading);
//                                    Toast.makeText(getContext(), "Current Volume" + volumeReading, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Some error occured!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", "onErrorResponse: " + error);
//                Toast.makeText(getActivity(), "RASP didn't work!" + error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
        return volumeReading;
    }




    public static class PetrolPumpReview extends  DialogFragment{
        Double diff;
        TextView nameView, fraudView;
        LinearLayout foodRatingBarLayout;
        String name = "DUMMY";
        RatingBar foodRatingBar, sanitationRatingBar, paymentRatingBar;
        PetrolPumpReview(JSONObject json, Double diff) {
            try {
                name = json.getString("name");
                this.diff = diff;
            }catch (JSONException e){
                Toast.makeText(getContext(), "ERROR PARSING FOR REVIEW", Toast.LENGTH_LONG).show();
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.review_layout, null);
            builder.setView(view);

            fraudView = view.findViewById(R.id.fraud);
            fraudView.setText("Did you refill with 2.3 Litres?");
            foodRatingBarLayout = view.findViewById(R.id.foodRatingBar);
            foodRatingBar = view.findViewById(R.id.RatingFood);
            sanitationRatingBar = view.findViewById(R.id.RatingSanitation);
            paymentRatingBar = view.findViewById(R.id.RatingPayment);

            nameView = view.findViewById(R.id.name);
//            GRating = view.findViewById(R.id.rating);
//            appRating = view.findViewById(R.id.app_rating);
            nameView.setText(name);
//            GRating.setText(ratings+"");
//            appRating.setText(totalUserRatings);

//            userRating = view.findViewById(R.id.your_rating);

            final ArrayList<Boolean> bathroom = new ArrayList<>();
            final ArrayList<Boolean> food = new ArrayList<>();
            final ArrayList<Boolean> air = new ArrayList<>();
            final ArrayList<Boolean> cashless = new ArrayList<>();
            final ArrayList<Boolean> yesList = new ArrayList<>();

            bathroom.add(false);
            food.add(false);
            air.add(false);
            cashless.add(false);
            yesList.add(false);


            CheckBox check1 = view.findViewById(R.id.bathroom);
            CheckBox check2 = view.findViewById(R.id.food);
            CheckBox check3 = view.findViewById(R.id.cashless);
            CheckBox check4 = view.findViewById(R.id.air);
            final CheckBox yes = view.findViewById(R.id.yes);
            CheckBox no = view.findViewById(R.id.no);
            View.OnClickListener OnCheckBoxClicked = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();

                    // Check which checkbox was clicked
                    switch (v.getId()) {
                        case R.id.bathroom:
                            if (checked) {
                                bathroom.add(true);
                                Toast.makeText(getActivity(), "BATHROOM", Toast.LENGTH_SHORT).show();
                            }
                            else
                                bathroom.add(false);
                            break;
                        case R.id.food:
                            if (checked) {
                                food.add(true);
                                foodRatingBarLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "FOOD", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                food.add(false);
                                foodRatingBarLayout.setVisibility(View.GONE);
                            }
                            break;
                        case R.id.cashless:
                            if (checked) {
                                cashless.add(true);
                                Toast.makeText(getActivity(), "CASHLESS", Toast.LENGTH_SHORT).show();
                            }
                            else
                                cashless.add(false);
                            break;
                        case R.id.air:
                            if (checked) {
                                air.add(true);
                                Toast.makeText(getActivity(), "AIR", Toast.LENGTH_SHORT).show();
                            }
                            else
                                air.add(false);
                            break;
                        case R.id.yes:
                            if(checked){
                                yesList.add(false);
                                Toast.makeText(getActivity(), "YES", Toast.LENGTH_SHORT).show();
                            }
                            else
                               yesList.add(true);
                            break;
                        case R.id.no:
                            if(checked){
                                yesList.add(true);
                                Toast.makeText(getActivity(), "NO", Toast.LENGTH_SHORT).show();
                            }
                            else
                                yesList.add(false);
                    }

                }
            };
            check1.setOnClickListener(OnCheckBoxClicked);
            check2.setOnClickListener(OnCheckBoxClicked);
            check4.setOnClickListener(OnCheckBoxClicked);
            check3.setOnClickListener(OnCheckBoxClicked);
            yes.setOnClickListener(OnCheckBoxClicked);
            no.setOnClickListener(OnCheckBoxClicked);

            builder.setTitle("Please provide your review")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String serverURL = login.BASE_URL + "/petrol_pump_ratings_response";
                            final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                                Log.i("response", response);
//                                                Toast.makeText(getActivity(), "REVIEWS SUBMITTED!", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("Error", "onErrorResponse: " + error);
//                                    Toast.makeText(getActivity(), "Couldn't POST" + error, Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() {

                                    Boolean bathroomResponse = bathroom.get(bathroom.size()-1);
                                    Boolean foodResponse = food.get(food.size()-1);
                                    Boolean airResponse = air.get(air.size()-1);
                                    Boolean cashlessResponse = cashless.get(cashless.size()-1);
                                    String foodRating, sanitationRating, paymentRating;
                                    foodRating = String.valueOf(foodRatingBar.getRating());
                                    sanitationRating = String.valueOf(sanitationRatingBar.getRating());
                                    paymentRating = String.valueOf(paymentRatingBar.getRating());
                                    Boolean fraudDetection = yesList.get(yesList.size()-1);

                                    Map<String, String> params = new HashMap<String, String>();

                                    params.put("name", String.valueOf(name));
                                    params.put("bathroom", String.valueOf(bathroomResponse));
                                    params.put("food", String.valueOf(foodResponse));
                                    params.put("air", String.valueOf(airResponse));
                                    params.put("cashless", String.valueOf(cashlessResponse));
                                    params.put("sanitation", sanitationRating);
                                    params.put("payment", paymentRating);
                                    params.put("foodrating", foodRating);
                                    params.put("fraud", String.valueOf(fraudDetection));

                                    return params;
                                }
                            };
                            queue.add(stringRequest);
                        }
                    })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            return builder.create();
        }

    }

    StringRequest getNearbyPumpsRequest() {

//        Toast.makeText(getContext(), "REQUEST ATTEMPT", Toast.LENGTH_SHORT).show();

        String serverURL = login.BASE_URL + "/petrolpump";
        return new StringRequest(Request.Method.POST, serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        try {

                            JSONObject responseObject = new JSONObject(response);
                            JSONObject nearestPump = responseObject.getJSONObject("0");
                            JSONObject nearestPumpLocation = nearestPump.getJSONObject("location");
                            double nearestPumpLong = nearestPumpLocation.getDouble("lng");
                            double nearestPumpLat = nearestPumpLocation.getDouble("lat");



                            Uri gmmIntentUri1 =
                                    Uri.parse("google.navigation:q=" + nearestPumpLat + "," + nearestPumpLong);
                            Intent mapIntent1 = new Intent(Intent.ACTION_VIEW, gmmIntentUri1);
                            mapIntent1.setPackage("com.google.android.apps.maps");

                            Uri gmmIntentUri2 = Uri.parse("geo:0,0?q=petrol pumps");
                            Intent mapIntent2 = new Intent(Intent.ACTION_VIEW, gmmIntentUri2);
                            mapIntent2.setPackage("com.google.android.apps.maps");

                            PendingIntent nearestPendingIntent = PendingIntent.getActivity( getView().getContext(), 0, mapIntent1, 0);
                            PendingIntent nearPendingIntent = PendingIntent.getActivity(getView().getContext(), 0, mapIntent2, 0);

                            DangerNotiBuilder = new NotificationCompat.Builder(getActivity())
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("You are running low on fuel")
                                    .setContentText("Click here to head to the nearest station")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .addAction(R.drawable.ic_launcher_foreground, "Head to nearest pump", nearestPendingIntent)
                                    .addAction(R.drawable.ic_launcher_foreground, "See all pumps", nearPendingIntent);


                            notificationManager.notify(2, DangerNotiBuilder.build());



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                            textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "That didn't work!" + error, Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String authKey = sharedPref.getString("authkey", "");
                params.put("lat", String.valueOf(latitude));
                params.put("lon", String.valueOf(longitude));
                //Add the data you'd like to send to the server.
                return params;
            }
        };


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_graph, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());

        back = getView().findViewById(R.id.background);
        ServerGraphRequest();
        sharedPref = getActivity().getSharedPreferences(
                "mainSP", Context.MODE_PRIVATE);
        distView = getView().findViewById(R.id.distanceText);
        mileageView = getView().findViewById(R.id.mileageText);
        fuelView = getView().findViewById(R.id.fuelText);
        logoutButton = getView().findViewById(R.id.logout_button);
        userView = getView().findViewById(R.id.currentUser);
        sos_button = getView().findViewById(R.id.sos_button);
        notificationManager = NotificationManagerCompat.from(getContext());
        groupMileageView = getView().findViewById(R.id.groupMileage);
        groupMileageView.setText("AVG MILEAGE: "+login.groupmileage);

        bathroom = false;
        food = false;
        cashless = false;
        air = false;

        sos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SOS_activity.class);
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        String serverUrl = login.BASE_URL + "/current_user";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String userName = jsonObject.getString("username");
                            userView.setText("Hello "+userName+"!");
                            Log.d(response, response);
//                            Toast.makeText(getContext(), response+"", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(getActivity(), "Current Volume" + volumeReading, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
//                            Toast.makeText(getActivity(), "Some error occured!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", "onErrorResponse: " + error);
//                Toast.makeText(getActivity(), "Distance Can't be fetched!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authKey);
                //Add the data you'd like to send to the server.
                return params;
            }
        };
        queue.add(stringRequest);

        sharedPref = getActivity().getSharedPreferences(
                "mainSP", Context.MODE_PRIVATE);
        authKey = sharedPref.getString("authkey", "");






//        current_stats

//        PetrolPumpReview review = new PetrolPumpReview();
//        review.show(getFragmentManager(), "review");
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                APIRequest();

                Double Trun = BigDecimal.valueOf(volumeReading)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                fuelView.setText("FUEL: "+Trun+" L");

                int color;
                if(Trun<1) {
                    color = Color.parseColor("#ef9a9a");
                    StringRequest req = getNearbyPumpsRequest();

                    queue.add(req);
                }
                else
                    color = Color.parseColor("#9ccc65");
                back.setBackgroundColor(color);

                ServerRequest();
                Log.d(" "+distance+" DIS IN", mileage+" MIL IN");

                Double Trun1 = BigDecimal.valueOf(distance*0.001)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                distView.setText("DISTANCE:\n "+Trun1+" Kkm");


//                Double Trun2 = BigDecimal.valueOf(mileage);

                Double Trun2 = BigDecimal.valueOf(mileage*0.001)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                mileageView.setText("MILEAGE: "+Trun2+" Km/L");
//


                handler.postDelayed(this, 10000);
            }
        };

        handler.postDelayed(r, 5000);

        final Button refill = getView().findViewById(R.id.refill);
        final ProgressBar bar = getView().findViewById(R.id.progress_bar);
        refill.setTag(1);
        refill.setOnClickListener(new View.OnClickListener() {

            void APIPOSTRequest(final Double diff) {
                String serverURL = login.BASE_URL + "/petrol_pump_ratings";
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    PetrolPumpReview review = new PetrolPumpReview(jsonObject, diff);
                                    review.show(getFragmentManager(), "review");

                                    Toast.makeText(getContext(), response + "", Toast.LENGTH_LONG).show();
//                                    Log.i("response", response + "");
                                }catch (JSONException e){}
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Error", "onErrorResponse: " + error);
                        Toast.makeText(getActivity(), "That didn't work!" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("lat", String.valueOf(latitude));
                        params.put("lon", String.valueOf(longitude));
                        //Add the data you'd like to send to the server.
                        return params;
                    }
                };
                queue.add(stringRequest);
            }

            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();
                if (status == 1) {
                    refill.setText("Done Refilling");
                    bar.setVisibility(View.VISIBLE);
                    before = APIRequest();
                    v.setTag(0); //pause
                } else {
                    refill.setText("Refill");
                    bar.setVisibility(View.GONE);
                    after = APIRequest();
                    diff = after - before;
                    Double diffTrun = BigDecimal.valueOf(diff)
                            .setScale(3, RoundingMode.HALF_UP)
                            .doubleValue();
//                    Toast.makeText(getActivity(), "DIFF"+diffTrun, Toast.LENGTH_LONG).show();
//                    FuelCheckDialog fuelcheck = new FuelCheckDialog(diffTrun);
//                    fuelcheck.show(getFragmentManager(), "Fuel Check");
//                    bar.setVisibility(View.VISIBLE);
                    APIPOSTRequest(diffTrun);
                    v.setTag(1);
                }
            }
        });


    }

}
