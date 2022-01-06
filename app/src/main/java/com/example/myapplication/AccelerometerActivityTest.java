package com.example.myapplication;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccelerometerActivityTest extends AppCompatActivity {

    private TextView xText, yText, zText;
    private Accelerometer accelerometer;
    private int counter = 10, zCounter = 10, yCounter3 = 0, counterTwo = 2;
    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;
    private ImageView image;
    private float accSumX = 0, accSumY = 0, accSumZ = 0;
    private boolean set = false;

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
        defaultX = image.getX();
        defaultY = image.getX();


        accelerometer.setListener(new Accelerometer.Listener() {

            @Override
            public void onTranslation(float x, float y, float z) {

                if (counterTwo > 0 && set == true){
                    counterTwo--;
                }
                if (accSumX >= 3 || accSumX <= -3f || y >= 3 || y <= -3f || z >= 3 || z <= -3f) {
                    if (counter == 0 && set == false) {
                        counterTwo = 4;
                        set = true;
                    }
                }

                xText.setText("" + counterTwo + " " + set);

                accSumX += x;
                accSumY += y;
                if (zCounter != 0) {
                    zCounter--;
                } else {
                    accSumZ += z;
                }


                //xText.setText("" + String.format("%.2f", accSumX));
                //yText.setText("" + String.format("%.2f", accSumY));
                yText.setText("" + String.format("%.2f", accSumX));

                // left right
                accSumX = accSumX - (accSumX * 0.2f);
                accSumY = accSumY - (accSumY * 0.2f);
                accSumZ = accSumZ - (accSumZ * 0.2f);


                zText.setText("" + counter);

                if (accSumX >= 1.5f && counterTwo == 0) {
                    image.setX(image.getX() + 250);
                    counter = 10;

                    set = false;
                    counterTwo = 3;
                } else if (accSumX <= -1.5f && counterTwo == 0) {
                    image.setX(image.getX() - 250);
                    counter = 10;
                    zText.setText("left");
                    set = false;
                    counterTwo = 3;
                }
                if (counter > 0) {
                    counter--;
                }

                if (accSumY >= 1.5f && counterTwo == 0) {
                    image.setY(image.getY() - 250);
                    counter = 10;

                    set = false;
                    counterTwo = 3;
                }
                if (accSumY <= -1.5f && counterTwo == 0) {
                    image.setY(image.getY() + 250);
                    counter = 10;

                    set = false;
                    counterTwo = 3;
                }


                //zoom
                if (accSumZ >= 2 && counterTwo == 0) {
                    zoomIn();
                    xText.setText("zoom in");
                    counter = 10;
                    set = false;
                    counterTwo = 3;
                }
                if (accSumZ <= -2 && counterTwo == 0 && imageX == defaultImageX) {
                    counter = 10;
                    set = false;
                    counterTwo = 3;
                }
                if (accSumZ <= -2 && counterTwo == 0 && imageX != defaultImageX) {
                    zoomOut();
                    xText.setText("zoom out");
                    counter = 10;
                    set = false;
                    counterTwo = 3;
                }
                zText.setText(counter + "");
                //-zoom


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