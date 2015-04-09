package com.example.student.accelerometergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Accelerometer Game
 *
 * @author Dean Vang
 *         4/3/2015
 */
public class Actor extends Objects{

    private Bitmap bitmap;
    private float x;
    private float y;
    private float accelerometerScaleX;
    private float accelerometerScaleY;
    public final float MIN_SCALE = 0.01f;

    public Actor(Bitmap bitmap, float x, float y, float accelerometerScaleX, float accelerometerScaleY, boolean solid ) {
        //super(view, accelerometerScaleX, solid);
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.accelerometerScaleX = accelerometerScaleX;
        this.accelerometerScaleY = accelerometerScaleY;

    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public float getX(){
        return x;
    }

    public void setX(float x){
        this.x = x;
    }

    public float getY(){
        return y;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getAccelerometerScaleX(){
        return accelerometerScaleX;
    }

    public void setAccelerometerScaleX(float accelerometerScaleX){
        this.accelerometerScaleX = accelerometerScaleX;
    }

    public float getAccelerometerScaleY(){
        return accelerometerScaleY;
    }

    public void setAccelerometerScaleY(float accelerometerScaleY){
        this.accelerometerScaleY = accelerometerScaleY;
    }

    public void translate(float x, float y){
        this.x += x;
        this.y += y;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }



    //public Actor(ImageView view, int imgId, boolean accelerometerScaleX, boolean solid){
    //    super(view, imgId, accelerometerScaleX, solid);
    //}


}
