package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AccelerometerActivityTest extends AppCompatActivity {
    DatabaseReference databaseReference;

    private SharedPreferences prefs;

    private long startTime1, startTime2, endTime1, endTime2;
    private long swipeTime = 0, zoomTime = 0;

    private Accelerometer accelerometer;

    private ProgressBar prog;
    private DrawImageView image;
    private TextView nb;
    private TextView nbImage, testsDone;

    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
            R.drawable.a13, R.drawable.a14};
    private int[] imageZoomArrayX = {1750, 750, -500, -1750};
    private int[] imageZoomArrayY = {3750, 2500, 1250, 0, -1250, -2500, -3750};

    private int currentImage = 0;
    private int mode = 0; // 0 - swipe 1 - zoom

    private final int counterDefault = 20;
    private int counterTwoDefault = 2;
    private int counter = 10, zCounter = 10, counterTwo = 2;
    private int testCounter = 1;
    private int randomTest = 0, randomSwipeNumber;


    private float imageX, imageY, defaultScaleImageX, defaultScaleImageY, defaultX, defaultY;
    private float imageLeft, imageRight, imageTop, imageBottom;
    private int randomX, randomY;

    private float accSumX = 0, accSumY = 0, accSumZ = 0;
    private boolean set = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_test);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        accelerometer = new Accelerometer(this);

        nb = (TextView) findViewById(R.id.textView2);
        nbImage = (TextView) findViewById(R.id.textView3);
        image = findViewById(R.id.imageView);
        testsDone = findViewById(R.id.textViewTestDone);

        prog = findViewById(R.id.progressBar);
        prog.setMax(100);

        defaultScaleImageX = image.getScaleX();
        defaultScaleImageY = image.getScaleY();
        defaultX = image.getX();
        defaultY = image.getX();

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float x, float y, float z) {

                if (testCounter != 0) {
                    if (randomTest == 0) {
                        randomSwipeNumber = ThreadLocalRandom.current().nextInt(0, 13);
                        nbImage.setText(" " + (randomSwipeNumber + 1));
                        randomTest = 3;
                        startTime1 = System.currentTimeMillis();
                    } else if (randomTest == 3) {
                        if (currentImage == randomSwipeNumber) {
                            randomSquare();
                            randomTest = 4;
                            mode = 2;
                            endTime1 = System.currentTimeMillis();
                            swipeTime += endTime1 - startTime1;
                            startTime2 = endTime1;
                        }
                    } else if (randomTest == 4) {
                        if (image.getScaleX() == 5 && image.getX() < imageZoomArrayX[randomX] + 800 && image.getX() > imageZoomArrayX[randomX] - 800
                                && image.getY() < imageZoomArrayY[randomY] + 800 && image.getY() > imageZoomArrayY[randomY] - 800) {
                            image.setX(defaultX);
                            image.setY(defaultY);
                            image.setScaleX(1);
                            image.setScaleY(1);
                            randomSquareZero();
                            randomTest = 0;
                            mode = 0;
                            testCounter--;
                            testsDone.setText((5 - testCounter) + "/5");
                            endTime2 = System.currentTimeMillis();
                            zoomTime += endTime2-startTime2;
                        }
                    }
                } else {
                    //end of test

                    prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("swipe", swipeTime);
                    editor.putLong("zoom", zoomTime);
                    editor.apply();

                    String id = prefs.getString("id",null);

                    int swipe = (int)swipeTime;
                    int zoom = (int)zoomTime;

                    databaseReference.child(id).child("swipeTime").setValue(swipe);
                    databaseReference.child(id).child("zoomTime").setValue(zoom);


                    Intent intent = new Intent(AccelerometerActivityTest.this, EndOfTest.class);
                    startActivity(intent);
                }

                if (counterTwo > 0 && set == true) {
                    counterTwo--;
                }
                if (x >= 3 || x <= -3f || y >= 3 || y <= -3f || z >= 3 || z <= -3f) {
                    if (counter == 0 && set == false) {
                        counterTwo = counterTwoDefault;
                        set = true;
                    }
                }

                accSumX += x;
                accSumY += y;
                if (zCounter != 0) {
                    zCounter--;
                } else {
                    accSumZ += z;
                }

                accSumX = accSumX - (accSumX * 0.2f);
                accSumY = accSumY - (accSumY * 0.2f);
                accSumZ = accSumZ - (accSumZ * 0.2f);

                switch (mode) {
                    case 0:
                        if (accSumZ >= 2 && counterTwo == 0) {
                            counter = counterDefault;
                            set = false;
                            mode = 1;
                            counterTwo = counterTwoDefault;
                            zoomIn();
                        } else if (accSumX >= 1.3f && counterTwo == 0) {
                            currentImage -= 1;
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumX <= -1.3f && counterTwo == 0) {
                            currentImage += 1;
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumY >= 1.3f && counterTwo == 0) {
                            currentImage -= 5;
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumY <= -1.3f && counterTwo == 0) {
                            currentImage += 5;
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else {
                            mode = 0;
                        }

                        if (currentImage < 0) {
                            currentImage = 0;
                        } else if (currentImage > 13) {
                            currentImage = 13;
                        }
                        image.setImageResource(images[currentImage]);
                        break;

                    case 1:
                        if (accSumZ >= 2 && counterTwo == 0) {
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                            if (image.getScaleX() < 5f) {
                                zoomIn();
                            }
                        } else if (accSumZ <= -2 && counterTwo == 0) {
                            zoomOut();
                            if (image.getScaleY() == defaultScaleImageY) {
                                image.setX(defaultX);
                                image.setY(defaultY);
                                mode = 0;
                            }
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumX >= 1.5f && counterTwo == 0) {
                            image.setX(image.getX() + 250);
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumX <= -1.5f && counterTwo == 0) {
                            image.setX(image.getX() - 250);
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumY >= 1.5f && counterTwo == 0) {
                            image.setY(image.getY() - 250);
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumY <= -1.5f && counterTwo == 0) {
                            image.setY(image.getY() + 250);
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        }
                        if (counter > 0) {
                            counter--;
                        }
                        break;
                    case 2:
                        if (accSumZ >= 2 && counterTwo == 0) {
                            counter = counterDefault;
                            set = false;
                            mode = 1;
                            counterTwo = counterTwoDefault;
                            zoomIn();
                        }
                        break;
                }
                if (counter == counterDefault) {
                    prog.setProgress(100);
                } else {
                    prog.setProgress(100 - (5 * (counter)));
                }

                if (counter > 0) {
                    counter--;
                }
                nb.setText((currentImage + 1) + "/14");
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
        if (image.getScaleX() == 1) {
            return;
        }
        image.setScaleX(image.getScaleX() - 1);
        image.setScaleY(image.getScaleY() - 1);
        counter = counterDefault;
        checkImageBorder();
    }

    private void zoomIn() {
        if (image.getScaleX() == 5) {
            return;
        }
        image.setScaleX(image.getScaleX() + 1);
        image.setScaleY(image.getScaleY() + 1);
        counter = counterDefault;
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
            if (image.getX() < -750) {
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
            if (image.getX() < -1250) {
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
            if (image.getX() < -1750) {
                image.setX(-1750);
            }
            if (image.getX() > 1750) {
                image.setX(1750);
            }
            if (image.getY() < -3000) {
                image.setY(-3000);
            }
        }
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