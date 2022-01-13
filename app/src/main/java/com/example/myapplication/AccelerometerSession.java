package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

public class AccelerometerSession extends AppCompatActivity {
    private AccelerationAlg accAlg;
    private Accelerometer accelerometer;

    private ProgressBar prog;
    private DrawImageView image;
    private TextView nbImage;

    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
            R.drawable.a13, R.drawable.a14};

    private int[] imageZoomArrayX = {1750, 750, -500, -1750};
    private int[] imageZoomArrayY = {3750, 2500, 1250, 0, -1250, -2500, -3750};

    private int currentImage = 0;
    private int mode = 0; // 0 - swipe 1 - zoom
    private int ret = 0;

    private final int counterDefault = 6;
    private int counter = 5;
    private int testCounter = 5;
    private int randomTest = 0, randomSwipeNumber;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;
    private float imageLeft, imageRight, imageTop, imageBottom;
    private int randomX, randomY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_session);

        accAlg = new AccelerationAlg();
        accelerometer = new Accelerometer(this);

        Button button = (Button) findViewById(R.id.button);
        TextView nb = (TextView) findViewById(R.id.textView2);
        nbImage = (TextView) findViewById(R.id.textView3);

        prog = findViewById(R.id.progressBar);
        prog.setMax(100);
        image = findViewById(R.id.imageView);


        imageX = image.getScaleX();
        imageY = image.getScaleY();
        defaultImageX = imageX;
        defaultImageY = imageY;
        defaultX = image.getX();
        defaultY = image.getX();

        accAlg.setImageX(imageX);
        accAlg.setImageY(imageY);
        accAlg.setDefaultX(defaultX);
        accAlg.setDefaultY(defaultY);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {

                if (testCounter != 0) {
                    if (randomTest == 0) {
                        randomSwipeNumber = ThreadLocalRandom.current().nextInt(0, 13);
                        nbImage.setText(" " + (randomSwipeNumber + 1));
                        randomTest = 3;
                    } else if (randomTest == 3) {
                        if (currentImage == randomSwipeNumber) {
                            randomSquare();
                            randomTest = 4;
                            mode = 2;
                        }
                    } else if (randomTest == 4) {
                        if (image.getScaleX() == 5 && image.getX() == imageZoomArrayX[randomX] && image.getY() == imageZoomArrayY[randomY]) {
                            image.setX(defaultX);
                            image.setY(defaultY);
                            image.setScaleX(1);
                            image.setScaleY(1);
                            randomSquareZero();
                            randomTest = 0;
                            mode = 0;
                            testCounter--;
                        }
                    }
                } else {
                    //end of test
                }

                switch (mode) {
                    case 0:
                        ret = accAlg.movementSwipe(tx, ty, tz);
                        if (ret == -1) {
                            mode = 1;
                            zoomIn();
                            break;
                        } else {
                            currentImage = ret;
                            image.setImageResource(images[currentImage]);
                            nb.setText("" + currentImage);
                        }
                        break;
                    case 1:
                        ret = accAlg.movement(tx, ty, tz);

                        switch (ret) {
                            case 1:
                                image.setX(image.getX() + 250);
                                nb.setText("right " + tx);
                                break;
                            case 2:
                                image.setX(image.getX() - 250);
                                nb.setText("left " + tx);
                                break;
                            case 3:
                                image.setY(image.getY() - 250);
                                break;
                            case 4:
                                image.setY(image.getY() + 250);
                                break;
                            case 5:
                                zoomIn();
                                break;
                            case 6:
                                if (image.getScaleY() != defaultY) {
                                    image.setX(defaultImageX);
                                    image.setY(defaultImageY);
                                    mode = 0;
                                }
                                zoomOut();
                                break;
                            default:
                                break;
                        }

                        break;
                }
                if (accAlg.getCounter() == counterDefault) {
                    prog.setProgress(100);
                } else {
                    prog.setProgress(100 - (16 * (counter)));
                }
            }
        });
    }

    private void randomSquare() {
        randomX = ThreadLocalRandom.current().nextInt(1, 4);
        randomY = ThreadLocalRandom.current().nextInt(1, 7);

        imageLeft = image.getX() + 20 + 250 * randomX;
        imageTop = image.getX() + 20 + 250 * randomX + 250;
        imageRight = image.getY() + 250 * randomY;
        imageBottom = image.getY() + 250 * randomY + 350;

        Log.e(" " + image.getHeight(), " ");
        Log.e(" " + image.getWidth(), " ");

        image.left = imageLeft;
        image.top = imageRight;
        image.right = imageTop;
        image.bottom = imageBottom;

        image.invalidate();
        image.drawRect = true;
    }

    private void randomSquareZero() {
        image.left = 0;
        image.top = 0;
        image.right = 0;
        image.bottom = 0;

        image.invalidate();
        image.drawRect = true;
    }


    private void zoomOut() {
        image.setScaleX(imageX - 1);
        image.setScaleY(imageY - 1);
        imageX = image.getScaleX();
        imageY = image.getScaleY();
        checkImageBorder();
    }

    private void checkImageBorder() {
        float scale = image.getScaleX();
        if (scale == 2) {
            if (image.getY() > 1000) {
                image.setY(1000);
            }
            if (image.getX() > 750) {
                image.setX(750);
            }
            if (image.getX() < -750){
                image.setX(-750);
            }
            if (image.getY() < -1000) {
                image.setY(-1000);
            }
        }
        if (scale == 3) {
            if (image.getY() > 2000) {
                image.setY(2000);
            }
            if (image.getX() > 1250) {
                image.setX(1250);
            }
            if (image.getX() < -1250){
                image.setX(-1250);
            }
            if (image.getY() < -2000) {
                image.setY(-2000);
            }
        }
        if (scale == 4) {
            if (image.getY() > 2250) {
                image.setY(2250);
            }
            if (image.getX() < -1750){
                image.setX(-1750);
            }
            if (image.getX() > 1750){
                image.setX(1750);
            }
            if (image.getY() < -3000) {
                image.setY(-3000);
            }
        }
    }

    private void zoomIn() {
        if (image.getScaleX() == 5) {
            return;
        }
        image.setScaleX(imageX + 1);
        image.setScaleY(imageY + 1);
        imageX = image.getScaleX();
        imageY = image.getScaleY();
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