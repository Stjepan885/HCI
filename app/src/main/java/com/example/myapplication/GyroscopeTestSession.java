package com.example.myapplication;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GyroscopeTestSession extends AppCompatActivity {

    private GyroscopeAlg gyroAlg;
    private Gyroscope gyroscope;
    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
            R.drawable.a13, R.drawable.a14};
    private int currentImage = 0;
    private int mode = 0; // 0 - swipe 1 - zoom
    private int ret = 0;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;
    private ImageView image;


    private final int counterDefault = 6;
    private final int rotationLine = 2;
    private int counter = 5;

    private ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_test_session);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //gyroAlg = new GyroscopeAlg();
        gyroscope = new Gyroscope(this);

        Button button = (Button) findViewById(R.id.button);
        TextView nb = (TextView) findViewById(R.id.textView2);

        image = findViewById(R.id.imageView);
        prog = findViewById(R.id.progressBar);
        prog.setMax(100);

        imageX = image.getScaleX();
        imageY = image.getScaleY();
        defaultImageX = imageX;
        defaultImageY = imageY;
        defaultX = image.getX();
        Log.e(" dsd", " " + defaultImageX);
        defaultY = image.getX();
        float imageLeft = image.getLeft();
        float imageTop = image.getTop();
        Log.e(" dsd", " " + imageLeft);

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float x, float y, float z) {
                switch (mode) {
                    case 0:
                        if (counter == 0) {
                            if (y >= rotationLine) {
                                currentImage += 1;
                                counter = counterDefault;
                            } else if (y <= -rotationLine) {
                                currentImage -= 1;
                                counter = counterDefault;
                            } else if (x >= rotationLine) {
                                currentImage -= 5;
                                counter = counterDefault;
                            } else if (x <= -rotationLine) {
                                currentImage += 5;
                                counter = counterDefault;
                            } else if (z <= -rotationLine) {
                                zoomIn();
                                counter = counterDefault;
                                mode = 1;
                            } else {
                                mode = 0;
                            }

                            if (currentImage < 0) {
                                currentImage = 0;
                            } else if (currentImage > 13) {
                                currentImage = 13;
                            }
                        }

                        if (counter > 0) {
                            counter--;
                        }
                        image.setImageResource(images[currentImage]);

                        nb.setText(currentImage + "/14");
                        break;
                    case 1:
                        if (counter == 0) {
                            if (z <= -rotationLine) {
                                counter = counterDefault;
                                if (image.getScaleX() < 10f) {
                                    zoomIn();
                                }
                            } else if (z >= rotationLine) {
                                zoomOut();
                                if (image.getScaleY() == defaultImageY) {
                                    image.setX(defaultX);
                                    image.setY(defaultY);
                                    mode = 0;
                                }
                                counter = counterDefault;
                            } else if (y >= rotationLine) {
                                if ((image.getX() / image.getScaleX()) > -333) {
                                    image.setX(image.getX() - 250);
                                }
                                counter = counterDefault;
                            } else if (y <= -rotationLine) {
                                if ((image.getX() / image.getScaleX()) < 333){
                                    image.setX(image.getX() + 250);
                                }
                                counter = counterDefault;
                            } else if (x >= rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() - 250);
                            } else if (x <= -rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() + 250);
                            }

                            Log.e("dimenzije", image.getX() + " x " + image.getY() + " y " + image.getScaleX() + " x  scale" + image.getScaleY() + " y");
                        }

                        if (counter > 0) {
                            counter--;
                        }

                        break;
                }
                if (counter == counterDefault){
                    prog.setProgress(100);
                }else{
                    prog.setProgress(100 - (16*(counter)));
                }
            }
        });

    }

    private void zoomOut() {
        image.setScaleX(imageX - 1);
        image.setScaleY(imageY - 1);
        imageX = image.getScaleX();
        imageY = image.getScaleY();
    }

    private void zoomIn() {
        image.setScaleX(imageX + 1);
        image.setScaleY(imageY + 1);
        imageX = image.getScaleX();
        imageY = image.getScaleY();
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