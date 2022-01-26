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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ThreadLocalRandom;

public class AccelerometerSession extends AppCompatActivity {

    private long startTime1, startTime2, endTime1, endTime2;
    private long swipeTime = 0l, zoomTime = 0l;

    private Accelerometer accelerometer;

    private ProgressBar prog;
    private DrawImageView image;
    private TextView nb;
    private TextView nbImage, testsDone, zoomText, stepText;

    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
            R.drawable.a13, R.drawable.a14};

    private int currentImage = 0, squareImage;
    private int mode = 0; // 0 - swipe 1 - zoom

    private final int counterDefault = 20;
    private int counterTwoDefault = 2;
    private int counter = 10, zCounter = 10, counterTwo = 2;
    private int testCounter = 10;
    private int randomTest = 0, randomSwipeNumber;


    private float imageX, imageY, defaultScaleImageX, defaultScaleImageY, defaultX, defaultY;
    private float imageLeft, imageRight, imageTop, imageBottom;
    private int randomX, randomY, currentRandomX, getCurrentRandomX;

    private float accSumX = 0, accSumY = 0, accSumZ = 0;
    private boolean set = false;

    private int screenWidth, screenHeight;

    private int imageCenterWidth, imageCenterHeight;

    private int step = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_test);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        accelerometer = new Accelerometer(this);

        nb = (TextView) findViewById(R.id.textView2);
        nbImage = (TextView) findViewById(R.id.textView3);
        image = findViewById(R.id.imageView);
        testsDone = findViewById(R.id.textViewTestDone);
        zoomText = findViewById(R.id.textViewZoom);
        stepText = findViewById(R.id.textViewKorak);

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
                    } else if (randomTest == 3) {
                        if (currentImage == randomSwipeNumber) {
                            randomSquare();
                            squareImage = currentImage;
                            randomTest = 4;
                            mode = 2;
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
                    //end of test
                    Intent intent = new Intent(AccelerometerSession.this, MainMenu.class);
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
                            image.setX(image.getX() + step);
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumX <= -1.5f && counterTwo == 0) {
                            image.setX(image.getX() - step);
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumY >= 1.5f && counterTwo == 0) {
                            image.setY(image.getY() - step);
                            counter = counterDefault;
                            set = false;
                            counterTwo = counterTwoDefault;
                        } else if (accSumY <= -1.5f && counterTwo == 0) {
                            image.setY(image.getY() + step);
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
        if (image.getScaleX() == 1) {
            return;
        }
        image.setScaleX(image.getScaleX() - 1);
        image.setScaleY(image.getScaleY() - 1);
        counter = counterDefault;
        checkImageBorder();
        zoomText.setText("Zoom: " + (image.getScaleX() - 1));
    }

    private void zoomIn() {
        if (image.getScaleX() == 4) {
            return;
        }
        image.setScaleX(image.getScaleX() + 1);
        image.setScaleY(image.getScaleY() + 1);
        counter = counterDefault;
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
        accelerometer.register();

    }

    @Override
    protected void onPause() {
        super.onPause();
        accelerometer.unregister();
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