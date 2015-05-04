package com.example.student.accelerometergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;

import java.util.ArrayList;

/**
 * Accelerometer Game
 *
 * @author Dean Vang, Eric Bonsness
 *         4/6/2015
 */
public class WorldView extends SurfaceView implements SurfaceHolder.Callback{
    private final String TAG = this.getClass().getSimpleName();
    private final int PAR_TIME = 10;

    private WorldViewThread thread;
    private ArrayList<Actor> actors;
    private ArrayList<Obstacle> zones;
    private ArrayList<RectF> walls;
    private ArrayList<Obstacle> constantObstacles;
    private MainActivity main;
    private Chronometer chronometer;
    private Paint text;
    public final float PX_WIDTH;
    public final float PX_HEIGHT;
    private boolean winFlag;
    private int densityDpi;
    public final float BITMAP_SCALE;
    private long timeWhenStopped = 0;
    private int score = -1;
    private int time;


    public WorldView(int level, Context context, MainActivity main, Display display){
        super(context);
        this.main = main;

        winFlag = false; //TODO: We may need to move a lot of these into separate initialization methods, the WorldView constructor is getting too big

        Log.d("Display", "density="+getResources().getDisplayMetrics().density);
        this.BITMAP_SCALE = 1/getResources().getDisplayMetrics().density;
        Log.d("Display", "BITMAP_SCALE="+this.BITMAP_SCALE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        PX_WIDTH = displayMetrics.widthPixels;
        PX_HEIGHT = displayMetrics.heightPixels-50;
        densityDpi = displayMetrics.densityDpi;
        Log.d("Display", "densityDpi="+densityDpi);
        Log.d("Display", "width="+ PX_WIDTH);
        Log.d("Display", "height="+ PX_HEIGHT);
        //Initialize ArrayList of actors
        actors = new ArrayList<>();
        zones = new ArrayList<>();
        walls=new ArrayList<>();
        constantObstacles=new ArrayList<>();

        //Create the simple timer using a chronometer
        chronometer = new Chronometer(context);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        //Create game thread
        thread = new WorldViewThread(this);
        getHolder().addCallback(this);

        new Level(actors, zones, constantObstacles, level, true, this);

       
        //put path array back here
        RectF currentWall;
        boolean doNotExit=true;

        if(constantObstacles.size()>0) {
            currentWall=new RectF(constantObstacles.get(0).getHitBox());

            for (int i=1;i<constantObstacles.size();i++) {//does not check for intersection, can be used to draw very long walls by giving two points in order
               if (((constantObstacles.get(i).getHitBox().left==currentWall.left&&constantObstacles.get(i).getHitBox().right==currentWall.right)||(constantObstacles.get(i).getHitBox().top==currentWall.top&&constantObstacles.get(i).getHitBox().bottom==currentWall.bottom))&&doNotExit) {
                   currentWall.union(constantObstacles.get(i).getHitBox());
                   doNotExit=false;
                }
                else{
                   walls.add(currentWall);
                   currentWall=new RectF(constantObstacles.get(i).getHitBox());
                   doNotExit=true;
               }
                if(i==constantObstacles.size()-1){//we reached the end, add the last region no matter what
                    walls.add(currentWall);
                }
            }
        }

        //Create the paint to render the clock
        text = new Paint();
        text.setColor(Color.BLUE);
        text.setTextSize(25);

        // make the WorldView focusable so it can handle events
        //setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void changeThreadState(boolean running){
        thread.setRunning(running);
        if(running){
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            //timeWhenStopped = 0;
            chronometer.start();
        }else{
            timeWhenStopped = (chronometer.getBase() - SystemClock.elapsedRealtime());
            chronometer.stop();
        }
    }

    public boolean getThreadState(){
        return thread.getRunning();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(thread.getState()==Thread.State.TERMINATED){
            thread = new WorldViewThread(this);
        }
        thread.setRunning(true);
        thread.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while(retry){
            try{
                thread.join();
                retry = false;
            }catch(InterruptedException e){
                Log.e(TAG, TAG+" ERROR: "+e.getMessage());
            }
        }
    }

    /**
     * Renders WorldView with all the actors onto the canvas
     * @param canvas - The canvas to draw the world :Canvas
     */
    public void render(Canvas canvas){
        //Log.d(TAG,"elapsed time:"+(SystemClock.elapsedRealtime()-chronometer.getBase()));

        //Draw the canvas
        canvas.drawColor(Color.WHITE);
    }

    /**
     * Render all actors stored in the actors ArrayList
     * @param canvas - The canvas to draw the actors on :Canvas
     */
    public void renderActors(Canvas canvas){
        if(!actors.isEmpty()){
            //("Coordinates", "(x,y)=(" + actors.get(0).getHitBox().left + "," + actors.get(0).getHitBox().top+")");
            for(int i=0;i<actors.size();i++){ //Iterate through all actors
                if(actors.get(i).getAccelerometerScaleX() > actors.get(i).MIN_ACCEL_SCALE || actors.get(i).getAccelerometerScaleY() > actors.get(i).MIN_ACCEL_SCALE){ //Move the actor if it uses the accelerometer
                    float oldX = -main.getAccelX()*actors.get(i).getAccelerometerScaleX();
                    float oldY = main.getAccelY()*actors.get(i).getAccelerometerScaleY();
                    boolean collision = false;

                    //we need to translate first. this ensure the next movement is tested instead of the current, allowing oldX and oldY to properly move the object back in time, instead of moving it a new direction
                    //the old way created the bug allowing you to tilt the accelerometer before draw allowing the ball to phase through solids
                    actors.get(i).translate(-main.getAccelX() * actors.get(i).getAccelerometerScaleX(), main.getAccelY() * actors.get(i).getAccelerometerScaleY());

                    //only check against those that didn't TODO:Needs to change (if we have time).  Nested loops = Bad For Performance
                    for(RectF wall:walls){
                        if(actors.get(i).isIntersecting(wall)){
                            collision=true;
                            actors.set(i,slideCollision(actors.get(i),wall,oldX,oldY));
                        }
                    }
                    if(!collision) {
                        for (int j = i + 1; j < actors.size(); j++) {
                            if (actors.get(i).isIntersecting(actors.get(j))) {
                                //actors.get(i).translate(-oldX,-oldY);//for now, just stop them
                                //collision = true;
                                Log.d("Collision", "Collision Successful with Actor");
                                actors.set(i, slideCollision(actors.get(i), actors.get(j).getHitBox(), oldX, oldY));
                            }
                        }
                    }
                }
                actors.get(i).draw(canvas); //Draw the actor on the canvas
            }
        }
        if(!zones.isEmpty()){
            for(Obstacle zone : zones){
                //Log.d("Obstacle", "Zone Type:" + obstacle.getObstacleType());
                if(actors.get(0).isIntersecting(zone.getHitBox())){
                    Log.d("Obstacle", "Player is inside of " + zone.getObstacleType());
                    if(zone.getObstacleType() == Obstacle.ObstacleType.END_ZONE && !winFlag){
                        winFlag = true;
                    }
                }
                zone.draw(canvas);
            }
        }
        if(!constantObstacles.isEmpty()){
            for(Obstacle constant:constantObstacles){
                constant.draw(canvas);
            }
        }
        if(!walls.isEmpty()){//sanity check to be removed, simply highlights all regions so we can see their bounding boxes
            for(RectF condensedWalls:walls){
                Paint testing=new Paint();//draw a green bounding box where the path/region should exist
                testing.setColor(Color.BLACK);
                testing.setStrokeWidth(2);
                //testing.setAlpha(25);
                canvas.drawRect(condensedWalls, testing);
            }
        }
    }

    /**
     * Spawn a GameObject relative to the screen density dpi (Removed)
     * @param x - The x coordinate to spawn a GameObject :float
     * @param y - The y coordinate to spawn a GameObject :float
     * @return the scaled spawn location
     */
    private float[] spawn(float x, float y){
        return new float[]{x, y};
    }

    public void renderText(Canvas canvas){
        //Draw the time using drawText(String text, float x, float y, Paint paint)
        if(!winFlag){
            time = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
        }
        canvas.drawText("Par Time:" + PAR_TIME, 15, 35, text);
        canvas.drawText("Time: " + time, 15, 35 + text.getTextSize(), text);
        if(winFlag){
            score = (score < 0 ? (PAR_TIME - time > 0 ? PAR_TIME - time : 0): score);
            Paint textCentered = text;
            textCentered.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Level Clear", PX_WIDTH /2, PX_HEIGHT /2,textCentered);
            canvas.drawText("Score: " + score, PX_WIDTH / 2, PX_HEIGHT / 2 + text.getTextSize(), textCentered);
            canvas.drawText("Touch Screen to Continue", PX_WIDTH / 2, PX_HEIGHT / 2 + 2*text.getTextSize(), textCentered);
            endLevel();
        }
    }

    private void endLevel(){
        Log.d("zones", "won level");
        changeThreadState(false);
        //"Restart" the game to the next level when the screen is touched
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                main.nextLevel();
                return false;
            }
        });
    }

    public boolean getWinFlag(){
        return winFlag;
    }

    private Actor slideCollision(Actor actor,RectF objectHit,float oldX, float oldY){
        RectF xMovementOnly=new RectF(actor.getHitBox());
        xMovementOnly.offset(0,-oldY);
        RectF yMovementOnly=new RectF(actor.getHitBox());
        yMovementOnly.offset(-oldX,0);
        if(RectF.intersects(xMovementOnly,objectHit)){
            actor.translate(-oldX, 0);
        }
        if(RectF.intersects(yMovementOnly,objectHit)){
            actor.translate(0, -oldY);
        }
        return actor;
    }

}
