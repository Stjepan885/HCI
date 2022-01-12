package com.example.myapplication;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

public class GyroscopeTestSession extends AppCompatActivity {

    private Gyroscope gyroscope;
    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8,
            R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12,
            R.drawable.a13, R.drawable.a14};
    private int currentImage = 0;
    private int mode = 0; // 0 - swipe 1 - zoom
    private int ret = 0;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;

    private final int counterDefault = 6;
    private final int rotationLine = 2;
    private int counter = 5;

    private ProgressBar prog;

    private boolean drawSquareFlag = true;

    private DrawImageView image;

    private int nbOfTestsSwipe = 10, nbOfTestsZoom = 100;
    private int randomTest = 0, randomSwipeNumber;

    private TextView nbImage;

    private float imageLeft, imageRight, imageTop, imageBottom;

    private int randomX, randomY;

    private int[] imageZoomArrayX = {1750, 750, -500, -1750};
    private int[] imageZoomArrayY = {3750, 2500, 1250, 0, -1250, -2500, -3750};

    private int testCounter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_test_session);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //gyroAlg = new GyroscopeAlg();
        gyroscope = new Gyroscope(this);


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
        defaultY = image.getY();

        randomTest = 0;


        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float x, float y, float z) {

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

                            Log.e("dimenzije", image.getX() + " x " + image.getY() + " y " + image.getScaleX() + " x  scale" + image.getScaleY() + " y");
                            Log.e("dimenzije", imageLeft + " left " + imageRight + " right " + imageTop + " Top " + imageBottom + " Bottom");
                            //Log.e(" " + image.getHeight() , " ");
                            //Log.e(" " + image.getWidth() , " ");
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

                //check image dimensions


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
        }
        if (scale == 3) {
            if (image.getY() > 2000) {
                image.setY(2000);
            }
            if (image.getX() > 1250) {
                image.setX(1250);
            }
        }
        if (scale == 4) {
            if (image.getY() > 2250) {
                image.setY(2250);
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