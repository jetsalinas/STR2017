package salinas.primary.sensors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jose Salinas on 3/30/2017.
 */

public class SensorScheduleService extends Service {

    //TODO: Test and debug alarm scheduling functionality

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    public SensorScheduleService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("onCreate ", "Created sensor alarm");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startSensorAlarm();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(alarmManager != null && alarmIntent != null) {
            alarmManager.cancel(alarmIntent);
        }
        super.onDestroy();
    }

    public void startSensorAlarm() {
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Log.d("startSensorAlarm", "Starting sensor alarm");
        Intent intent = new Intent();
        intent.setClass(this, SensorListenerService.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, cal.getTimeInMillis(), 1000*60*15, alarmIntent);
    }

}
