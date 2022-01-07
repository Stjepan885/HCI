package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GyroscopeSession extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope_session);

        gyroAlg = new GyroscopeAlg();
        gyroscope = new Gyroscope(this);

        Button button = (Button) findViewById(R.id.button);
        TextView nb = (TextView) findViewById(R.id.textView2);

        image = findViewById(R.id.imageView);

        imageX = image.getScaleX();
        imageY = image.getScaleY();
        defaultImageX = imageX;
        defaultImageY = imageY;
        defaultX = image.getX();
        defaultY = image.getX();

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float x, float y, float z) {
                nb.setText("right " + y);
                switch (mode){
                    case 0:
                        ret = gyroAlg.movementSwipe(x,y,z);
                        if (ret == -1){
                            mode = 1;
                            zoomIn();
                            break;
                        }else{
                            currentImage = ret;
                            image.setImageResource(images[currentImage]);
                        }
                        break;
                    case 1:
                        ret = gyroAlg.movement(x,y,z);
                        switch (ret){
                            case 1:
                                image.setX(image.getX() + 250);
                                break;
                            case 2:
                                image.setX(image.getX() - 250);
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