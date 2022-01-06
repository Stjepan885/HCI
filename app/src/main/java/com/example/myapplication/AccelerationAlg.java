package com.example.myapplication;

import android.widget.ImageView;
import android.widget.TextView;

public class AccelerationAlg {

    private int counter = 10, zCounter = 10, yCounter3 = 0;
    private float accSumX = 0, accSumY = 0, accSumZ = 0;
    private int currentImage = 0;
    private boolean set = false;
    private int counterTwo = 2;

    private float imageX, imageY, defaultImageX, defaultImageY, defaultX, defaultY;

    public int movement(float x, float y, float z) {
        int ret = 0;

        if (counterTwo > 0 && set == true){
            counterTwo--;
        }
        if (accSumX >= 3 || accSumX <= -3f || y >= 3 || y <= -3f || z >= 3 || z <= -3f) {
            if (counter == 0 && set == false) {
                counterTwo = 4;
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

        // right left
        accSumX = accSumX - (accSumX * 0.2f);
        if (x >= 1.5f && counterTwo == 0) {
            setValues();
            ret = 1;
        }else if (x <= -1.5f && counterTwo == 0) {
            setValues();
            ret = 2;
        }

        // up down
        accSumY = accSumY - (accSumY * 0.2f);
        if (accSumY >= 1.5f && counterTwo == 0) {
            setValues();
            ret = 3;
        }
        if (accSumY <= -1.5f && counterTwo == 0) {
            setValues();
            ret = 4;
        }

        //zoom
        accSumZ = accSumZ - (accSumZ * 0.2f);
        if (accSumZ >= 2f && counterTwo == 0) {
            setValues();
            ret = 5;
        }
        if (accSumZ <= -2f && counterTwo == 0) {
            ret = 6;
            setValues();
        }

        if (counter > 0) {
            counter--;
        }
        return ret;
    }

    private void setValues() {
        counter = 10;
        set = false;
        counterTwo = 3;
    }

    public int movementSwipe(float x, float y, float z) {

        accSumX += x;
        accSumY += y;
        if (zCounter != 0) {
            zCounter--;
        } else {
            accSumZ += z;
        }

        // swipe
        accSumX = accSumX - (accSumX * 0.2f);
        if (accSumX >= 2f && counter == 0) {
            currentImage += 1;
            counter = 15;

        } else if (accSumX <= -2f && counter == 0) {
            currentImage -= 1;
            counter = 15;
        }

        //swipe 5
        accSumY = accSumY - (accSumY * 0.2f);
        if (accSumY >= 2f && counter == 0) {
            currentImage += 5;
            counter = 15;

        }
        if (accSumY <= -2f && counter == 0) {
            currentImage -= 5;
            counter = 15;
        }


        if (currentImage < 0){
            currentImage = 0;
        }else if (currentImage > 13){
            currentImage = 13;
        }

        if (counter > 0) {
            counter--;
        }

        accSumZ = accSumZ - (accSumZ * 0.2f);
        if (accSumZ >= 3 && counter == 0) {
            counter  = 10;
            return -1;
        }

        return currentImage;
    }

    public void setDefaultX(float defaultX) {
        this.defaultX = defaultX;
    }

    public void setDefaultY(float defaultY) {
        this.defaultY = defaultY;
    }

    public void setImageX(float imageX) {
        this.imageX = imageX;
    }

    public void setImageY(float imageY) {
        this.imageY = imageY;
    }
}

