package com.example.student.accelerometergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Accelerometer Game
 *
 * @author Dean Vang, Eric Bonsness
 *         4/3/2015
 */

@SuppressWarnings("UnusedDeclaration")
public class Actor extends GameObject {

    public final float MIN_ACCEL_SCALE = 0.01f;
    private Bitmap bitmap;

    private float accelerometerScaleX;
    private float accelerometerScaleY;

    public Actor(Bitmap bitmap, float[] spawn, float accelerometerScaleX, float accelerometerScaleY, boolean solid, float scale ) {
        super(bitmap, spawn[0], spawn[1], solid, scale);

        this.accelerometerScaleX = accelerometerScaleX;
        this.accelerometerScaleY = accelerometerScaleY;

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

}
