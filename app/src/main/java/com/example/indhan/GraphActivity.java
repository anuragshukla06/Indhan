package com.example.indhan;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class GraphActivity extends AppCompatActivity {



    public static class FuelDataService extends Service {
        RequestQueue queue;

        @Override
        public void onCreate() {
            queue= Volley.newRequestQueue(this);
            super.onCreate();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            String rpiURL ="http://192.168.43.13:8080/";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, rpiURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//                            textView.setText("Response is: "+ response.substring(0,500));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "That didn't work!" + error, Toast.LENGTH_LONG).show();
                }
            });

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 10000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 10000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);

            return super.onStartCommand(intent, flags, startId);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        GraphView mileageGraph = (GraphView) findViewById(R.id.MileageGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        mileageGraph.addSeries(series);
        series.setTitle("Mileage");
        series.setColor(Color.MAGENTA);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        mileageGraph.getLegendRenderer().setVisible(true);
        mileageGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GraphView distanceGraph = (GraphView) findViewById(R.id.DistanceGraph);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 3),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5)
        });
        distanceGraph.addSeries(series2);
        series2.setTitle("Distance");
        series2.setColor(Color.RED);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        series2.setThickness(8);
        distanceGraph.getLegendRenderer().setVisible(true);
        distanceGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GraphView fuelGraph = (GraphView) findViewById(R.id.FuelGraph);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(2, 3),
                new DataPoint(3, 5),
                new DataPoint(3, 2),
                new DataPoint(5, 4)
        });
        fuelGraph.addSeries(series3);
        series3.setTitle("Fuel");
        series3.setColor(Color.BLACK);
        series3.setDrawDataPoints(true);
        series3.setDataPointsRadius(10);
        series3.setThickness(8);
        fuelGraph.getLegendRenderer().setVisible(true);
        fuelGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }


}
