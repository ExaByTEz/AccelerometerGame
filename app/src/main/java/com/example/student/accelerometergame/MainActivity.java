package com.example.student.accelerometergame;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.Window;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sm;
    private Sensor sen;
    private long lastUpdate;
    private WorldView worldView;
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
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

        worldView=new WorldView(this, this, displayMetrics.density,displayMetrics.widthPixels,displayMetrics.heightPixels);
        setContentView(worldView);


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
    }

    @Override
    protected void onStop() {
        super.onStop();
        worldView.changeThreadState(false);
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

}
