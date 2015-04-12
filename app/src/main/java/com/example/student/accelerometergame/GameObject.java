package com.example.student.accelerometergame;

import android.util.Log;
import android.widget.ImageView;

/**
 * Accelerometer Game
 *
 * @author Dean Vang
 *         4/3/2015
 */
public class GameObject {
    private boolean solid;
    private float[] hitBox;
    private ImageView view;
    private boolean useAccelerometer;
    private boolean isHostile;

    public GameObject(){}

    public GameObject(ImageView view, boolean useAccelerometer, boolean solid){
        this.solid = solid;
        this.view = view;
        this.useAccelerometer = useAccelerometer;
    }

    public GameObject(ImageView view, int imgId, boolean useAccelerometer, boolean solid){
        this.solid = solid;
        this.view = view;
        this.useAccelerometer = useAccelerometer;

        view.setImageResource(imgId);
        view.setScaleType(ImageView.ScaleType.CENTER);
    }

    public final void setCoordinates(ImageView view, float x, float y){
        view.setX(x);
        view.setY(y);
    }

    public final boolean getSolid(){
        return solid;
    }

    public final ImageView getView(){
        return view;
    }

    public final boolean getUseAccelerometer(){
        return useAccelerometer;
    }

    public final float[] getHitBox(){
        return hitBox;
    }

    public boolean validPosition(){
        return true;
    }

    public float[] fixPosition(){
        return null;
    }

    public void move(float x, float y){
        view.setX(view.getX()+x);
        view.setY(view.getY()+y);
        //Log.d("Accel Objects", "pos(x,y): (" + view.getX() + "," + view.getY() + ")");
    }

    public void remove(){

    }

}
