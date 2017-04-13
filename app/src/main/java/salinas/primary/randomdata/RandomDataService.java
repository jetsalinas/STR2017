package salinas.primary.randomdata;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class RandomDataService extends IntentService {

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
            //TODO: return null on intent and kill service
        }
    }
}
