package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class GyroscopeTest extends AppCompatActivity {

    private TextView xText, yText, zText;
    private Gyroscope gyroscope;
    private float imageX, imageY, defaultImageX, defaultImageY;
    private ImageView image;
    private int counter = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_test);

        gyroscope = new Gyroscope(this);

        xText = findViewById(R.id.x);
        yText = findViewById(R.id.y);
        zText = findViewById(R.id.z);

        image = findViewById(R.id.imageView);

        imageX = image.getScaleX();
        imageY = image.getScaleY();
        defaultImageX = imageX;
        defaultImageY = imageY;

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                xText.setText("" + String.format("%.2f", rx));
                yText.setText("" + String.format("%.2f", ry));
                zText.setText(""+ counter);

                // right left
                if (ry >= 2 && counter == 0){
                    image.setX(image.getX() + 250);
                    counter = 4;
                }
                if (ry <= -2 && counter == 0){
                    image.setX(image.getX() - 250);
                    counter = 4;
                }

                // up down
                if (rx >= 2 && counter == 0){
                    image.setY(image.getY() + 250);
                    counter = 4;
                }
                if (rx <= -2 && counter == 0){
                    image.setY(image.getY() - 250);
                    counter = 4;
                }

                // zoom
                if (rz >= 2 && counter == 0){
                    image.setScaleX(imageX - 1);
                    image.setScaleY(imageY - 1);
                    imageX = image.getScaleX();
                    imageY = image.getScaleY();
                    counter = 4;
                }
                if (rz <= -2 && counter == 0){
                    image.setScaleX(imageX + 1);
                    image.setScaleY(imageY + 1);
                    imageX = image.getScaleX();
                    imageY = image.getScaleY();
                    counter = 4;
                }


                if (counter > 0){
                    counter--;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscope.register();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscope.unregister();
    }
}