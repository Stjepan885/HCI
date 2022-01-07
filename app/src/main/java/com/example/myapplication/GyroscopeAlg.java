package com.example.myapplication;

import android.util.Log;

public class GyroscopeAlg {

    private int counter = 10;
    private float accSumX = 0, accSumY = 0, accSumZ = 0;
    private int currentImage = 0;
    private boolean set = false;
    private int counterTwo = 2;
    private final int counterDefault = 8;
    private final float rotationLine = 2f;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;

    public int movement(float x, float y, float z){
        int ret = 0;

        // right left

        if (counter == 0) {
            if (y >= rotationLine) {
                counter = counterDefault;
                ret = 1;
            } else if (y <= -rotationLine) {
                counter = counterDefault;
                ret = 2;
            }

            // up down
            else if (x >= rotationLine) {
                counter = counterDefault;
                ret = 3;
            } else if (x <= -rotationLine) {
                counter = counterDefault;
                ret = 4;
            } else if (z >= rotationLine) {
                counter = counterDefault;
                ret = 5;
            } else if (z <= -rotationLine) {
                counter = counterDefault;
                ret = 6;
            }
        }

        if (counter > 0){
            counter--;
        }

        return ret;
    }

    public int movementSwipe(float x, float y, float z) {
        int ret = 0;

        // swipe left right
        if (y >= rotationLine && counter == 0){
            currentImage += 1;
            counter = 10;
        }
        if (y <= -rotationLine && counter == 0){
            currentImage -= 1;
            counter = 10;
        }

        // 5 images swipe
        if (x >= rotationLine && counter == 0){
            currentImage -= 5;
            counter = 10;
        }
        if (x <= -rotationLine && counter == 0){
            currentImage += 5;
            counter = 10;
        }

        //zoom
        if (z <= rotationLine && counter == 0){
            return -1;
        }


        if (currentImage < 0){
            currentImage = 0;
        }else if (currentImage > 13){
            currentImage = 13;
        }

        if (counter > 0) {
            counter--;
        }

        return currentImage;
    }
}
