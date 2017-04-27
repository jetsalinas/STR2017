package salinas.primary.sensors;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import salinas.primary.data.SensorDataStringBuilder;
import salinas.primary.location.LocationService;
import salinas.primary.time.TimeService;

import static salinas.primary.data.Constants.*;
import static salinas.primary.time.Constants.BROADCAST_TIME_DATA;
import static salinas.primary.time.Constants.TIME_DATA_STRING;

/**
 * Created by Jose Salinas on 4/14/2017.
 */

public class SensorService extends IntentService implements SensorEventListener {

    private static final String TAG = "Sensor Service";

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

        final String dataString = intent.getDataString();
        ArrayList<String> dataParameters = new ArrayList<>(Arrays.asList(dataString.split(",")));

        if(dataParameters.get(0).equals("TRUE")) {
            hasLocation = true;
            requestLocationUpdates();
            requestTimeUpdates();

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
            Log.e(TAG, "Location settings are disabled, terminating sensor service.");
            stopSelf();
        }

        final Runnable updateData = new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        updateData();
                        Log.i(TAG, "Updating sensor information.");
                        broadcastData();
                        Log.i(TAG, "Broadcasting sensor information: " + data);
                        Thread.sleep(3*1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        updateData.run();
    }

    private String time;
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
        time = "";
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
        if(locationBroadcastReceiver != null) {
            unregisterReceiver(locationBroadcastReceiver);
        }
        if(timeBroadcastReceiver != null) {
            unregisterReceiver(timeBroadcastReceiver);
        }
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

    LocationBroadcastReceiver locationBroadcastReceiver;

    private void requestLocationUpdates() {
        locationBroadcastReceiver = new LocationBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(salinas.primary.location.Constants.BROADCAST_LOCATION);
        registerReceiver(locationBroadcastReceiver, intentFilter);

        Intent locationIntent = new Intent(this, LocationService.class);
        this.startService(locationIntent);
    }

    protected class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = intent.getDoubleExtra(salinas.primary.location.Constants.BROADCAST_LOCATION_LATITUDE, DATA_UNAVAILABLE);
            longitude = intent.getDoubleExtra(salinas.primary.location.Constants.BROADCAST_LOCATION_LONGITUDE, DATA_UNAVAILABLE);

            Log.i(TAG, "Updating location. " + Double.toString(latitude) + "; " + Double.toString(longitude));
        }
    }

    TimeBroadcastReceiver timeBroadcastReceiver;

    private void requestTimeUpdates() {
        timeBroadcastReceiver = new TimeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(BROADCAST_TIME_DATA);
        registerReceiver(timeBroadcastReceiver, intentFilter);

        Intent intent = new Intent(this, TimeService.class);
        this.startService(intent);
        Log.i(TAG, "Requesting time updates.");
    }

    protected class TimeBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            time = intent.getStringExtra(TIME_DATA_STRING);
            Log.i(TAG, "Updating time: " + time);
        }
    }

    private void updateData() {
        if(hasUserID) {
            data = SensorDataStringBuilder.sensorDataString(time, latitude, longitude, humidity, light, pressure, temperature, userID);
        } else {
            data = SensorDataStringBuilder.sensorDataString(time, latitude, longitude, humidity, light, pressure, temperature);
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
}
