package salinas.primary.location;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import static salinas.primary.location.Constants.*;

/**
 * Created by Jose Salinas on 4/15/2017.
 */

public class LocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public LocationService() {
        super("LocationService");
    }

    public LocationService(String name) {
        super(name);
    }

    Location lastKnownLocation;
    GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {

        super.onCreate();
        //Create an instance of a Google API client
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
    }

    private int isConnected = IS_NOT_CONNECTED;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = IS_CONNECTED;
    }

    @Override
    public void onConnectionSuspended(int i) {
        isConnected = IS_CONNECTION_SUSPENDED;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isConnected = IS_FAILED_CONNECTION;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        googleApiClient.connect();

    }

    @Override
    public void onDestroy() {
        if(googleApiClient != null) {
            googleApiClient.disconnect();
            isConnected = IS_NOT_CONNECTED;
        }
        super.onDestroy();
    }
}
