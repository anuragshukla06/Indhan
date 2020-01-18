package com.example.indhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import customfonts.MyRegulerText;

public class login extends AppCompatActivity {
    private TextView fbook,acc,sin,sup;
    private EditText emailSupEditText,pswdSupEditText, pswdConfirmEditText, vehicleModelEditText;
    LinearLayout sinSection;
    LinearLayout supSection;
    String authKey;
    MyRegulerText signInButton;
    boolean signedIn = false;


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
        sup = (TextView)findViewById(R.id.sup);
        sin = (TextView)findViewById(R.id.sin);
        sinSection = findViewById(R.id.sinSection);
        supSection = findViewById(R.id.supSection);
        signInButton = findViewById(R.id.signInButton);

        SharedPreferences sharedPref = getApplication().getSharedPreferences(
                "mainSP", Context.MODE_PRIVATE);

        authKey = sharedPref.getString("authKEY", "");

        if (authKey.isEmpty()) {
            signedIn = false;
            showSignInPage();
        } else {

            // CALL NEW ACTIVITY HERE

        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
}
