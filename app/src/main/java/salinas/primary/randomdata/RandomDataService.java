package salinas.primary.randomdata;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import salinas.primary.data.SensorDataStringBuilder;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class RandomDataService extends IntentService {


    public RandomDataService() {
        super("Random Data Service");
    }

    public RandomDataService(String name) {
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

        if(dataParameters.get(0) == "TRUE") {
            hasLocation = true;
            if(dataParameters.get(1) == "TRUE") {
                hasHumidity = true;
            }
            if(dataParameters.get(2) == "TRUE") {
                hasLight = true;
            }
            if(dataParameters.get(3) == "TRUE") {
                hasPressure = true;
            }
            if(dataParameters.get(4) == "TRUE") {
                hasTemperature = true;
            }
            if(dataParameters.get(5) == "TRUE") {
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
    private final String userID = "SALINAS";
    private Random random = new Random();
    private String data = "";

    private void generateData() {

        latitude = random.nextFloat()*180;
        longitude = random.nextFloat()*180;
        humidity = random.nextFloat()*100;
        light = random.nextFloat()*40000;
        pressure = random.nextFloat()*800 + 300;
        temperature = random.nextFloat()*371.3 - 271.3;

        data = SensorDataStringBuilder.sensorDataString(latitude, longitude, humidity, light, pressure, temperature, userID);
    }

    private void broadcastData() {
        Intent localIntent = new Intent(Constants.BROADCAST_RANDOM_DATA);
        localIntent.putExtra(Constants.BASIC_RANDOM_DATA_STATUS, data);
        sendBroadcast(localIntent);
    }
}
