package salinas.primary.sensors;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

/**
 * Created by Jose Salinas on 3/29/2017.
 */

public class SensorListenerService extends IntentService implements SensorEventListener{

    //TODO: Test and debug sensor data gathering functionality
    //TODO: Test and debug sensor broadcast functionality

    private SensorManager mSensorManager;
    private Sensor mPressure;
    private Sensor mTemperature;
    private Sensor mLight;
    private Sensor mHumidity;
    private double pressure;
    private double temperature;
    private double light;
    private double humidity;

    public SensorListenerService() {
        super("SensorListenerService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SensorListenerService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerSensors();
        //TODO: Create JSON broadcast of sensor data
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        registerSensors();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterSensors();
        super.onDestroy();
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        //TODO: Add onAccuracyChanged() logic
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

    //Update functions for all sensors
    private void updatePressure(SensorEvent event) {
        pressure = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }

    private void updateTemperature(SensorEvent event) {
        temperature = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }

    private void updateLight(SensorEvent event) {
        light = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }
    private void updateHumidity(SensorEvent event) {
        humidity = Double.parseDouble(Float.valueOf(event.values[0]).toString());
    }

    protected void registerSensors() {
        //Get all sensors available in device, returns null if no sensor is available
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

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

    protected void unregisterSensors() {
        mSensorManager.unregisterListener(this);
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
