package com.example.indhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class login extends AppCompatActivity {
    private TextView fbook,acc,sin,sup;
    private EditText mal,pswd;
    LinearLayout sinSection;
    LinearLayout supSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sup = (TextView)findViewById(R.id.sup);
        sin = (TextView)findViewById(R.id.sin);
        sinSection = findViewById(R.id.sinSection);
        supSection = findViewById(R.id.supSection);

        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sinSection.setVisibility(View.GONE);
                supSection.setVisibility(View.VISIBLE);
                sup.setTextColor(Color.BLACK);
                sin.setTextColor(Color.WHITE);
            }
        });

        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinSection.setVisibility(View.VISIBLE);
                supSection.setVisibility(View.GONE);
                sup.setTextColor(Color.WHITE);
                sin.setTextColor(Color.BLACK);
            }
        });

    }
}
