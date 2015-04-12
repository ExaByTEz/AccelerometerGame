package com.example.student.accelerometergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Accelerometer Game
 *
 * @author Dean Vang
 *         4/3/2015
 */
public class Actor extends GameObject {

    public final float MIN_SCALE = 0.01f;
    private Bitmap bitmap;
    private float x;
    private float y;
    private float accelerometerScaleX;
    private float accelerometerScaleY;

    public Actor(Bitmap bitmap, float x, float y, float accelerometerScaleX, float accelerometerScaleY, boolean solid ) {
        //super(view, accelerometerScaleX, solid);
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.accelerometerScaleX = accelerometerScaleX;
        this.accelerometerScaleY = accelerometerScaleY;

    }

    /**
     * Get the bitmap resource
     * @return The bitmap resource :Bitmap
     */
    public Bitmap getBitmap(){
        return bitmap;
    }

    /**
     * Set the image to draw
     * @param bitmap - The bitmap resource :Bitmap
     */
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    /**
     * Get the x coordinate
     * @return The x coordinate :float
     */
    public float getX(){
        return x;
    }

    /**
     * Set the x coordinate
     * @param x - Value to set x coordinate :float
     */
    public void setX(float x){
        this.x = x;
    }

    /**
     * Get the y coordinate
     * @return The y coordinate :float
     */
    public float getY(){
        return y;
    }

    /**
     * Set the y coordinate
     * @param y - Value to set y coordinate :float
     */
    public void setY(float y){
        this.y = y;
    }

    /**
     * Get the accelerometer x scale
     * @return The scale to multiply the accelerometer x value by :float
     */
    public float getAccelerometerScaleX(){
        return accelerometerScaleX;
    }

    /**
     * Set the accelerometer x scale
     * @param accelerometerScaleX - Value to set x scale :float
     */
    public void setAccelerometerScaleX(float accelerometerScaleX){
        this.accelerometerScaleX = accelerometerScaleX;
    }

    /**
     * Get the accelerometer y scale
     * @return The scale to multiply the accelerometer y value by :float
     */
    public float getAccelerometerScaleY(){
        return accelerometerScaleY;
    }

    /**
     * Set the accelerometer y scale
     * @param accelerometerScaleY - Value to set y scale :float
     */
    public void setAccelerometerScaleY(float accelerometerScaleY){
        this.accelerometerScaleY = accelerometerScaleY;
    }

    /**
     * Translate the actor by a set value
     * @param x - Value to add to x coordinate :float
     * @param y - Value to add to y coordinate :float
     */
    public void translate(float x, float y){
        this.x += x;
        this.y += y;
    }

    /**
     * Draw the actor on the given canvas
     * @param canvas - The canvas to draw the actor on
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }

}
