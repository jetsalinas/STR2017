package salinas.primary.sensors;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import salinas.primary.data.SensorDataStringBuilder;

import static salinas.primary.data.Constants.*;

/**
 * Created by Jose Salinas on 4/14/2017.
 */

public class SensorService extends IntentService implements SensorEventListener{

    public SensorService() {
        super("SensorService");
    }

    public SensorService(String name) {
        super(name);
    }

    private boolean hasLocation = false;
    private boolean hasHumidity = false;
    private boolean hasLight = false;
    private boolean hasPressure = false;
    private boolean hasTemperature = false;
    private boolean hasUserID = false;

    @Override
    protected void onHandleIntent(Intent intent) {

        String dataString = intent.getDataString();
        ArrayList<String> dataParameters = new ArrayList<>(Arrays.asList(dataString.split(",")));

        if(dataParameters.get(0).equals("TRUE")) {
            hasLocation = true;
            if(dataParameters.get(1).equals("TRUE")) {
                hasHumidity = true;
            }
            if(dataParameters.get(2).equals("TRUE")) {
                hasLight = true;
            }
            if(dataParameters.get(3).equals("TRUE")) {
                hasPressure = true;
            }
            if(dataParameters.get(4).equals("TRUE")) {
                hasTemperature = true;
            }
            if(dataParameters.get(5).equals("TRUE")) {
                hasUserID = true;
            }
        } else {
            stopSelf();
        }

        final Runnable updateData = new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        updateData();
                        broadcastData();
                        Thread.sleep(3*1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        updateData.run();
    }

    private double latitude;
    private double longitude;
    private double humidity;
    private double light;
    private double pressure;
    private double temperature;
    private String userID;
    private String data = "";

    private void broadcastData() {
        Intent localIntent = new Intent(Constants.BROADCAST_SENSOR_DATA);
        localIntent.putExtra(Constants.BASIC_SENSOR_DATA_STATUS, data);
        sendBroadcast(localIntent);
    }

    @Override
    public void onCreate() {
        latitude = DATA_UNAVAILABLE;
        longitude = DATA_UNAVAILABLE;
        humidity = DATA_UNAVAILABLE;
        light = DATA_UNAVAILABLE;
        pressure = DATA_UNAVAILABLE;
        temperature = DATA_UNAVAILABLE;
        userID = DEBUG_USER_ID;
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

        //Update sensors if they exist and are permitted
        if(sensor.getType() == Sensor.TYPE_PRESSURE && hasPressure) {
            updatePressure(event);
        } else if(sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE && hasTemperature) {
            updateTemperature(event);
        } else if(sensor.getType() == Sensor.TYPE_LIGHT && hasLight) {
            updateLight(event);
        } else if(sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY && hasLocation) {
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

    private void updateData() {
        if(hasUserID) {
            data = SensorDataStringBuilder.sensorDataString(latitude, longitude, humidity, light, pressure, temperature, userID);
        } else {
            data = SensorDataStringBuilder.sensorDataString(latitude, longitude, humidity, light, pressure, temperature);
        }
    }

    private SensorManager mSensorManager;
    private Sensor mPressure;
    private Sensor mTemperature;
    private Sensor mLight;
    private Sensor mHumidity;

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
