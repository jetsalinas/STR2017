package salinas.primary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import salinas.primary.data.UserParameterStringBuilder;
import salinas.primary.file.FileService;
import salinas.primary.sensors.Constants;
import salinas.primary.sensors.SensorService;

import static salinas.primary.data.Constants.DATA_UNAVAILABLE;
import static salinas.primary.data.Constants.NO_USER_ID;

/**
 * Created by Jose Salinas on 4/14/2017.
 */

public class SensorMonitorActivity extends Activity {

    private TextView valuesLatitude;
    private TextView valuesLongitude;
    private TextView valuesHumidity;
    private TextView valuesLight;
    private TextView valuesPressure;
    private TextView valuesTemperature;
    private TextView valuesUserID;

    private SensorBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_monitor);

        valuesLatitude = (TextView)findViewById(R.id.sensorlatitudevalue);
        valuesLongitude= (TextView)findViewById(R.id.sensorlongitudevalue);
        valuesHumidity = (TextView)findViewById(R.id.sensorhumidityvalue);
        valuesLight = (TextView)findViewById(R.id.sensorlightvalue);
        valuesPressure = (TextView)findViewById(R.id.sensorpressurevalue);
        valuesTemperature = (TextView)findViewById(R.id.sensortemperaturevalue);
        valuesUserID = (TextView)findViewById(R.id.sensoruserid);
    }

    @Override
    protected void onStart() {

        super.onStart();

        broadcastReceiver = new SensorMonitorActivity.SensorBroadcastReceiver(new Handler());
        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_SENSOR_DATA);
        getBaseContext().registerReceiver(broadcastReceiver, intentFilter);

        Intent sensorDataIntent = new Intent(this, SensorService.class);
        sensorDataIntent.setData(Uri.parse(UserParameterStringBuilder.userParameterString(true, true, true, true, true, true)));
        this.startService(sensorDataIntent);

        Intent fileOutputIntent = new Intent(this, FileService.class);
        this.startService(fileOutputIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    ArrayList<String> data;
    String latitude = "";
    String longitude = "";
    String humidity = "";
    String light = "";
    String pressure = "";
    String temperature = "";
    String userID = "";

    protected class SensorBroadcastReceiver extends BroadcastReceiver {

        private Handler handler;

        public SensorBroadcastReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, final Intent intent) {
            data = new ArrayList<>(Arrays.asList(intent.getStringExtra(Constants.BASIC_SENSOR_DATA_STATUS).split(",")));
            latitude = data.get(1);
            longitude = data.get(2);
            humidity = data.get(3);
            light = data.get(4);
            pressure = data.get(5);
            temperature = data.get(6);
            userID = data.get(7);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(latitude.equals(Double.toString(DATA_UNAVAILABLE))) {
                        valuesLatitude.setText(getResources().getString(R.string.text_values_nodata));
                        valuesLongitude.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesLatitude.setText(latitude);
                        valuesLongitude.setText(longitude);
                    }
                    if(humidity.equals(Double.toString(DATA_UNAVAILABLE))) {
                        valuesHumidity.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesHumidity.setText(humidity);
                    }
                    if(light.equals(Double.toString(DATA_UNAVAILABLE))) {
                        valuesLight.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesLight.setText(light);
                    }
                    if(pressure.equals(Double.toString(DATA_UNAVAILABLE))) {
                        valuesPressure.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesPressure.setText(pressure);
                    }
                    if(temperature.equals(Double.toString(DATA_UNAVAILABLE))) {
                        valuesTemperature.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesTemperature.setText(temperature);
                    }
                    if(userID.equals(NO_USER_ID)) {
                        valuesUserID.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesUserID.setText(userID);
                    }
                }
            });
        }
    }
}
