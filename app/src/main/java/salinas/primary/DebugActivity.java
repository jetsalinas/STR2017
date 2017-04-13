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

import salinas.primary.data.UserParameterStringBuilder;
import salinas.primary.randomdata.Constants;
import salinas.primary.randomdata.RandomDataService;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class DebugActivity extends Activity {

    private TextView valuesLocation;
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

        valuesLocation = (TextView)findViewById(R.id.randomlocationvalue);
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
        randomDataIntent.setData(Uri.parse(UserParameterStringBuilder.userParameterString(true, true, true, true, true, true)));
        this.startService(randomDataIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    protected class DebugBroadcastReceiver extends BroadcastReceiver {

        private Handler handler;

        public DebugBroadcastReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    valuesLocation.setText("Broadcast received.");
                    valuesHumidity.setText("Broadcast received.");
                    valuesLight.setText("Broadcast received.");
                    valuesPressure.setText("Broadcast received.");
                    valuesTemperature.setText("Broadcast received.");
                    valuesUserID.setText("Broadcast received.");
                }
            });
        }
    }
}
