package salinas.primary.sensors;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import salinas.primary.data.SensorDataStringBuilder;

/**
 * Created by Jose Salinas on 4/14/2017.
 */

public class SensorService extends IntentService {

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

        Log.e("SensorService", "Intent received");
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

        final Runnable generateData = new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        generateData();
                        broadcastData();
                        Thread.sleep(3*1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        generateData.run();
    }

    private double latitude;
    private double longitude;
    private double humidity;
    private double light;
    private double pressure;
    private double temperature;
    private String userID;
    private Random random = new Random();
    private String data = "";

    private void generateData() {

        if(hasLocation) {
            latitude = random.nextFloat()*180;
            longitude = random.nextFloat()*180;
        } else {
            latitude = -1000;
            longitude = -1000;
        }
        if(hasHumidity) {
            humidity = random.nextFloat()*100;
        } else {
            humidity = -1000;
        }
        if(hasLight) {
            light = random.nextFloat()*40000;

        } else {
            light = -1000;
        }
        if(hasPressure) {
            pressure = random.nextFloat()*800 + 300;

        } else {
            pressure = -1000;
        }
        if(hasTemperature) {
            temperature = random.nextFloat()*371.3 - 271.3;
        } else {
            temperature = -1000;
        }
        if(hasUserID) {
            userID = "SALINAS";
        } else {
            userID = "UNKNOWN";
        }

        data = SensorDataStringBuilder.sensorDataString(latitude, longitude, humidity, light, pressure, temperature, userID);
    }

    private void broadcastData() {
        Intent localIntent = new Intent(Constants.BROADCAST_SENSOR_DATA);
        localIntent.putExtra(Constants.BASIC_SENSOR_DATA_STATUS, data);
        sendBroadcast(localIntent);
    }
}
