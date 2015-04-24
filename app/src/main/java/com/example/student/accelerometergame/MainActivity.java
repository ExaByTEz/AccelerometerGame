package com.example.student.accelerometergame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sm;
    private Sensor sen;
    private long lastUpdate;
    private WorldView worldView;
    private LinearLayout game;
    private LinearLayout gameWidgets;
    private TextView gameText;
    private Button restartBtn;
    private Button pauseBtn;
    private Button resumeBtn;
    //private static final int SHAKE_THRESHOLD = 500;
    private float x;
    private float y;
    public MainActivity(){
        lastUpdate = 0;
        x = 0;
        y = 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        game = new LinearLayout(this);
        game.setOrientation(LinearLayout.VERTICAL);
        gameWidgets = new LinearLayout(this);
        gameWidgets.setOrientation(LinearLayout.HORIZONTAL);

        gameText = new TextView(this);
        gameText.setText("GAME PAUSED");
        gameText.setTextSize(24);
        gameText.setVisibility(View.GONE);
        gameText.setGravity(Gravity.CENTER);
        gameText.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        restartBtn = new Button(this);
        restartBtn.setText("Restart");
        restartBtn.setTextSize(12);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldView.changeThreadState(false);
                game.removeAllViews();
                worldView = new WorldView(v.getContext(), (MainActivity)v.getContext(), getWindowManager().getDefaultDisplay());
                addViewsToGame();
                Log.d("button","restart btn clicked");
                Log.d("button","Game time:"+ SystemClock.elapsedRealtime());
            }
        });

        pauseBtn = new Button(this);
        pauseBtn.setText("Pause");
        pauseBtn.setTextSize(12);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(worldView.getThreadState() && !worldView.getWinFlag()){
                    worldView.changeThreadState(false);
                    worldView.setVisibility(View.GONE);
                    gameText.setVisibility(View.VISIBLE);

                    //worldView.pauseThread();
                    Log.d("button", "pause button pressed");
                }
            }
        });

        resumeBtn = new Button(this);
        resumeBtn.setText("Resume");
        resumeBtn.setTextSize(12);
        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!worldView.getThreadState() && !worldView.getWinFlag()){
                    worldView.changeThreadState(true);
                    gameText.setVisibility(View.GONE);
                    worldView.setVisibility(View.VISIBLE);
                    Log.d("button", "resume button pressed");
                }
            }
        });

        gameWidgets.addView(restartBtn);
        gameWidgets.addView(pauseBtn);
        gameWidgets.addView(resumeBtn);

        worldView = new WorldView(this, this, getWindowManager().getDefaultDisplay());
        addViewsToGame();


        setContentView(game);


        //Setup the sensor manager and the sensor used for the accelerometer
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sen = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Setup the listener for the sensor manager
        sm.registerListener(this, sen, SensorManager.SENSOR_DELAY_NORMAL); //registerListener(Context, Sensor, Rate)

    }

    @Override
    protected void onPause() {
        super.onPause();
        worldView.changeThreadState(false);
        //Stop sensor when onPause is called
        sm.unregisterListener(this);
        worldView.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        worldView.changeThreadState(false);
        sm.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        worldView.changeThreadState(false);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        worldView.changeThreadState(true);
        //Start sensor when onResume is called
        sm.registerListener(this, sen, SensorManager.SENSOR_DELAY_NORMAL);
        worldView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            //float z = event.values[2]; //No need for z value

            //Only get the data after every few milliseconds instead of always constantly
            long curTime = System.currentTimeMillis();
            if((curTime - lastUpdate) > 100){
                //long diffTime = curTime-lastUpdate;
                lastUpdate = curTime;
                this.x = x;
                this.y = y;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Get the accelerometer x coordinate
     * @return The value of the x coordinate of the accelerometer :float
     */
    public float getAccelX(){
        return x;
    }

    /**
     * Get the accelerometer y coordinate
     * @return The value of the y coordinate of the accelerometer :float
     */
    public float getAccelY(){
        return y;
    }

    private void addViewsToGame(){
        game.addView(gameWidgets);
        game.addView(worldView);
        game.addView(gameText);
        delayButton(restartBtn, 2000);
        delayButton(pauseBtn, 2000);
        delayButton(resumeBtn, 2000);
    }

    private void delayButton(final Button button, int delay){
        button.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        button.setEnabled(true);
                    }
                });
            }
        },delay);
    }

}
