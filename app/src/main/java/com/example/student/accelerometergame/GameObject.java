package com.example.student.accelerometergame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

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
    private float bitmapScale;

    public GameObject(){}

    /**
     *
     * @param bitmap - Bitmap of the GameObject :Bitmap
     * @param x - Left x coordinate to place the bitmap :float
     * @param y - Left y coordinate to place the bitmap :float
     * @param solid - Should the GameObject be treated as a solid :boolean
     * @param bitmapScale - Scale to draw the bitmap :float
     */
    public GameObject(Bitmap bitmap, float x, float y, boolean solid, float bitmapScale){
        //localImage.setWidth((int) (localImage.getWidth()*BITMAP_SCALE+.5f));

        //x = x*BITMAP_SCALE;
        //y = y*BITMAP_SCALE;

        this.solid = solid;
        this.bitmapScale = bitmapScale;

        this.bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*bitmapScale),(int)(bitmap.getHeight()*bitmapScale),false);
        //hitBox = new RectF(float left, float top, float right, float bottom)
        hitBox = new RectF(x,y, x+(this.bitmap.getWidth()),y+(this.bitmap.getHeight()));//new RectF((x - (bitmap.getWidth() / 2)),bitmap.getHeight(), bitmap.getWidth(),(y - (bitmap.getHeight() / 2)));
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
        this.bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*bitmapScale),(int)(bitmap.getHeight()*bitmapScale),false);
        //TODO:Update hitBox to scale to new bitmap
    }

    /**
     * Get the center x coordinate
     * @return The center x coordinate :float
     */
    public float getX(){
        return hitBox.left;
    }

    /**
     * Set the x coordinate
     * @param x - Value to set x coordinate :float
     */
    public void setX(float x){
        hitBox.offsetTo(x,hitBox.top);
        //this.x=x;
    }

    /**
     * Get the center y coordinate
     * @return The center y coordinate :float
     */
    public float getY(){
        return hitBox.top;
    }

    /**
     * Set the y coordinate
     * @param y - Value to set y coordinate :float
     */
    public void setY(float y){
        hitBox.offsetTo(hitBox.left,y);
        //this.y=y;
    }

    /**
     * Translate the actor by a set value
     * @param x - Value to add to x coordinate :float
     * @param y - Value to add to y coordinate :float
     */
    public void translate(float x, float y){
        hitBox.offset(x, y);
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
        testing.setAlpha(25);
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

    public boolean isIntersecting(RectF other){//also checks if one contains the other
        return RectF.intersects(other,this.hitBox);//|| hitBox.contains(other.getHitBox())||other.getHitBox().contains(this.hitBox)
    }

    /**
     * Checks if a point is intersecting a RectF
     * @param point - The point to check for a collision :PointF
     * @param other - The RectF to check a collision with :RectF
     * @return If the point collides with the RectF
     */
    public boolean pointIsIntersecting(PointF point, RectF other){//also checks if one contains the other
        return RectF.intersects(other,new RectF(point.x, point.y, point.x, point.y));//|| hitBox.contains(other.getHitBox())||other.getHitBox().contains(this.hitBox)
    }

    public void destroyGameObject(){

    }

    public int getWidth(){
        return (int)(hitBox.right-hitBox.left);
    }

    public int getHeight(){
        return (int)(hitBox.bottom-hitBox.top);
    }

}
