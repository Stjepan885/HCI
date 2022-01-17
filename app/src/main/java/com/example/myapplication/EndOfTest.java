package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndOfTest extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button mainMenu, send;

    private TextView finished1, finished2, time1, time2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_test);

        mainMenu = findViewById(R.id.buttonBack);
        finished1 = findViewById(R.id.textView8);
        finished2 = findViewById(R.id.textView10);
        time1 = findViewById(R.id.textView13);
        time2 = findViewById(R.id.textView11);

        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);
        long swipeTime = prefs.getLong("swipe",0);
        long zoomTime = prefs.getLong("zoom",0);
        long swipeTimeG = prefs.getLong("swipeG",0);
        long zoomTimeG = prefs.getLong("zoomG",0);


        if (swipeTime != 0 && zoomTime != 0){
            finished1.setText("da");
            time1.setText("" + swipeTime+zoomTime);
        }

        if (swipeTimeG != 0 && zoomTimeG != 0){
            finished2.setText("da");
            time2.setText("" + swipeTimeG+zoomTimeG);
        }

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndOfTest.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }
}