package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    private Button inst1_button, inst2_button, start1_button, start2_button;
    private TextView informationText;
    private SharedPreferences prefs;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Change user?");
                alertDialogBuilder
                        .setMessage("Click yes to change!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);
                                        prefs.edit().clear().commit();
                                        Intent intent = new Intent(MainMenu.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            case R.id.action_reset:
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
                alertDialogBuilder1.setTitle("Reset?");
                alertDialogBuilder1
                        .setMessage("Click yes to reset!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);
                                        String idd = prefs.getString("id", null);
                                        String user = prefs.getString("name", null);
                                        prefs.edit().clear().commit();

                                        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);

                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("id", idd);
                                        editor.putString("name", user);
                                        editor.apply();
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                alertDialog1.show();

                return true;
            default:


                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}