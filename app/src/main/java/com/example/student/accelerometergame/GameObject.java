package com.example.student.accelerometergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

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
    private RectF hitBox;
    private float scale;

    public GameObject(){}
    public GameObject(Bitmap bitmap, float x, float y, boolean solid, float scale){
        //localImage.setWidth((int) (localImage.getWidth()*scale+.5f));

        x=x*scale;
        y = y*scale;

        this.bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*scale),(int)(bitmap.getHeight()*scale),false);

        hitBox=new RectF((x - (this.bitmap.getWidth() / 2)),(y - (this.bitmap.getHeight() / 2)),(x + (this.bitmap.getWidth() / 2)),(y + (this.bitmap.getHeight() / 2)));



        /*
        this.x=x;
        this.y=y;
        this.bitmap=bitmap;
        */
        this.solid = solid;
        this.scale=scale;
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
        this.bitmap = Bitmap.createScaledBitmap(bitmap,(int)hitBox.left,(int)hitBox.top,false);
    }

    /**
     * Get the x coordinate
     * @return The x coordinate :float
     */
    public float getX(){
        return hitBox.centerX();
    }

    /**
     * Set the x coordinate
     * @param x - Value to set x coordinate :float
     */
    public void setX(float x){
        hitBox.offsetTo(x*scale,hitBox.top);
        //this.x=x;
    }

    /**
     * Get the y coordinate
     * @return The y coordinate :float
     */
    public float getY(){
        return hitBox.centerY();
    }

    /**
     * Set the y coordinate
     * @param y - Value to set y coordinate :float
     */
    public void setY(float y){
        hitBox.offsetTo(hitBox.left,y*scale);
        //this.y=y;
    }

    /**
     * Translate the actor by a set value
     * @param x - Value to add to x coordinate :float
     * @param y - Value to add to y coordinate :float
     */
    public void translate(float x, float y){
        hitBox.offset(x * scale, y * scale);
    }

    /**
     * Draw the actor on the given canvas
     * @param canvas - The canvas to draw the actor on
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, hitBox.left, hitBox.top, null);
        /*
        Paint testing=new Paint();//draw a blue bounding box where the bitmap should be
        testing.setColor(Color.BLUE);
        testing.setStrokeWidth(2);
        canvas.drawRect(hitBox,testing);
        */
    }

    public boolean isSolid(){
        return solid;
    }

    public RectF getHitBox(){ return hitBox;}

    public void setHitBox(RectF hitBox){this.hitBox=hitBox;}

    public boolean isValidPosition(){
        return true;
    }

    public boolean isIntersecting(GameObject other){//also checks if one contains the other
        if(other.isSolid()&&this.solid){
            return RectF.intersects(other.getHitBox(),this.hitBox);//|| hitBox.contains(other.getHitBox())||other.getHitBox().contains(this.hitBox)
        }
        return false;
    }

    public void destroyGameObject(){

    }

}
