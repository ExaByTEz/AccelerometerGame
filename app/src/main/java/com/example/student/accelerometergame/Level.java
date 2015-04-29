package com.example.student.accelerometergame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * AccelerometerGame
 *
 * @author Dean Vang, Eric Bonsness
 *         4/29/2015
 */
public class Level {
    private final String TAG = this.getClass().getSimpleName();

    Level(ArrayList<Actor> actors, ArrayList<Obstacle> zones, ArrayList<Obstacle> obstacles, int levelId, boolean clearGameObjects, WorldView view){
        if(clearGameObjects){
            actors.clear();
            zones.clear();
            obstacles.clear();
        }

        Bitmap wallBitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.wall);
        Bitmap playerBitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.ball);

        zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.end_zone), spawn(view.PX_HEIGHT - 75, 600), false, view.BITMAP_SCALE, Obstacle.ObstacleType.START_ZONE)); //Index 0: Start Zone
        zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.end_zone), spawn(400, 300), false, view.BITMAP_SCALE, Obstacle.ObstacleType.END_ZONE)); //Index 1: End Zone

        //Create player and/or other actors
        actors.add(new Actor(playerBitmap, spawn(zones.get(0).getX(),zones.get(0).getY()), 1, 1, true, view.BITMAP_SCALE)); //Index 0: Player


        switch(levelId){
            case 1:
                obstacles.add(new Obstacle(wallBitmap,spawn(250+(wallBitmap.getWidth()*view.BITMAP_SCALE),300),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                break;
            default: Log.d(TAG,"Invalid level: " + levelId); break;
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

}
