package com.example.student.accelerometergame;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Accelerometer Game
 *
 * @author Dean Vang, Eric Bonsness
 *         4/3/2015
 */

@SuppressWarnings("UnusedDeclaration")
public class Obstacle extends GameObject{

    private ObstacleType obstacleType;

    public Obstacle(Bitmap bitmap, float[] spawn, boolean solid, float bitmapScale, ObstacleType obstacleType){
        super(bitmap,spawn[0],spawn[1],solid,bitmapScale);
        this.obstacleType = obstacleType;
        Log.d("Obstacle","obstacleType="+this.obstacleType);
    }

    /**
     * Sets the obstacle type
     * @param obstacleType - The type of obstacle :ObstacleType
     */
    public void setObstacleType(ObstacleType obstacleType){
        this.obstacleType = obstacleType;
    }

    /**
     * Get the type of obstacle
     * @return the type of obstacle :ObstacleType
     */
    public ObstacleType getObstacleType(){
        return obstacleType;
    }

    public enum ObstacleType{
        /**
         * No type associated with obstacle
         */
        NONE(0),
        /**
         * Start zone (useful for respawning if we get time)
         */
        START_ZONE(1),
        /**
         * End zone
         */
        END_ZONE(2);

        private final int type;
        ObstacleType(int type){
            this.type = type;
        }



    }
}
