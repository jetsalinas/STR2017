package salinas.primary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.app.Activity;
import salinas.primary.sensors.SensorScheduleService;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class DebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }
}
