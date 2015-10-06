package com.christophergs.sensorreadings;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.text.format.Time;
import android.widget.Toast;
import android.os.Handler;

import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements SensorEventListener {


    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration_x;
    TextView acceleration_y;
    TextView acceleration_z;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    public void stopTimer(View view){
        if(mTimer1 != null){
            mTimer1.cancel();
            mTimer1.purge();
            toastIt("Data saving stopped");
        }
    }

    public void startTimer(View view){
        toastIt("Data being saved to csv...");
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        Time now = new Time();
                        now.setToNow();
                        String sTime = now.format("%Y_%m_%d_%H_%M_%S");
                        String FILENAME = "sensor_log.csv";
                        String entry = sTime + "," +
                                acceleration_x.getText().toString() + "," +
                                acceleration_y.getText().toString() + "," +
                                acceleration_z.getText().toString() + ",\n";

                        try {
                            FileOutputStream out = openFileOutput( FILENAME, Context.MODE_APPEND );
                            out.write( entry.getBytes() );
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 0, 10);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        acceleration_x=(TextView)findViewById(R.id.acceleration_x);
        acceleration_y=(TextView)findViewById(R.id.acceleration_y);
        acceleration_z=(TextView)findViewById(R.id.acceleration_z);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        acceleration_x.setText("X: " + event.values[0]);
        acceleration_y.setText("Y: " + event.values[1]);
        acceleration_z.setText("Z: " + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public void saveData(View view) {
        //Long tsLong = System.currentTimeMillis()/1000;
        //String ts = tsLong.toString();
        Time now = new Time();
        now.setToNow();
        String sTime = now.format("%Y_%m_%d_%H_%M_%S");
        String FILENAME = "sensor_log.csv";
        String entry = sTime + "," +
                acceleration_x.getText().toString() + "," +
                acceleration_y.getText().toString() + "," +
                acceleration_z.getText().toString() + ",\n";
        try {
            FileOutputStream out = openFileOutput( FILENAME, Context.MODE_APPEND );
            out.write( entry.getBytes() );
            out.close();
            toastIt("Data saved to csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toastIt( String msg ) {
        Toast.makeText( MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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
}
