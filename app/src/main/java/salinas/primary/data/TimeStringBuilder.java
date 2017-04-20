package salinas.primary.data;

/**
 * Created by Jose Salinas on 4/20/2017.
 */

public class TimeStringBuilder {

    private static int currentMilis;
    private static int currentSecond;
    private static int currentMinute;
    private static int currentHour;
    private static int currentDate;
    private static int currentMonth;
    private static int currentYear;
    private static String timeString;

    public static String timeDataString(int currentMilis, int currentSecond, int currentMinute, int currentHour, int currentDate, int currentMonth, int currentYear) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(currentYear);
        stringBuilder.append(":");
        stringBuilder.append(currentMonth);
        stringBuilder.append(":");
        stringBuilder.append(currentDate);
        stringBuilder.append(":");
        stringBuilder.append(currentHour);
        stringBuilder.append(":");
        stringBuilder.append(currentMinute);
        stringBuilder.append(":");
        stringBuilder.append(currentSecond);
        stringBuilder.append(":");
        stringBuilder.append(currentMilis);
        timeString = stringBuilder.toString();
        return timeString;
    }
}
