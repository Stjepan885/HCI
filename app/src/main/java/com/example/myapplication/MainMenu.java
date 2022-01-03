package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private Button inst1_button, inst2_button, start1_button, start2_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        inst1_button = findViewById(R.id.ins1);
        inst2_button = findViewById(R.id.ins2);
        start1_button = findViewById(R.id.start1);
        start2_button = findViewById(R.id.start2);


    }
}