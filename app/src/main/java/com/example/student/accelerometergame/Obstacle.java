package com.example.student.accelerometergame;

import android.graphics.Bitmap;

/**
 * Accelerometer Game
 *
 * @author Dean Vang, Eric Bonsness
 *         4/3/2015
 */

@SuppressWarnings("UnusedDeclaration")
public class Obstacle extends GameObject{
    public Obstacle(Bitmap bitmap, float x, float y, boolean solid, float bitmapScale){
        super(bitmap,x,y,solid,bitmapScale);
    }
}
