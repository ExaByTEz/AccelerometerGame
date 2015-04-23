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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
    private float pxWidth;
    private float pxHeight;
    private boolean winFlag;
    private int densityDpi;
    private float bitmapScale;
    private long timeWhenStopped = 0;
    private int score = -1;


    public WorldView(Context context, MainActivity main, Display display){
        super(context);
        this.main = main;

        winFlag = false; //TODO: We may need to move a lot of these into separate initialization methods, the WorldView constructor is getting too big

        Log.d("Display", "density="+getResources().getDisplayMetrics().density);
        this.bitmapScale = 1/getResources().getDisplayMetrics().density;
        Log.d("Display", "bitmapScale="+this.bitmapScale);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        pxWidth = displayMetrics.widthPixels;
        pxHeight = displayMetrics.heightPixels;
        densityDpi = displayMetrics.densityDpi;
        Log.d("Display", "densityDpi="+densityDpi);
        Log.d("Display", "width="+ pxWidth);
        Log.d("Display", "height="+ pxHeight);
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

        Bitmap wallBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wall);
        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

        zones.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.end_zone), spawn(pxWidth - 75, 600), false, bitmapScale, Obstacle.ObstacleType.START_ZONE)); //Index 0: Start Zone
        zones.add(new Obstacle(BitmapFactory.decodeResource(getResources(), R.drawable.end_zone), spawn(400, 300), false, bitmapScale, Obstacle.ObstacleType.END_ZONE)); //Index 1: End Zone

        //Create player and/or other actors
        actors.add(new Actor(playerBitmap, spawn(zones.get(0).getX(),zones.get(0).getY()), 1, 1, true, bitmapScale)); //Index 0: Player
        constantObstacles.add(new Obstacle(wallBitmap,spawn(250+(wallBitmap.getWidth()*bitmapScale),300),true, bitmapScale,Obstacle.ObstacleType.NONE));
        constantObstacles.add(new Obstacle(wallBitmap,spawn(250,400),true, bitmapScale,Obstacle.ObstacleType.NONE));
        constantObstacles.add(new Obstacle(wallBitmap,spawn(pxWidth/2-200,800),true, bitmapScale,Obstacle.ObstacleType.NONE));
        constantObstacles.add(new Obstacle(wallBitmap,spawn((200)+(wallBitmap.getWidth()),500),true, bitmapScale,Obstacle.ObstacleType.NONE));
        constantObstacles.add(new Obstacle(wallBitmap,spawn(200,500),true, bitmapScale,Obstacle.ObstacleType.NONE));

        //put path array back here
        RectF currentWall;

        if(constantObstacles.size()>0) {
            currentWall=new RectF(constantObstacles.get(0).getHitBox());

            for (int i=1;i<constantObstacles.size();i++) {//does not check for intersection, can be used to draw very long walls by giving two points in order
               if ((constantObstacles.get(i).getHitBox().left==currentWall.left&&constantObstacles.get(i).getHitBox().right==currentWall.right)||(constantObstacles.get(i).getHitBox().top==currentWall.top&&constantObstacles.get(i).getHitBox().bottom==currentWall.bottom)) {
                   currentWall.union(constantObstacles.get(i).getHitBox());
                }
                else{
                   walls.add(currentWall);
                   currentWall=new RectF(constantObstacles.get(i).getHitBox());
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

        //Draw the time using drawText(String text, float x, float y, Paint paint)
        int time = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
        canvas.drawText("Par Time:" + PAR_TIME, 15, 35, text);
        canvas.drawText("Time: " + time, 15, 35+text.getTextSize(), text);
        if(winFlag){
            score = (score < 0 ? (PAR_TIME - time > 0 ? PAR_TIME - time : 0): score);
            canvas.drawText("Level Clear", pxWidth /2, pxHeight /2,text);
            canvas.drawText("Score: " + score, pxWidth / 2, pxHeight / 2 + text.getTextSize(), text);
            endLevel();
        }
    }

    /**
     * Render all actors stored in the actors ArrayList
     * @param canvas - The canvas to draw the actors on :Canvas
     */
    public void renderActors(Canvas canvas){
        if(!actors.isEmpty()){
            Log.d("Coordinates", "(x,y)=(" + actors.get(0).getHitBox().left + "," + actors.get(0).getHitBox().top+")");
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
                            break;
                        }
                    }
                    if (collision) {//walls.get(0).quickReject(roundedHitBox)
                        Log.d("Collision", "Collision Successful with Complex Region");
                        actors.get(i).translate(-oldX, -oldY);
                    } else {
                        for (int j = i + 1; j < actors.size(); j++) {
                            if (actors.get(i).isIntersecting(actors.get(j))) {
                                //actors.get(i).translate(-oldX,-oldY);//for now, just stop them
                                //collision = true;
                                Log.d("Collision", "Collision Successful with Actor");
                                actors.get(i).translate(-oldX, -oldY);
                                break;
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

    private void endLevel(){
        Log.d("zones", "won level");
        changeThreadState(false);
    }

    public boolean getWinFlag(){
        return winFlag;
    }

}
