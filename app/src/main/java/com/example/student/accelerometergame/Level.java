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
    private Actor player;
    public final int PAR_TIME;

    Level(ArrayList<Actor> actors, ArrayList<Obstacle> zones, ArrayList<Obstacle> constantObstacles, int levelId, boolean clearGameObjects, WorldView view){
        if(clearGameObjects){
            actors.clear();
            zones.clear();
            constantObstacles.clear();
        }

        Bitmap wallBitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.wall);
        Bitmap wallBitmapVert = BitmapFactory.decodeResource(view.getResources(), R.drawable.wallvert);
        Bitmap playerBitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.ball);

        switch(levelId){
            case 1:
                PAR_TIME = 10;
                zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.start_zone), spawn(150, 150), false, view.BITMAP_SCALE, Obstacle.ObstacleType.START_ZONE)); //Index 0: Start Zone
                zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.finish_zone), spawn(view.PX_WIDTH-250, view.PX_HEIGHT-150), false, view.BITMAP_SCALE, Obstacle.ObstacleType.END_ZONE)); //Index 1: End Zone

                actors.add(new Actor(playerBitmap, spawn(zones.get(0).getX(),zones.get(0).getY()), 1, 1, true, view.BITMAP_SCALE)); //Index 0: Player
                player = actors.get(0);

                //top and bottom of screen
                constantObstacles.add(new Obstacle(wallBitmap,spawn(0,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(0,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));


                //left and right side screen
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(0,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(0,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                //rest of level
                constantObstacles.add(new Obstacle(wallBitmap,spawn(0,view.PX_HEIGHT*.2f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.6f,view.PX_HEIGHT*.2f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH,view.PX_HEIGHT*.4f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.3f,view.PX_HEIGHT*.4f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(0,view.PX_HEIGHT*.6f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.6f,view.PX_HEIGHT*.6f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH,view.PX_HEIGHT*.8f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.3f,view.PX_HEIGHT*.8f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                break;
            case 2:
                PAR_TIME = 15;
                zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.start_zone), spawn(100, 100), false, view.BITMAP_SCALE, Obstacle.ObstacleType.START_ZONE)); //Index 0: Start Zone
                zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.finish_zone), spawn(view.PX_WIDTH-150, view.PX_HEIGHT-200), false, view.BITMAP_SCALE, Obstacle.ObstacleType.END_ZONE)); //Index 1: End Zone

                actors.add(new Actor(playerBitmap, spawn(zones.get(0).getX(),zones.get(0).getY()), 1, 1, true, view.BITMAP_SCALE)); //Index 0: Player
                player = actors.get(0);

                //Top Wall
                constantObstacles.add(new Obstacle(wallBitmap, spawn(0, 0), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap, spawn(view.PX_WIDTH, 0), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                //Bottom Wall
                constantObstacles.add(new Obstacle(wallBitmap, spawn(0, view.PX_HEIGHT), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap, spawn(view.PX_WIDTH, view.PX_HEIGHT), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                //Left Wall
                constantObstacles.add(new Obstacle(wallBitmapVert, spawn(0, 0), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert, spawn(0, view.PX_HEIGHT), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                //Right Wall
                constantObstacles.add(new Obstacle(wallBitmapVert, spawn(view.PX_WIDTH, 0), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert, spawn(view.PX_WIDTH, view.PX_HEIGHT), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap, spawn(0, view.PX_HEIGHT*0.2f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap, spawn(0+(view.PX_WIDTH*0.3f), view.PX_HEIGHT*0.2f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap, spawn((view.PX_WIDTH*0.3f)+(player.getWidth()*3), view.PX_HEIGHT*0.2f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap, spawn((view.PX_WIDTH-(player.getBitmap().getWidth()*3)), view.PX_HEIGHT*0.2f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmapVert, spawn((view.PX_WIDTH-(player.getWidth()*3)), view.PX_HEIGHT*0.2f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert, spawn((view.PX_WIDTH-(player.getWidth()*3)), view.PX_HEIGHT*0.3f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmapVert, spawn((view.PX_WIDTH-(player.getWidth()*3)), (view.PX_HEIGHT*0.3f)+(player.getHeight()*3)), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert, spawn((view.PX_WIDTH-(player.getWidth()*3)), view.PX_HEIGHT), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap, spawn((view.PX_WIDTH-(player.getWidth()*3)), (view.PX_HEIGHT*0.3f)+(player.getHeight()*3)), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap, spawn((view.PX_WIDTH), (view.PX_HEIGHT*0.3f)+(player.getHeight()*3)), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                //Moving Wall
                actors.add(new Actor(wallBitmapVert,spawn((view.PX_WIDTH-(player.getWidth()*3-wallBitmapVert.getWidth())), (view.PX_HEIGHT*0.3f-wallBitmapVert.getHeight())+(player.getHeight()*3)),0,0.3f,true,view.BITMAP_SCALE));

                constantObstacles.add(new Obstacle(wallBitmapVert, spawn((view.PX_WIDTH-2*(player.getWidth()*3)), view.PX_HEIGHT*0.2f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert, spawn((view.PX_WIDTH-2*(player.getWidth()*3)), (view.PX_HEIGHT-(player.getHeight()*5))), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap, spawn(0, view.PX_HEIGHT*0.4f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap, spawn((view.PX_WIDTH-3*(player.getWidth()*3)), view.PX_HEIGHT*0.4f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap, spawn(player.getWidth()*3, view.PX_HEIGHT*0.7f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap, spawn((view.PX_WIDTH-2*(player.getWidth()*3)), view.PX_HEIGHT*0.7f), true, view.BITMAP_SCALE, Obstacle.ObstacleType.NONE));
                break;
            case 3:
                PAR_TIME = 15;
                zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.start_zone), spawn(150, 150), false, view.BITMAP_SCALE, Obstacle.ObstacleType.START_ZONE)); //Index 0: Start Zone
                zones.add(new Obstacle(BitmapFactory.decodeResource(view.getResources(), R.drawable.finish_zone), spawn(view.PX_WIDTH-250, view.PX_HEIGHT-150), false, view.BITMAP_SCALE, Obstacle.ObstacleType.END_ZONE)); //Index 1: End Zone

                actors.add(new Actor(playerBitmap, spawn(zones.get(0).getX(),zones.get(0).getY()), 1, 1, true, view.BITMAP_SCALE)); //Index 0: Player
                player = actors.get(0);

                //top and bottom of screen
                constantObstacles.add(new Obstacle(wallBitmap,spawn(0,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(0,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                //left and right side screen
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(0,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(0,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH,0),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH,view.PX_HEIGHT),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                //rest of level
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.2f,view.PX_HEIGHT*.2f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.2f,view.PX_HEIGHT*.4f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.2f,view.PX_HEIGHT*.6f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.2f,view.PX_HEIGHT*.8f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(0,view.PX_HEIGHT*.7f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.2f,view.PX_HEIGHT*.7f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.2f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.6f,view.PX_HEIGHT*.2f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.6f,view.PX_HEIGHT*.2f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.6f,view.PX_HEIGHT*.4f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.6f,view.PX_HEIGHT*.4f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.4f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.4f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.6f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.2f,view.PX_HEIGHT*.6f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.6f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.8f,view.PX_HEIGHT*.2f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.8f,view.PX_HEIGHT*.7f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.8f,view.PX_HEIGHT*.7f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.7f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.7f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmapVert,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.8f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));

                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH*.4f,view.PX_HEIGHT*.8f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                constantObstacles.add(new Obstacle(wallBitmap,spawn(view.PX_WIDTH,view.PX_HEIGHT*.8f),true, view.BITMAP_SCALE,Obstacle.ObstacleType.NONE));
                break;
            default:
                PAR_TIME=0;
                Log.d(TAG,"Invalid level: " + levelId);
                break;
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

    private float[] spawn(float x, float y, int xOffset, int yOffset){
        return (player != null ? new float[]{x+(xOffset*player.getBitmap().getWidth()), y+(yOffset*player.getBitmap().getHeight())} : new float[]{0,0});
    }
}
