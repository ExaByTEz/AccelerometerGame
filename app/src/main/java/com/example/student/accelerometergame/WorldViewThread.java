package com.example.student.accelerometergame;

import android.graphics.Canvas;

/**
 * Accelerometer Game
 *
 * @author Dean Vang, Eric Bonsness
 *         4/6/2015
 */
public class WorldViewThread extends Thread {
    private WorldView view;
    private boolean running = false;

    public WorldViewThread(WorldView view){
        this.view = view;
    }

    public void setRunning(boolean run){
        running = run;
    }
    public boolean getRunning(){
        return running;
    }

    /**
     * Render the actual WorldView canvas while the thread is running
     */
    @Override
    public void run() {
        while(running){
            Canvas c = null;
            try{
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()){
                    view.render(c);
                    //other code to update actors
                    view.renderActors(c);

                }
            }finally {
                if(c != null){
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }

        }
    }
}
