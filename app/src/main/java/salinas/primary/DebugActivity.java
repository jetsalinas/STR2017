package salinas.primary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import salinas.primary.data.UserParameterStringBuilder;
import salinas.primary.randomdata.Constants;
import salinas.primary.randomdata.RandomDataService;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class DebugActivity extends Activity {

    private TextView valuesLatitude;
    private TextView valuesLongitude;
    private TextView valuesHumidity;
    private TextView valuesLight;
    private TextView valuesPressure;
    private TextView valuesTemperature;
    private TextView valuesUserID;

    private DebugBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        valuesLatitude = (TextView)findViewById(R.id.randomlatitudevalue);
        valuesLongitude= (TextView)findViewById(R.id.randomlongitudevalue);
        valuesHumidity = (TextView)findViewById(R.id.randomhumidityvalue);
        valuesLight = (TextView)findViewById(R.id.randomlightvalue);
        valuesPressure = (TextView)findViewById(R.id.randompressurevalue);
        valuesTemperature = (TextView)findViewById(R.id.randomtemperaturevalue);
        valuesUserID = (TextView)findViewById(R.id.randomuserid);
    }

    @Override
    protected void onStart() {
        super.onStart();

        broadcastReceiver = new DebugBroadcastReceiver(new Handler());
        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_RANDOM_DATA);
        getBaseContext().registerReceiver(broadcastReceiver, intentFilter);

        Intent randomDataIntent = new Intent(this, RandomDataService.class);
        randomDataIntent.setData(Uri.parse(UserParameterStringBuilder.userParameterString(true, false, false, false, false, false)));
        this.startService(randomDataIntent);
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

    protected class DebugBroadcastReceiver extends BroadcastReceiver {

        private Handler handler;

        public DebugBroadcastReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, final Intent intent) {
            data = new ArrayList<>(Arrays.asList(intent.getStringExtra(Constants.BASIC_RANDOM_DATA_STATUS).split(",")));
            latitude = data.get(0);
            longitude = data.get(1);
            humidity = data.get(2);
            light = data.get(3);
            pressure = data.get(4);
            temperature = data.get(5);
            userID = data.get(6);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(latitude.equals(Double.toString(-1000))) {
                        valuesLatitude.setText(getResources().getString(R.string.text_values_nodata));
                        valuesLongitude.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesLatitude.setText(latitude);
                        valuesLongitude.setText(longitude);
                    }
                    if(humidity.equals(Double.toString(-1000))) {
                        valuesHumidity.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesHumidity.setText(humidity);
                    }
                    if(light.equals(Double.toString(-1000))) {
                        valuesLight.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesLight.setText(light);
                    }
                    if(pressure.equals(Double.toString(-1000))) {
                        valuesPressure.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesPressure.setText(pressure);
                    }
                    if(temperature.equals(Double.toString(-1000))) {
                        valuesTemperature.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesTemperature.setText(temperature);
                    }
                    if(userID.equals("UNKNOWN")) {
                        valuesUserID.setText(getResources().getString(R.string.text_values_nodata));
                    } else {
                        valuesUserID.setText(userID);
                    }
                }
            });
        }
    }
}
