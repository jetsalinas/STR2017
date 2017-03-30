package salinas.primary.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;

import salinas.primary.R;

public class SensorActivity extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mPressure;
    private Sensor mTemperature;
    private Sensor mLight;
    private Sensor mHumidity;
    private double pressure;
    private double temperature;
    private double light;
    private double humidity;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //Get all sensors available in device, returns null if no sensor is available
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        //TODO: ACTUALLY DO THIS SALINAS
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;

        //Update sensors if they exist
        if(sensor.getType() == Sensor.TYPE_PRESSURE) {
            updatePressure(event);
        } else if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            updateTemperature(event);
        } else if(sensor.getType() == Sensor.TYPE_LIGHT) {
            updateLight(event);
        } else if(sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            updateHumidity(event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Register all sensors if they are available
        if (mPressure != null) {
            mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mTemperature != null) {
            mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mLight != null) {
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mHumidity != null) {
            mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        //Unregisters all sensors
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    //Update functions for all sensors
    protected void updatePressure(SensorEvent event) {
        pressure = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }

    protected void updateTemperature(SensorEvent event) {
        temperature = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }

    protected void updateLight(SensorEvent event) {
        light = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }
    protected void updateHumidity(SensorEvent event) {
        humidity = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }

    //Getter functions for all sensors
    public double getHumidity() {
        return humidity;
    }

    public double getLight() {
        return light;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }
}
