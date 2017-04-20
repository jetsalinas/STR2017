package salinas.primary.time;

import android.app.IntentService;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import salinas.primary.time.Constants.*;
import salinas.primary.data.TimeStringBuilder;
import salinas.primary.sensors.SensorService;

import static salinas.primary.time.Constants.TIME_DATA_STRING;

/**
 * Created by Jose Salinas on 4/20/2017.
 */

public class TimeService extends IntentService {

    public static final String TAG = "Time Service";

    public TimeService() {
        super(TAG);
    }

    public TimeService(String name) {
        super(name);
    }

    Calendar currentCalendar;
    int currentMilis;
    int currentSecond;
    int currentMinute;
    int currentHour;
    int currentDate;
    int currentMonth;
    int currentYear;
    String timeString;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        currentCalendar =  Calendar.getInstance();
        currentMilis = currentCalendar.get(Calendar.MILLISECOND);
        currentSecond = currentCalendar.get(Calendar.SECOND);
        currentMinute = currentCalendar.get(Calendar.MINUTE);
        currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        currentDate = currentCalendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = currentCalendar.get(Calendar.MONTH);
        currentYear = currentCalendar.get(Calendar.YEAR);
        Log.i(TAG, "Creating calendar instance");

        timeString = TimeStringBuilder.timeDataString(currentMilis, currentSecond, currentMinute, currentHour, currentDate, currentMonth, currentYear);
        Intent timeIntent = new Intent(this, SensorService.class);
        timeIntent.putExtra(TIME_DATA_STRING, timeString);
        sendBroadcast(timeIntent);
        Log.i(TAG, "Broadcasted time intent: " + timeString);

    }
}
