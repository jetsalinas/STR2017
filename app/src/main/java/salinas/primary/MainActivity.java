package salinas.primary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import salinas.primary.sensors.SensorScheduleService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Main", "Attempting to start SensorScheduleService");
        setContentView(R.layout.activity_main);



        Intent intent = new Intent(this, SensorScheduleService.class);
        startService(intent);
    }
}
