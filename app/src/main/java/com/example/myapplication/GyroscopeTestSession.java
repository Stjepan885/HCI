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
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6};

    private int currentImage = 0, squareImage;
    private int mode = 0; // 0 - swipe 1 - zoom

    private final int counterDefault = 7;
    private final int rotationLine = 2;
    private int counter = 7;
    private int testCounterDefault = 20;
    private int testCounter = testCounterDefault;
    private int randomTest = 0, randomSwipeNumber;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;
    private float imageLeft, imageRight, imageTop, imageBottom;
    private int randomX, randomY;

    private boolean size = true;
    private int screenWidth, screenHeight;

    private int imageCenterWidth, imageCenterHeight;

    private int step = 200;

    private int locSwipe = 0, locZoom = 0;

    StringBuilder swipeTimeArray = new StringBuilder();
    StringBuilder swipeLocArray = new StringBuilder();
    StringBuilder zoomTimeArray = new StringBuilder();
    StringBuilder zoomLocArray = new StringBuilder();

    public String id;

    private int imageCounterError = 0, zoomCounter = 0;

    private long tmpLoadingStart, tmpLoadingEnd, tmpLoading;
    private boolean loading = true;

    private long jedan, dva;
    boolean a = true;

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


        swipeTimeArray.append("#");
        swipeLocArray.append("#");
        zoomTimeArray.append("#");
        zoomLocArray.append("#");

        start();

        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);
        id = prefs.getString("id", null);

        jedan = System.currentTimeMillis();


        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float x, float y, float z) {

                if (counter == 0 && a == true){
                    Log.e("bla", "bla " + (System.currentTimeMillis()-jedan));
                    a = false;
                }

                if (counter == counterDefault - 1) {
                    tmpLoadingStart = System.currentTimeMillis();
                    loading = true;
                }

                if (counter == 0 && loading == true) {
                    tmpLoadingEnd = System.currentTimeMillis() - tmpLoadingStart;
                    Log.e("time", "" + tmpLoadingEnd);
                    tmpLoading += tmpLoadingEnd;
                    loading = false;
                }

                if (testCounter != 0) {
                    if (randomTest == 0) {
                        randomSwipeNumber = ThreadLocalRandom.current().nextInt(0, 19);
                        nbImage.setText(" " + (randomSwipeNumber + 1));
                        randomTest = 3;
                        startTime1 = System.currentTimeMillis();
                        int loc;
                        if (randomSwipeNumber > currentImage){
                            loc = randomSwipeNumber - currentImage;
                        }else{
                            loc = currentImage - randomSwipeNumber;
                        }

                        locSwipe = locSwipe + loc;
                        databaseReference.child(id).child("swipeLocArrayG1").child(String.valueOf(testCounterDefault-testCounter)).setValue(loc);
                        swipeLocArray.append(String.valueOf(loc));
                        swipeLocArray.append("#");
                        imageCounterError = 0;
                    } else if (randomTest == 3) {
                        if (currentImage == randomSwipeNumber) {
                            randomSquare();
                            squareImage = currentImage;
                            randomTest = 4;
                            mode = 2;
                            endTime1 = System.currentTimeMillis();
                            long diff = endTime1 - startTime1 - (tmpLoading);
                            Log.e("tmp loading", " " + tmpLoading);
                            tmpLoading = 0;
                            swipeTime += diff;
                            diff = diff /100;

                            databaseReference.child(id).child("swipeTimeArrayG1").child(String.valueOf(testCounterDefault-testCounter)).setValue(diff);
                            swipeTimeArray.append(String.valueOf(diff));
                            swipeTimeArray.append("#");
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
                                    endTime2 = System.currentTimeMillis();
                                    long dif = endTime2 - startTime2 - (tmpLoading);
                                    Log.e("tmp loading", " " + tmpLoading);
                                    tmpLoading = 0;
                                    zoomTime += dif;
                                    dif = dif / 100;
                                    zoomTimeArray.append(dif);
                                    zoomTimeArray.append("#");
                                    databaseReference.child(id).child("zoomTimeArrayG1").child(String.valueOf(testCounterDefault-testCounter)).setValue(dif);
                                    databaseReference.child(id).child("imageCounterErrorG").child(String.valueOf(testCounterDefault - testCounter)).setValue(imageCounterError);
                                    databaseReference.child(id).child("zoomCounterG").child(String.valueOf(testCounterDefault - testCounter)).setValue(zoomCounter);
                                    zoomCounter = 0;
                                    testCounter--;
                                    testsDone.setText((testCounterDefault - testCounter) + "/20");
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
                    end();
                }

                switch (mode) {
                    case 0:
                        if (counter == 0) {
                            if (y >= rotationLine) {
                                currentImage += 1;
                                counter = counterDefault;
                                imageCounterError++;
                            } else if (y <= -rotationLine) {
                                currentImage -= 1;
                                counter = counterDefault;
                                imageCounterError++;
                            } else if (x >= rotationLine) {
                                currentImage -= 5;
                                counter = counterDefault;
                                imageCounterError++;
                            } else if (x <= -rotationLine) {
                                currentImage += 5;
                                counter = counterDefault;
                                imageCounterError++;
                            } else if (z <= -rotationLine) {
                                mode = 1;
                                zoomIn();
                                counter = counterDefault;
                            } else {
                                mode = 0;
                            }
                            if (currentImage < 0) {
                                currentImage = 0;
                            } else if (currentImage > 19) {
                                currentImage = 19;
                            }
                        }

                        if (counter > 0) {
                            counter--;
                        }
                        image.setImageResource(images[currentImage]);

                        nb.setText((currentImage + 1) + "/20");
                        break;
                    case 1:
                        if (counter == 0) {
                            if (z <= -rotationLine) {
                                if (image.getScaleX() < 5f) {
                                    zoomIn();
                                }
                                counter = counterDefault;
                                zoomCounter++;
                            } else if (z >= rotationLine) {
                                zoomOut();
                                if (image.getScaleY() == defaultImageY) {
                                    image.setX(defaultX);
                                    image.setY(defaultY);
                                    mode = 0;
                                }
                                counter = counterDefault;
                                zoomCounter++;
                            } else if (y >= rotationLine) {
                                image.setX(image.getX() - step);
                                counter = counterDefault;
                                zoomCounter++;
                            } else if (y <= -rotationLine) {
                                image.setX(image.getX() + step);
                                counter = counterDefault;
                                zoomCounter++;
                            } else if (x >= rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() - step);
                                zoomCounter++;
                            } else if (x <= -rotationLine) {
                                counter = counterDefault;
                                image.setY(image.getY() + step);
                                zoomCounter++;
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
        randomX = ThreadLocalRandom.current().nextInt(-3, 2);
        randomY = ThreadLocalRandom.current().nextInt(-2, 1);

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

        zoomLocArray.append(randomX);
        zoomLocArray.append("$");
        zoomLocArray.append(randomY);
        zoomLocArray.append("#");
        locZoom = locZoom + Math.abs(randomX) + Math.abs(randomY);
        databaseReference.child(id).child("zoomLocArrayGX").child(String.valueOf(testCounterDefault-testCounter)).setValue(String.valueOf(randomX));
        databaseReference.child(id).child("zoomLocArrayGY").child(String.valueOf(testCounterDefault-testCounter)).setValue(String.valueOf(randomY));
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

    private void start(){
        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);

        testCounter = prefs.getInt("testCounterG", testCounterDefault);

        currentImage = prefs.getInt("currentImageG", 0);
        image.setImageResource(images[currentImage]);

        squareImage = prefs.getInt("squareImageG", 50);
        if (squareImage == currentImage){
            randomX = prefs.getInt("randomXG", 0);
            randomY = prefs.getInt("randomYG", 0);
           // randomSquareOld(randomX, randomY);
        }

        randomSwipeNumber = prefs.getInt("randomSwipeNumberG",0);
        nbImage.setText(" " + (randomSwipeNumber + 1));
        testsDone.setText((testCounterDefault - testCounter) + "/20");

        swipeTime = prefs.getLong("swipeG", 0);
        zoomTime = prefs.getLong("zoomG", 0);

        randomTest = prefs.getInt("randomTestG", 0);

        if (testCounter == 0){
            Log.e("bok", "bok");
            finish();
        }else if (testCounter == testCounterDefault){
            return;
        }

        int rndTest = prefs.getInt("randomTestG", 0);

        if (rndTest == 0){
            randomTest = 0;
            return;
        }

        swipeTimeArray.setLength(0);
        swipeTimeArray.append(prefs.getString("swipeTimeArrayG", "#"));
        swipeLocArray.setLength(0);
        swipeLocArray.append(prefs.getString("swipeLocArrayG", "#"));
        zoomTimeArray.setLength(0);
        zoomTimeArray.append(prefs.getString("zoomTimeArrayG", "#"));
        zoomLocArray.setLength(0);
        zoomLocArray.append(prefs.getString("zoomLocArrayG", "#"));


        if (rndTest == 3){
            randomTest = 3;
            startTime1 = System.currentTimeMillis();
            return;
        }else if (rndTest == 4){
            randomTest = 4;
            if (squareImage == currentImage){
                randomX = prefs.getInt("randomXG", 0);
                randomY = prefs.getInt("randomYG", 0);
                randomSquareOld(randomX, randomY);
            }
            startTime2 = System.currentTimeMillis();
        }
    }

    private void end1() {
        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("randomTestG", randomTest);
        editor.putInt("randomSwipeNumberG", randomSwipeNumber);
        editor.putInt("squareImageG", squareImage);
        editor.putInt("currentImageG", currentImage);
        editor.putInt("testCounterG", testCounter);
        editor.putInt("randomXG", randomX);
        editor.putInt("randomYG", randomY);

        editor.putLong("swipeG", swipeTime);
        editor.putLong("zoomG", zoomTime);


        String s = swipeTimeArray.toString();
        String ss = swipeLocArray.toString();

        String sL = zoomTimeArray.toString();
        String ssL = zoomLocArray.toString();

        editor.putString("swipeTimeArrayG", s);
        editor.putString("swipeLocArrayG", ss);
        editor.putString("zoomTimeArrayG", sL);
        editor.putString("zoomLocArrayG", ssL);


        editor.apply();


        //end of test
        finish();
    }

    private void end() {
        prefs = getSharedPreferences("shared_pref_name", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("swipeG", swipeTime);
        editor.putLong("zoomG", zoomTime);
        editor.putInt("testCounter", testCounter);
        editor.apply();

        String id = prefs.getString("id", null);

        String swipe = String.valueOf(swipeTime);
        String zoom = String.valueOf(zoomTime);

        databaseReference.child(id).child("swipeTimeG").setValue(swipe);
        databaseReference.child(id).child("zoomTimeG").setValue(zoom);

        swipeLocArray.append("%");
        swipeTimeArray.append("%");
        zoomLocArray.append("%");
        zoomTimeArray.append("%");

        databaseReference.child(id).child("locSwipeG").setValue(locSwipe);
        databaseReference.child(id).child("locZoomG").setValue(locZoom);

        String s = swipeTimeArray.toString();
        String ss = swipeLocArray.toString();
        databaseReference.child(id).child("swipeTimeArrayG").setValue(s);
        databaseReference.child(id).child("swipeLocArrayG").setValue(ss);

        String sL = zoomTimeArray.toString();
        String ssL = zoomLocArray.toString();
        databaseReference.child(id).child("zoomTimeArrayG").setValue(sL);
        databaseReference.child(id).child("zoomLocArrayG").setValue(ssL);

        //end of test
        Intent intent = new Intent(GyroscopeTestSession.this, EndOfTest.class);
        startActivity(intent);
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
                                end1();

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