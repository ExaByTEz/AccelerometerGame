package com.example.student.accelerometergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
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

    private WorldViewThread thread;
    private ArrayList<Actor> actors;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Region> walls;
    private ArrayList<Obstacle> constantObstacles;
    private MainActivity main;
    private Chronometer chronometer;
    private Paint text;
    private float dpWidth;
    private float dpHeight;
    private boolean winFlag;
    private int densityDpi;
    private float bitmapScale;
    private Bitmap wallBitmap;


    public WorldView(Context context, MainActivity main, Display display){
        super(context);
        this.main = main;

        winFlag = false; //TODO: We may need to move a lot of these into separate initialization methods, the WorldView constructor is getting too big

        Log.d("Display", "density="+getResources().getDisplayMetrics().density);
        this.bitmapScale = getResources().getDisplayMetrics().density;
        Log.d("Display", "bitmapScale="+this.bitmapScale);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        dpWidth=displayMetrics.widthPixels;
        dpHeight=displayMetrics.heightPixels;
        densityDpi = displayMetrics.densityDpi;
        Log.d("Display", "densityDpi="+densityDpi);
        Log.d("Display", "width="+dpWidth);
        Log.d("Display", "height="+dpHeight);
        //Initialize ArrayList of actors
        actors = new ArrayList<>();
        obstacles = new ArrayList<>();
        walls=new ArrayList<>();
        constantObstacles=new ArrayList<>();

        //Create the simple timer using a chronometer
        chronometer = new Chronometer(context);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        //Create game thread
        thread = new WorldViewThread(this);
        getHolder().addCallback(this);

        wallBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wall);

        //Create player and/or other actors
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), spawn(50,50), 1, 1, true, bitmapScale)); //Index 0: Player
        /* //Test Objects
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 150, 150, 0.2f,0, true, bitmapScale)); //Index 1: Test object
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 250, 250, 0.5f, 0.5f, true, bitmapScale)); //Index 2: Test object
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 350, 350, 0, 0.6f, true, bitmapScale)); //Index 3: Test object
        */
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball),spawn(300,300), 0, 0, true, bitmapScale)); //Index 4: Test object unaffected by accelerometer
        constantObstacles.add(new Obstacle(wallBitmap,spawn((dpWidth/2)+(wallBitmap.getWidth()*bitmapScale),800),true, bitmapScale,Obstacle.ObstacleType.NONE));
        constantObstacles.add(new Obstacle(wallBitmap,spawn(dpWidth/2,800),true, bitmapScale,Obstacle.ObstacleType.NONE));
        constantObstacles.add(new Obstacle(wallBitmap,spawn((200)+(wallBitmap.getWidth()*bitmapScale),500),true, bitmapScale,Obstacle.ObstacleType.NONE));
        constantObstacles.add(new Obstacle(wallBitmap,spawn(200,500),true, bitmapScale,Obstacle.ObstacleType.NONE));
        //Zones need to be solid to detect collision for now
        obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),R.drawable.end_zone),spawn(400,300),true, bitmapScale, Obstacle.ObstacleType.END_ZONE));
        obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),R.drawable.end_zone),spawn(dpWidth-75,600),true, bitmapScale, Obstacle.ObstacleType.START_ZONE));

        //put path array back here
        Path wallPath=new Path();
        RectF pathBounds=new RectF();
        int increment=0;

        if(constantObstacles.size()>0) {
            wallPath.addRect(constantObstacles.get(0).getHitBox(), Path.Direction.CCW);

            for (int i=1;i<constantObstacles.size();i++) {
                wallPath.computeBounds(pathBounds,true);
               if ((constantObstacles.get(i).getHitBox().left==pathBounds.left&&constantObstacles.get(i).getHitBox().right==pathBounds.right)||(constantObstacles.get(i).getHitBox().top==pathBounds.top&&constantObstacles.get(i).getHitBox().bottom==pathBounds.bottom)) {
                    wallPath.addRect(constantObstacles.get(i).getHitBox(),Path.Direction.CCW);
                }
                else{
                   walls.add(new Region());
                   walls.get(increment).setPath(wallPath,new Region(0,0,(int)dpWidth,(int)dpHeight));
                   wallPath.reset();
                   wallPath.addRect(constantObstacles.get(i).getHitBox(),Path.Direction.CCW);
                   increment++;
               }
                if(i==constantObstacles.size()-1){//we reached the end, add the last region no matter what
                    walls.add(new Region());
                    walls.get(increment).setPath(wallPath,new Region(0,0,(int)dpWidth,(int)dpHeight));
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
        canvas.drawText("Time: " + ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000), 15, 35, text);
    }

    /**
     * Render all actors stored in the actors ArrayList
     * @param canvas - The canvas to draw the actors on :Canvas
     */
    public void renderActors(Canvas canvas){
        if(!actors.isEmpty()){
            for(int i=0;i<actors.size();i++){ //Iterate through all actors
                if(actors.get(i).getAccelerometerScaleX() > actors.get(i).MIN_ACCEL_SCALE || actors.get(i).getAccelerometerScaleY() > actors.get(i).MIN_ACCEL_SCALE){ //Move the actor if it uses the accelerometer
                    float oldX = -main.getAccelX()*actors.get(i).getAccelerometerScaleX();
                    float oldY = main.getAccelY()*actors.get(i).getAccelerometerScaleY();
                    boolean collision = false;

                    //we need to translate first. this ensure the next movement is tested instead of the current, allowing oldX and oldY to properly move the object back in time, instead of moving it a new direction
                    //the old way created the bug allowing you to tilt the accelerometer before draw allowing the ball to phase through solids
                    actors.get(i).translate(-main.getAccelX() * actors.get(i).getAccelerometerScaleX(), main.getAccelY() * actors.get(i).getAccelerometerScaleY());

                    //only check against those that didn't TODO:Needs to change (if we have time).  Nested loops = Bad For Performance
                    Rect roundedHitBox=new Rect();
                    actors.get(i).getHitBox().round(roundedHitBox);
                    for(Region wall:walls){
                        if(!wall.quickReject(roundedHitBox)){
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
        if(!obstacles.isEmpty()){
            for(Obstacle obstacle : obstacles){
                //Log.d("Obstacle", "Zone Type:" + obstacle.getObstacleType());
                if(actors.get(0).isIntersecting(obstacle)){
                    Log.d("Obstacle", "Player is inside of " + obstacle.getObstacleType());
                    if(obstacle.getObstacleType() == Obstacle.ObstacleType.END_ZONE && !winFlag){
                        winFlag = true;
                        endLevel();
                    }
                }
                obstacle.draw(canvas);
            }
        }
        if(!walls.isEmpty()){//sanity check to be removed, simply highlights all regions so we can see their bounding boxes
            for(Region condensedWalls:walls){
                Paint testing=new Paint();//draw a green bounding box where the path/region should exist
                testing.setColor(Color.GREEN);
                testing.setStrokeWidth(2);
                testing.setAlpha(25);
                canvas.drawPath(condensedWalls.getBoundaryPath(), testing);
            }
        }
    }

    /**
     * Spawn a GameObject relative to the screen density dpi
     * @param x - The x coordinate to spawn a GameObject :float
     * @param y - The y coordinate to spawn a GameObject :float
     * @return the scaled spawn location
     */
    private float[] spawn(float x, float y){
        Log.d("spawn", "Spawning (x, y): (" + x+","+y+") " );
        Log.d("spawn", "pxToDp(" + x + "*" + bitmapScale + ")");
        Log.d("spawn", densityDpi + "/160f");
        return new float[]{pxToDp(x*bitmapScale), pxToDp(y*bitmapScale)};
    }

    private float pxToDp(float px){
        return px / (densityDpi / 160f);
    }

    private void endLevel(){
        Log.d("zones", "won level");
    }


}
