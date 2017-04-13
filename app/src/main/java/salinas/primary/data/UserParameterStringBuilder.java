package salinas.primary.data;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class UserParameterStringBuilder {

    public static String userParameterString(boolean hasLocation, boolean hasHumidity, boolean hasLight, boolean hasPressure, boolean hasTemperature, boolean hasUserID) {
        String data = "";

        if(hasLocation) {
            data += "TRUE,";
        } else {
            data += "FALSE,";
        }
        if(hasHumidity) {
            data += "TRUE,";
        } else {
            data += "FALSE,";
        }
        if(hasLight) {
            data += "TRUE,";
        } else {
            data += "FALSE,";
        }
        if(hasPressure) {
            data += "TRUE,";
        } else {
            data += "FALSE,";
        }
        if(hasTemperature) {
            data += "TRUE,";
        } else {
            data += "FALSE,";
        }
        if(hasUserID) {
            data += "TRUE";
        } else {
            data += "FALSE";
        }

        return data;
    }
}