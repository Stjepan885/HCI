package com.example.myapplication;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccelerometerActivityTest extends AppCompatActivity {

    private TextView xText, yText, zText;
    private Accelerometer accelerometer;
    private int counter = 10, zCounter = 10;
    private float imageX, imageY, defaultImageX, defaultImageY;
    private ImageView image;
    private float accSumX = 0, accSumY = 0, accSumZ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_test);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        accelerometer = new Accelerometer(this);

        xText = findViewById(R.id.x);
        yText = findViewById(R.id.y);
        zText = findViewById(R.id.z);

        image = findViewById(R.id.imageView);

        imageX = image.getScaleX();
        imageY = image.getScaleY();
        defaultImageX = imageX;
        defaultImageY = imageY;

        yText.setText(imageX + "" + imageY);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float x, float y, float z) {

                accSumX += x;
                accSumY += y;
                if (zCounter != 0) {
                    zCounter--;
                } else {
                    accSumZ += z;
                }

                accSumZ = accSumZ - (accSumZ * 0.2f);


                //xText.setText("" + String.format("%.2f", accSumX));
                //yText.setText("" + String.format("%.2f", accSumY));
                yText.setText("" + String.format("%.2f", accSumZ));

/*
                if (imageX != defaultImageX) {

                    if (x >= 2 && counter == 0) {
                        image.setX(image.getX() + 250);
                        counter = 5;
                    }
                    if (x <= -2 && counter == 0) {
                        image.setX(image.getX() - 250);
                        counter = 5;
                    }
                }

                if (imageX != defaultImageX) {

                    if (y >= 2 && counter == 0) {
                        image.setY(image.getY() + 250);
                        counter = 5;
                    }
                    if (y <= -2 && counter == 0) {
                        image.setY(image.getY() - 250);
                        counter = 5;
                    }
                }
                */
                if (accSumZ >= 3 && counter == 0) {
                     zoomIn();
                    xText.setText("zoom in");
                    counter  = 10;
                }
                if (accSumZ <= -3 && counter == 0 && imageX == defaultImageX){
                    counter = 10;
                }
                if (accSumZ <= -3 && counter == 0 && imageX != defaultImageX) {
                     zoomOut();
                    xText.setText("zoom out");
                    counter = 10;
                }
                if (counter > 0) {
                    counter--;
                }
                zText.setText(counter + "");

            }

            private void zoomOut() {
                image.setScaleX(imageX - 1);
                image.setScaleY(imageY - 1);
                imageX = image.getScaleX();
                imageY = image.getScaleY();
                counter = 10;
            }

            private void zoomIn() {
                image.setScaleX(imageX + 1);
                image.setScaleY(imageY + 1);
                imageX = image.getScaleX();
                imageY = image.getScaleY();
                counter = 10;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        accelerometer.register();

    }

    @Override
    protected void onPause() {
        super.onPause();
        accelerometer.unregister();
    }
}