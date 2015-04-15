package com.example.student.accelerometergame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
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
    private MainActivity main;
    private Chronometer chronometer;
    private Paint text;
    //private float widthDP;
    //private float heightDP;
    private float bitmapScale;


    public WorldView(Context context, MainActivity main, float bitmapScale){
        super(context);
        this.main = main;

        this.bitmapScale = bitmapScale;
        //widthDP=widthX;
        //heightDP=heightY;

        //Initialize ArrayList of actors
        actors = new ArrayList<>();
        obstacles = new ArrayList<>();

        //Create the simple timer using a chronometer
        chronometer = new Chronometer(context);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        //Create game thread
        thread = new WorldViewThread(this);
        getHolder().addCallback(this);

        //Create player and/or other actors
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 50, 50, 1, 1, true, bitmapScale)); //Index 0: Player
        /* //Test Objects
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 150, 150, 0.2f,0, true, bitmapScale)); //Index 1: Test object
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 250, 250, 0.5f, 0.5f, true, bitmapScale)); //Index 2: Test object
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 350, 350, 0, 0.6f, true, bitmapScale)); //Index 3: Test object
        */
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 250, 250, 0, 0, true, bitmapScale)); //Index 4: Test object unaffected by accelerometer
        obstacles.add(new Obstacle(BitmapFactory.decodeResource(getResources(),R.drawable.end_zone),400,300,true, bitmapScale));

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
        canvas.drawText("Time: " + ((SystemClock.elapsedRealtime()-chronometer.getBase())/1000), 200, 200, text);
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

                    //only check against those that didn't TODO:Needs to change (if we have time).  Nested loops = Bad For Performance
                    for (int j = i + 1; j < actors.size(); j++) {
                        if (actors.get(i).isIntersecting(actors.get(j))) {
                            //actors.get(i).translate(-oldX,-oldY);//for now, just stop them
                            collision = true;
                            Log.d("Collision", "Collision Successful");
                            actors.get(i).translate(-oldX,-oldY);
                            break;
                        }
                    }
                    if(!collision){
                        actors.get(i).translate(-main.getAccelX()*actors.get(i).getAccelerometerScaleX(), main.getAccelY()*actors.get(i).getAccelerometerScaleY());
                    }
                }
                actors.get(i).draw(canvas); //Draw the actor on the canvas
            }
        }
        if(!obstacles.isEmpty()){
            for(Obstacle obstacle : obstacles){
                obstacle.draw(canvas);
            }
        }
    }
}
