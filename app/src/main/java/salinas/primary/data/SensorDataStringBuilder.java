package salinas.primary.data;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class SensorDataStringBuilder {

    public static String sensorDataString(double latitude, double longitude, double humidity, double light, double pressure, double temperature, String userID) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(latitude);
        stringBuilder.append(',');
        stringBuilder.append(longitude);
        stringBuilder.append(',');
        stringBuilder.append(humidity);
        stringBuilder.append(',');
        stringBuilder.append(light);
        stringBuilder.append(',');
        stringBuilder.append(pressure);
        stringBuilder.append(',');
        stringBuilder.append(temperature);
        stringBuilder.append(',');
        stringBuilder.append(userID);

        return stringBuilder.toString();
    }

    public static String sensorDataString(float latitude, float longitude, float humidity, float light, float pressure, float temperature) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(latitude);
        stringBuilder.append(',');
        stringBuilder.append(longitude);
        stringBuilder.append(',');
        stringBuilder.append(humidity);
        stringBuilder.append(',');
        stringBuilder.append(light);
        stringBuilder.append(',');
        stringBuilder.append(pressure);
        stringBuilder.append(',');
        stringBuilder.append(temperature);
        stringBuilder.append(", ");

        return stringBuilder.toString();
    }
}
