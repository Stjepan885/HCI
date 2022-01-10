package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private Button inst1_button, inst2_button, start1_button, start2_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        inst1_button = findViewById(R.id.ins1);
        inst2_button = findViewById(R.id.ins2);
        start1_button = findViewById(R.id.start1);
        start2_button = findViewById(R.id.start2);

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
                //ntent intent = new Intent(MainMenu.this, .class);
                //startActivity(intent);
            }
        });

        start1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, GyroscopeSession.class);
                startActivity(intent);
            }
        });

    }
}