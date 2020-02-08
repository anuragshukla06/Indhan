package com.example.indhan;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.widget.Button;
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

    final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;


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
//                            Toast.makeText(FuelDataService.this, response + " " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
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

        StringRequest getNearbyPumpsRequest() {
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

                                PendingIntent nearestPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, mapIntent1, 0);
                                PendingIntent nearPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, mapIntent2, 0);

                                DangerNotiBuilder = new NotificationCompat.Builder(getApplicationContext())
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
                    Toast.makeText(getApplicationContext(), "That didn't work!" + error, Toast.LENGTH_LONG).show();
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

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

//            final Handler handler = new Handler();
            String rpiURL ="http://192.168.137.232:8080/";
//            Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
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
//                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                                StringRequest sendReadingRequest = getSendReadingRequest();
                                queue.add(sendReadingRequest);

                                if ((volumeReading < 100)) {

                                    StringRequest nearbyPumpsRequest = getNearbyPumpsRequest();
                                    queue.add(nearbyPumpsRequest);


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names in SOS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_graph);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }


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
                case R.id.nav_nearest_pumps:
                    selectedFrag = new NearbyPumps();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();

            return true;
        }
    };
}
