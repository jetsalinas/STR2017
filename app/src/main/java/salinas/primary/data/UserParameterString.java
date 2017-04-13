package salinas.primary.data;

/**
 * Created by Jose Salinas on 4/13/2017.
 */

public class UserParameterString {

    private boolean hasLocation;
    private boolean hasHumidity;
    private boolean hasLight;
    private boolean hasPressure;
    private boolean hasTemperature;
    private boolean hasUserID;
    private String data = "";

    public UserParameterString(boolean hasLocation, boolean hasHumidity, boolean hasLight, boolean hasPressure, boolean hasTemperature, boolean hasUserID) {
        this.hasLocation = hasLocation;
        this.hasHumidity = hasHumidity;
        this.hasLight = hasLight;
        this.hasPressure = hasPressure;
        this.hasTemperature = hasTemperature;
        this.hasUserID = hasUserID;
        this.data = "";

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
            data += "TRUE,";
        } else {
            data += "FALSE,";
        }
    }

    public UserParameterString(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
