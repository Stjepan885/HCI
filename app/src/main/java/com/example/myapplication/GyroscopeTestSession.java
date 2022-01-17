package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ThreadLocalRandom;

public class GyroscopeTestSession extends AppCompatActivity {
    DatabaseReference databaseReference;
    private SharedPreferences prefs;

    private long startTime1, startTime2, endTime1, endTime2;
    private long swipeTime = 0l, zoomTime = 0l;

    private Gyroscope gyroscope;

    private ProgressBar prog;
    private DrawImageView image;
    private TextView nbImage, testsDone;;

    private final int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
            R.drawable.a13, R.drawable.a14};
    private final int[] imageZoomArrayX = {1750, 750, -500, -1750};
    private final int[] imageZoomArrayY = {3750, 2500, 1250, 0, -1250, -2500, -3750};

    private int currentImage = 0;
    private int mode = 0; // 0 - swipe 1 - zoom

    private final int counterDefault = 6;
    private final int rotationLine = 2;
    private int counter = 5;
    private int testCounter = 1;
    private int randomTest = 0, randomSwipeNumber;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;
    private float imageLeft, imageRight, imageTop, imageBottom;
    private int randomX, randomY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_test_session);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        gyroscope = new Gyroscope(this);

        Button button = (Button) findViewById(R.id.button);
        TextView nb = (TextView) findViewById(R.id.textView2);
        nbImage = (TextView) findViewById(R.id.textView3);
        testsDone = findViewById(R.id.textViewTestDone);

        prog = findViewById(R.id.progressBar);
        prog.setMax(100);
        image = findViewById(R.id.imageView);

        imageX = image.getScaleX();
        imageY = image.getScaleY();
        defaultImageX = imageX;
        defaultImageY = imageY;
        defaultX = image.getX();
        defaultY = image.getY();

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float x, float y, float z) {

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
                        if (image.getScaleX() == 5 && image.getX() == imageZoomArrayX[randomX] && image.getY() == imageZoomArrayY[randomY]) {
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
                    prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("swipeG", swipeTime);
                    editor.putLong("zoomG", zoomTime);
                    editor.apply();

                    String id = prefs.getString("id",null);

                    int swipe = (int)swipeTime;
                    int zoom = (int)zoomTime;

                    databaseReference.child(id).child("swipeTimeG").setValue(swipe);
                    databaseReference.child(id).child("zoomTimeG").setValue(zoom);

                    //end of test
                    Intent intent = new Intent(GyroscopeTestSession.this, EndOfTest.class);
                    startActivity(intent);
                }

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

                        nb.setText((currentImage + 1) + "/14");
                        break;
                    case 1:
                        if (counter == 0) {
                            if (z <= -rotationLine) {
                                counter = counterDefault;
                                if (image.getScaleX() < 5f) {
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

                                image.setX(image.getX() - 250);

                                counter = counterDefault;
                            } else if (y <= -rotationLine) {

                                image.setX(image.getX() + 250);

                                counter = counterDefault;
                            } else if (x >= rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() - 250);
                            } else if (x <= -rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() + 250);
                            }

                        }

                        if (counter > 0) {
                            counter--;
                        }
                        break;
                    case 2:
                        if (counter == 0) {
                            if (z <= -rotationLine) {
                                zoomIn();
                                counter = counterDefault;
                                mode = 1;
                            }
                        }

                        if (counter > 0) {
                            counter--;
                        }
                        break;
                }

                if (counter == counterDefault) {
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
        gyroscope.register();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscope.unregister();
    }


}