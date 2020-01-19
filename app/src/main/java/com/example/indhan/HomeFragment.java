package com.example.indhan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static com.example.indhan.login.latitude;
import static com.example.indhan.login.longitude;


public class HomeFragment extends Fragment {
    static  double volumeReading, before, after, diff, distance, mileage;
    String hieghtReading;
    String rpiURL = "http://192.168.137.147:8080/";
    static RequestQueue queue;
    static JSONArray dist, mile, fuelCom;
    GraphView mileageGraph, distanceGraph, fuelGraph;
    TextView fuelView;
    RelativeLayout back;
    SharedPreferences sharedPref;
    TextView distView;
    TextView mileageView;
    Button logoutButton;
    String authKey;

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


                            DataPoint[] dataPoints = new DataPoint[mile.length()]; // declare an array of DataPoint objects with the same size as your list
                            for (int i = 0; i < mile.length(); i++) {
                                // add new DataPoint object to the array for each of your list entries
                                try {
                                    dataPoints[i] = new DataPoint(i + 1, mile.getDouble(i));
                                }catch (JSONException e){}// not sure but I think the second argument should be of type double
                            }
                            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
                            mileageGraph = getView().findViewById(R.id.MileageGraph);
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
                            fuelGraph.addSeries(series3);
                            series3.setTitle("Fuel");
                            series3.setColor(Color.BLACK);
                            series3.setDrawDataPoints(true);
                            series3.setDataPointsRadius(10);
                            series3.setThickness(8);
                            fuelGraph.getLegendRenderer().setVisible(true);
                            fuelGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);



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
                            Toast.makeText(getActivity(), "Some error occured!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", "onErrorResponse: " + error);
                Toast.makeText(getActivity(), "Distance Can't be fetched!" + error, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), "RASP didn't work!" + error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
        return volumeReading;
    }

    public static class PetrolPumpReview extends  DialogFragment{
        TextView nameView, GRating, appRating;
        EditText userRating;
        String name, totalUserRatings;
        double ratings;
        PetrolPumpReview(JSONObject json) {
            try {
                name = json.getString("name");
                totalUserRatings = json.getString("app_ratings");
                ratings = json.getDouble("rating");
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

            nameView = view.findViewById(R.id.name);
            GRating = view.findViewById(R.id.rating);
            appRating = view.findViewById(R.id.app_rating);
            nameView.setText(name);
            GRating.setText(ratings+"");
            appRating.setText(totalUserRatings);

            userRating = view.findViewById(R.id.your_rating);

            builder.setTitle("Reviews")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String str = userRating.getText().toString();

                            String serverURL = login.BASE_URL + "/petrol_pump_ratings_response";
                            final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                                Log.i("response", response);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("Error", "onErrorResponse: " + error);
                                    Toast.makeText(getActivity(), "Couldn't POST" + error, Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("name", String.valueOf(name));
                                    params.put("rating", str);
                                    //Add the data you'd like to send to the server.
                                    return params;
                                }
                            };
                            queue.add(stringRequest);
                        }
                    });
            return builder.create();
        }

    }

    public static class FuelCheckDialog extends DialogFragment {
        double difference;

        FuelCheckDialog(double diff) {
            difference = diff;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Did you just fill your tank with " + difference + " Litres ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return builder.create();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_graph, container, false);
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        sharedPref = getActivity().getSharedPreferences(
                "mainSP", Context.MODE_PRIVATE);
        authKey = sharedPref.getString("authkey", "");

//        current_stats

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                APIRequest();

                Double Trun = BigDecimal.valueOf(volumeReading)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                fuelView.setText("FUEL: "+Trun+" L");

                int color;
                if(Trun<1)
                    color = Color.parseColor("#ef9a9a");
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


                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(r, 5000);

        final Button refill = getView().findViewById(R.id.refill);
        final ProgressBar bar = getView().findViewById(R.id.progress_bar);
        refill.setTag(1);
        refill.setOnClickListener(new View.OnClickListener() {

            void APIPOSTRequest() {
                String serverURL = login.BASE_URL + "/petrol_pump_ratings";
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    PetrolPumpReview review = new PetrolPumpReview(jsonObject);
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
                    FuelCheckDialog fuelcheck = new FuelCheckDialog(diffTrun);
                    fuelcheck.show(getFragmentManager(), "Fuel Check");
                    APIPOSTRequest();
                    v.setTag(1); //pause
                }
            }
        });


    }

}
