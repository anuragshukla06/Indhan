package com.example.indhan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import customfonts.MyRegulerText;

public class login extends AppCompatActivity {
    private TextView fbook, acc, sin, sup;
    private EditText emailSupEditText, pswdSupEditText, pswdConfirmEditText, vehicleModelEditText;
    LinearLayout sinSection;
    LinearLayout supSection;
    MyRegulerText signInButton;
    TextView signUpButton;
    boolean signedIn = false;
    EditText emailSinEditText, pswdSinEditText;
   SharedPreferences sharedPref;

    RequestQueue MyRequestQueue;
    String BASE_URL = "http://172.22.125.23:8000";


    void showSignUpPage() {
        sinSection.setVisibility(View.GONE);
        supSection.setVisibility(View.VISIBLE);
        sup.setTextColor(Color.BLACK);
        sin.setTextColor(Color.WHITE);
    }

    void showSignInPage() {
        sinSection.setVisibility(View.VISIBLE);
        supSection.setVisibility(View.GONE);
        sup.setTextColor(Color.WHITE);
        sin.setTextColor(Color.BLACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sup = (TextView) findViewById(R.id.sup);
        sin = (TextView) findViewById(R.id.sin);
        sinSection = findViewById(R.id.sinSection);
        supSection = findViewById(R.id.supSection);
        signInButton = findViewById(R.id.signInButton);
        emailSupEditText = findViewById(R.id.emailSupEditText);
        pswdSupEditText = findViewById(R.id.pswdSupEditText);
        pswdConfirmEditText = findViewById(R.id.pswdConfirmEditText);
        vehicleModelEditText = findViewById(R.id.vehicleModelEditText);
        signUpButton = findViewById(R.id.signUpButton);
        MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        emailSinEditText = findViewById(R.id.emailSinEditText);
        pswdSinEditText = findViewById(R.id.pswdSinEditText);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("We have your back")
                .setContentText("We are continuously monitoring your fuel readings...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 1;
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

        startService(new Intent(this, GraphActivity.FuelDataService.class));

        sharedPref = getApplication().getSharedPreferences(
                "mainSP", Context.MODE_PRIVATE);

        final String authKey = sharedPref.getString("authkey", "");

        if (authKey.isEmpty()) {
            signedIn = false;
            showSignInPage();
        } else {

            // CALL NEW ACTIVITY HERE

        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = emailSinEditText.getText().toString();
                final String pswd = pswdSinEditText.getText().toString();

                String signInURL = BASE_URL + "/login";
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, signInURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status = responseObject.getString("success");
                            Toast.makeText(login.this, status, Toast.LENGTH_SHORT).show();
                            if (status.equals("true")) {

                                String authkey = responseObject.getString("token");
                                setSignInVariables(authkey);

                                Intent intent = new Intent(getApplicationContext(), GraphActivity.class); // TODO: CALL the intent to kavyansh activity
                                startActivity(intent);
                                Toast.makeText(login.this, authkey, Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(login.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(login.this, "Server Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username); //Add the data you'd like to send to the server.
                        params.put("password", pswd);
                        return params;
                    }
                };

                MyRequestQueue.add(MyStringRequest);
                // SignIn Logic here

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailSupEditText.getText().toString();
                //TODO: DO the valid email check.
                final String password = pswdSupEditText.getText().toString();
                String cnfrmPassword = pswdConfirmEditText.getText().toString();
                final String vehicleModel = vehicleModelEditText.getText().toString();
                if (email.isEmpty() || password.isEmpty() || cnfrmPassword.isEmpty() || vehicleModel.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter all the fields", Toast.LENGTH_LONG).show();
                } else {

                    if (!password.equals(cnfrmPassword)) {
                        Toast.makeText(login.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {

                        // Make Sign up post request here
                        String url = BASE_URL + "/signup";
                        StringRequest SignUpRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //This code is executed if the server responds, whether or not the response contains data.
                                //The String 'response' contains the server's response.
                                try {
                                    JSONObject responseObject = new JSONObject(response);
                                    String status = responseObject.getString("success");
                                    Toast.makeText(login.this, status, Toast.LENGTH_SHORT).show();
                                    if (status.equals("true")) {

                                        String authkey = responseObject.getString("token");

                                        Toast.makeText(login.this, authkey, Toast.LENGTH_SHORT).show();
                                        setSignInVariables(authkey);

//                                        Intent intent = new Intent(); // TODO: CALL the intent to kavyansh activity
                                        Intent intent = new Intent(getApplicationContext(), GraphActivity.class); // TODO: CALL the intent to kavyansh activity
                                        startActivity(intent);



                                    } else {
                                        Toast.makeText(login.this, "You are already signed up! Please Login.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(login.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(login.this, "Network Error Volley!", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", email);
                                params.put("password", password);
                                params.put("model", vehicleModel);
                                //Add the data you'd like to send to the server.
                                return params;
                            }
                        };

                        MyRequestQueue.add(SignUpRequest);


                    }

                }
            }
        });


        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpPage();
            }
        });

        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInPage();
            }
        });

    }

    void setSignInVariables (String authkey) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("authkey", authkey);
        editor.apply();
        signedIn = true;
    }
}
