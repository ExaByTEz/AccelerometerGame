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
 * @author Dean Vang
 *         4/6/2015
 */
public class WorldView extends SurfaceView implements SurfaceHolder.Callback{
    private final String TAG = this.getClass().getSimpleName();

    private WorldViewThread thread;
    private Actor player;
    private ArrayList<Actor> actors;
    private MainActivity main;
    private Chronometer chronometer;
    private Paint text;


    public WorldView(Context context, MainActivity main){
        super(context);
        this.main = main;

        //Initialize ArrayList of actors
        actors = new ArrayList<Actor>();

        //Create the simple timer using a chronometer
        chronometer = new Chronometer(context);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        //Create game thread
        thread = new WorldViewThread(this);
        getHolder().addCallback(this);

        //Create player and/or other actors
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 50, 50, 1, 1, false)); //Index 0: Player
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 150, 150, 0.2f,0, false)); //Index 1: Test object
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 250, 250, 0.5f, 0.5f, false)); //Index 2: Test object
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 350, 350, 0, 0.6f, false)); //Index 3: Test object
        actors.add(new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 250, 250, 0, 0, false)); //Index 4: Immovable test object
        //player = new Actor(BitmapFactory.decodeResource(getResources(),R.drawable.ball), 50, 50, true, false);


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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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

            }
        }
    }

    /**
     * Renders WorldView with all the actors onto the canvas
     * @param canvas
     */
    public void render(Canvas canvas){
        Log.d(TAG,"elapsed time:"+(SystemClock.elapsedRealtime()-chronometer.getBase()));
        //Draw the canvas
        canvas.drawColor(Color.WHITE);

        //Draw the time
        canvas.drawText("Time: " + ((SystemClock.elapsedRealtime()-chronometer.getBase())/1000), 200, 200, text);
    }

    public void renderActors(Canvas canvas){
        if(!actors.isEmpty()){
            for(Actor actor : actors){ //Iterate through all actors
                if(actor.getAccelerometerScaleX() > actor.MIN_SCALE || actor.getAccelerometerScaleY() > actor.MIN_SCALE){ //Move the actor if it uses the accelerometer
                    actor.translate(-main.getAccelX()*actor.getAccelerometerScaleX(), main.getAccelY()*actor.getAccelerometerScaleY());
                }
                actor.draw(canvas); //Draw the actor on the canvas
            }
        }
    }
}
