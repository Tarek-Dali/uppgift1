package com.example.uppgift1sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private FragmentManager fm = getSupportFragmentManager();

    private float x = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);

        setContentView(R.layout.activity_main);
        TextView text1 = findViewById(R.id.text1);
        TextView text3 = findViewById(R.id.text3);
        TextView text4 = findViewById(R.id.text4);
        ImageView image1 = findViewById(R.id.imageView2);
        CheckBox checkbox = findViewById(R.id.checkBox);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        fm.beginTransaction().add(R.id.frameLayout, BlankFragment.class, null).commit();


        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                text4.setText("Battery percentage is " + percentage + " %");
                
                if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                    float acc1 = sensorEvent.values[0];
                    float acc2 = sensorEvent.values[1];
                    float acc3 = sensorEvent.values[2];

                    text1.setText(String.format(Locale.ROOT, "Accelerator values\n\nX: %.1f\nY: %.1f\nZ:  %.1f", acc1, acc2, acc3));
                }

                if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    float gyro1 = sensorEvent.values[0];
                    float gyro2 = sensorEvent.values[1];
                    float gyro3 = sensorEvent.values[2];

                    text3.setText(String.format(Locale.ROOT, "Gyroscope values\n\nX: %.2f\nY: %.2f\nZ:  %.2f", gyro1, gyro2, gyro3));

                    if (gyro3 > 0) {
                        x = x + gyro3;
                        image1.setRotation(x * 15);
                    } else {
                        x = x + gyro3;
                        image1.setRotation(x * 15);
                    }

                    if (gyro1 > 0.5 && gyro2 > 0.5 || gyro1 > 0.5 && gyro2 < -0.5) {
                        if (!checkbox.isChecked()) {
                            checkbox.setChecked(true);
                        }
                        Toast.makeText(MainActivity.this,  "Gyro X: " + gyro1 + "\nGyro Y: " + gyro2, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
    }
}