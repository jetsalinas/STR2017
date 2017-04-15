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
import static salinas.primary.data.Constants.*;

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

    GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {

        super.onCreate();
        //Create an instance of a Google API client
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
    }

    private String isConnected = IS_NOT_CONNECTED;

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

    private Location lastKnownLocation;
    private double lastKnownLatitude;
    private double lastKnownLongitude;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        googleApiClient.connect();

        if (isConnected == IS_CONNECTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Intent requestIntent = new Intent(Constants.REQUEST_LOCATION_PERMISSION);
                requestIntent.putExtra(Constants.REQUEST_LOCATION_PERMISSION_PERMISISON_MESSAGE, ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));
                sendBroadcast(requestIntent);
                return;
            }
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(lastKnownLocation != null) {
                lastKnownLatitude = lastKnownLocation.getLatitude();
                lastKnownLongitude = lastKnownLocation.getLongitude();
            } else {
                lastKnownLatitude = DATA_UNAVAILABLE;
                lastKnownLongitude = DATA_UNAVAILABLE;
            }

            Intent locationIntent = new Intent(Constants.BROADCAST_LOCATION);
            locationIntent.putExtra(Constants.BROADCAST_LOCATION_LATITUDE, lastKnownLatitude);
            locationIntent.putExtra(Constants.BROADCAST_LOCATION_LONGITUDE, lastKnownLongitude);
            sendBroadcast(locationIntent);
        } else {
            Intent connectionIntent = new Intent(Constants.BROADCAST_LOCATION);
            connectionIntent.putExtra(Constants.BROADCAST_CONNECTION_STATUS, isConnected);
        }
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
