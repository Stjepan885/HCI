package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
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
    private TextView nbImage, testsDone, zoomText, stepText;


    private final int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
            R.drawable.a13, R.drawable.a14,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a13, R.drawable.a14,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12};

    private int currentImage = 0, squareImage;
    private int mode = 0; // 0 - swipe 1 - zoom

    private final int counterDefault = 7;
    private final int rotationLine = 2;
    private int counter = 7;
    private int testCounter = 1;
    private int randomTest = 0, randomSwipeNumber;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;
    private float imageLeft, imageRight, imageTop, imageBottom;
    private int randomX, randomY;

    private boolean size = true;
    private int screenWidth, screenHeight;

    private int imageCenterWidth, imageCenterHeight;

    private int step = 100;

    private int locSwipe = 0, locZoom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_test_session);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        Log.e("koordinate", "X " + screenHeight + "Y " + screenWidth);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        gyroscope = new Gyroscope(this);

        Button button = (Button) findViewById(R.id.button);
        TextView nb = (TextView) findViewById(R.id.textView2);
        nbImage = (TextView) findViewById(R.id.textView3);
        testsDone = findViewById(R.id.textViewTestDone);
        zoomText = findViewById(R.id.textViewZoom);
        stepText = findViewById(R.id.textViewKorak);

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
                        randomSwipeNumber = ThreadLocalRandom.current().nextInt(0, 27);
                        nbImage.setText(" " + (randomSwipeNumber + 1));
                        randomTest = 3;
                        startTime1 = System.currentTimeMillis();
                        locSwipe = locSwipe + randomSwipeNumber;
                    } else if (randomTest == 3) {
                        if (currentImage == randomSwipeNumber) {
                            randomSquare();
                            squareImage = currentImage;
                            randomTest = 4;
                            mode = 2;
                            endTime1 = System.currentTimeMillis();
                            swipeTime += endTime1 - startTime1;
                            startTime2 = endTime1;
                        }
                    } else if (randomTest == 4) {
                        if (squareImage != currentImage) {
                            randomTest = 5;
                            randomSquareZero();
                        } else if (image.getScaleX() == 4) {
                            if (((Math.abs(image.getX())) > Math.abs((screenWidth / 2) * (randomX + 1) * (-1)) + 99 && Math.abs(image.getX()) < Math.abs(((screenWidth * 4) / 8) * ((randomX + 1)) * (-1)) - 99)
                                    || ((Math.abs(image.getX())) < Math.abs((screenWidth / 2) * (randomX + 1) * (-1)) + 99 && Math.abs(image.getX()) > Math.abs(((screenWidth * 4) / 8) * ((randomX + 1)) * (-1)) - 99)) {
                                if (((Math.abs(image.getY())) > Math.abs((screenHeight / 2) * (randomY + 1) * (-1)) + 99 && Math.abs(image.getY()) < Math.abs(((screenHeight * 4) / 8) * ((randomY + 1)) * (-1)) - 99)
                                        || ((Math.abs(image.getY())) < Math.abs((screenHeight / 2) * (randomY + 1) * (-1)) + 99 && Math.abs(image.getY()) > Math.abs(((screenHeight * 4) / 8) * ((randomY + 1)) * (-1)) - 99)) {
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
                                    zoomTime += endTime2 - startTime2;
                                }
                            }
                        }
                    } else if (randomTest == 5) {
                        if (squareImage == currentImage) {
                            randomSquareOld(randomX, randomY);
                            randomTest = 4;
                        }
                    }
                } else {
                    prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("swipeG", swipeTime);
                    editor.putLong("zoomG", zoomTime);
                    editor.apply();

                    String id = prefs.getString("id", null);

                    String swipe = String.valueOf(swipeTime);
                    String zoom = String.valueOf(zoomTime);

                    databaseReference.child(id).child("swipeTimeG").setValue(swipe);
                    databaseReference.child(id).child("zoomTimeG").setValue(zoom);

                    databaseReference.child(id).child("locSwipeG").setValue(locSwipe);
                    databaseReference.child(id).child("locZoomG").setValue(locZoom);

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
                                mode = 1;
                                zoomIn();
                                counter = counterDefault;
                            } else {
                                mode = 0;
                            }
                            if (currentImage < 0) {
                                currentImage = 0;
                            } else if (currentImage > 27) {
                                currentImage = 27;
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
                                if (image.getScaleX() < 5f) {
                                    zoomIn();
                                }
                                counter = counterDefault;
                            } else if (z >= rotationLine) {
                                zoomOut();
                                if (image.getScaleY() == defaultImageY) {
                                    image.setX(defaultX);
                                    image.setY(defaultY);
                                    mode = 0;
                                }
                                counter = counterDefault;
                            } else if (y >= rotationLine) {

                                image.setX(image.getX() - step);

                                counter = counterDefault;
                            } else if (y <= -rotationLine) {

                                image.setX(image.getX() + step);

                                counter = counterDefault;
                            } else if (x >= rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() - step);
                            } else if (x <= -rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() + step);
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
                //randomX = ThreadLocalRandom.current().nextInt(1, 4);
                //randomY = ThreadLocalRandom.current().nextInt(1, 7);
            }

        });
    }

    private void randomSquare() {
        randomX = ThreadLocalRandom.current().nextInt(-3, 2);
        randomY = ThreadLocalRandom.current().nextInt(-3, 2);

        imageLeft = screenWidth / 2 + (screenWidth / 8) * (randomX);
        imageRight = screenWidth / 2 + (screenWidth / 8) * (randomX + 2);
        imageTop = screenHeight / 2 + (screenHeight / 8) * randomY;
        imageBottom = screenHeight / 2 + (screenHeight / 8) * (randomY + 2);

        imageCenterWidth = (int) ((imageLeft + imageRight));
        imageCenterHeight = (int) ((imageRight + imageBottom) / 2 - (image.getHeight() / 2));

        Log.e("center ", " x " + imageCenterWidth);

        image.left = imageLeft;
        image.top = imageTop;
        image.right = imageRight;
        image.bottom = imageBottom;

        image.invalidate();
        image.drawRect = true;

        locZoom = locZoom + Math.abs(randomX) + Math.abs(randomY);
    }

    private void randomSquareOld(int randomX, int randomY) {
        imageLeft = screenWidth / 2 + (screenWidth / 8) * (randomX);
        imageRight = screenWidth / 2 + (screenWidth / 8) * (randomX + 2);
        imageTop = screenHeight / 2 + (screenHeight / 8) * randomY;
        imageBottom = screenHeight / 2 + (screenHeight / 8) * (randomY + 2);

        imageCenterWidth = (int) ((imageLeft + imageRight));
        imageCenterHeight = (int) ((imageRight + imageBottom) / 2 - (image.getHeight() / 2));

        Log.e("center ", " x " + imageCenterWidth);

        image.left = imageLeft;
        image.top = imageTop;
        image.right = imageRight;
        image.bottom = imageBottom;

        image.invalidate();
        image.drawRect = true;
    }


    private void randomSquareZero() {
        image.left = -100;
        image.top = -100;
        image.right = -100;
        image.bottom = -100;

        image.invalidate();
        image.drawRect = true;
    }


    private void zoomOut() {
        image.setScaleX(imageX - 1);
        image.setScaleY(imageY - 1);
        imageX = image.getScaleX();
        imageY = image.getScaleY();
        checkImageBorder();
        zoomText.setText("Zoom: " + (image.getScaleX() - 1));
    }

    private void zoomIn() {
        if (image.getScaleX() == 4) {
            return;
        }
        image.setScaleX(image.getScaleX() + 1);
        image.setScaleY(image.getScaleY() + 1);
        imageX = image.getScaleX();
        imageY = image.getScaleY();
        zoomText.setText("Zoom: " + (image.getScaleX() - 1));
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
        gyroscope.register();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscope.unregister();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        if (eventaction == MotionEvent.ACTION_DOWN) {
            step += 100;

            if (step == 400) {
                step = 100;
            }
        }
        stepText.setText("Korak: " + step / 100);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Session?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
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