package com.goodapp.yumas.present_catch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!sensors.isEmpty()) {
            sensorManager.registerListener(this, sensors.get(0), sensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //加速度
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d("SensorValuse", "/n X軸" + event.values[0] +
                    "/n Y軸" + event.values[1] +
                    "/n Z軸" + event.values[2]);
        }
        if (gameView.player != null) {
            gameView.player.move(-event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}