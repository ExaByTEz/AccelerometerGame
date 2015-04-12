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
public class GameObject {
    private Bitmap bitmap;
    private boolean solid;
    private float x;
    private float y;

    public GameObject(){}
    public GameObject(Bitmap bitmap, float x, float y, boolean solid){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.solid = solid;
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

    public boolean isSolid(){
        return solid;
    }

    public boolean isValidPosition(){
        return true;
    }


    public void destroyGameObject(){

    }

}
