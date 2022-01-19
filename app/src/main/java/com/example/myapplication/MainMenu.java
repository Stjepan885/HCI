package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    private Button inst1_button, inst2_button, start1_button, start2_button;
    private TextView informationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //check gyroscope, accelerometer
        PackageManager packageManager = getPackageManager();
        boolean gyroExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        boolean accelerometerExists = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);

        inst1_button = findViewById(R.id.ins1);
        inst2_button = findViewById(R.id.ins2);
        start1_button = findViewById(R.id.start1);
        start2_button = findViewById(R.id.start2);
        informationText = findViewById(R.id.sensor_text);

        if (gyroExists == false){
            informationText.setText("Vaš uređaj ne posjeduje giroskop. Niste u mogučnosti riješiti test 2!");
            start2_button.setAlpha(0.5f);
            inst2_button.setAlpha(0.5f);
            start2_button.setClickable(false);
            inst2_button.setClickable(false);
        }

        if (accelerometerExists == false){
            informationText.setText("Vaš uređaj ne posjeduje senzor linearnog ubrzanja. Niste u mogučnosti riješiti test 1!");
            start1_button.setAlpha(0.5f);
            inst1_button.setAlpha(0.5f);
            start1_button.setClickable(false);
            inst1_button.setClickable(false);
        }

        inst1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, AccelerometerInstructions.class);
                startActivity(intent);
            }
        });

        inst2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, InstructionsGyroscope.class);
                startActivity(intent);
            }
        });

        start1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, AccelerometerActivityTest.class);
                startActivity(intent);
            }
        });

        start2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, GyroscopeTestSession.class);
                startActivity(intent);
            }
        });

    }
}