package com.example.indhan;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.indhan.login.latitude;
import static com.example.indhan.login.longitude;

public class MainGraphActivity extends AppCompatActivity {

    public static class FuelDataService extends Service {
        RequestQueue queue;
        static LocationManager locationManager;
        static LocationListener locationListener;
        NotificationManagerCompat notificationManager;
        NotificationCompat.Builder DangerNotiBuilder;
        SharedPreferences sharedPref;
        double volumeReading;
        String hieghtReading;

        @Override
        public void onCreate() {
            queue = Volley.newRequestQueue(this);

            sharedPref = getApplication().getSharedPreferences(
                    "mainSP", Context.MODE_PRIVATE);
            DangerNotiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("You are running low on fuel")
                    .setContentText("Please head to the nearest station")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            notificationManager = NotificationManagerCompat.from(this);



            super.onCreate();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        StringRequest getSendReadingRequest() {
            String serverURL = login.BASE_URL + "/refresh";
            return new StringRequest(Request.Method.POST, serverURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Toast.makeText(FuelDataService.this, response + " " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
//                            textView.setText("Response is: "+ response.substring(0,500));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "That didn't work!" + error, Toast.LENGTH_LONG).show();
                }
            })
            {
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    String authKey = sharedPref.getString("authkey", "");
                    params.put("token", authKey);
                    params.put("lat", String.valueOf(latitude));
                    params.put("lon", String.valueOf(longitude));
                    params.put("petrol", String.valueOf(volumeReading));
                    //Add the data you'd like to send to the server.
                    return params;
                }
            };


        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            final Handler handler = new Handler();
            String rpiURL ="http://192.168.137.147:8080/";

// Request a string response from the provided URL.
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, rpiURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                hieghtReading = jsonObject.getString("height");
                                volumeReading = jsonObject.getDouble("volume");
                                Toast.makeText(FuelDataService.this, response, Toast.LENGTH_SHORT).show();

                                StringRequest sendReadingRequest = getSendReadingRequest();
                                queue.add(sendReadingRequest);

                                if ((volumeReading < 100)) {
                                    notificationManager.notify(2, DangerNotiBuilder.build());

                                }


                            } catch (JSONException e) {
                                Toast.makeText(FuelDataService.this, "Server Error", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
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
        setContentView(R.layout.activity_main_graph);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFrag = null;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFrag = new HomeFragment();
                    break;
                case R.id.nav_planner:
                    selectedFrag = new PlannerFragment();
                    break;
                case R.id.nav_history:
                    selectedFrag = new HistoryFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();

            return true;
        }
    };
}
