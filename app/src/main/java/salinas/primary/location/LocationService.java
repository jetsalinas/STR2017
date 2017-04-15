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
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import static salinas.primary.location.Constants.*;
import static salinas.primary.data.Constants.*;

/**
 * Created by Jose Salinas on 4/15/2017.
 */

public class LocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationService";

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
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
        googleApiClient.connect();
    }

    private String isConnected = IS_NOT_CONNECTED;

    LocationRequest locationRequest;
    private Location lastKnownLocation;
    private double lastKnownLatitude;
    private double lastKnownLongitude;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while(true) {
            try {
                Thread.sleep(3000);
                googleApiClient.connect();
                if(isConnected != IS_CONNECTED) {
                    Intent connectionIntent = new Intent(Constants.BROADCAST_LOCATION);
                    connectionIntent.putExtra(Constants.BROADCAST_CONNECTION_STATUS, isConnected);
                    sendBroadcast(connectionIntent);
                    Log.i(TAG, "Google API client failed to connect.");
                    return;
                } else if (hasLocationPermissions == HAS_LOCATION_PERMISSIONS) {
                    if(lastKnownLocation == null) {
                        lastKnownLatitude = DATA_UNAVAILABLE;
                        lastKnownLongitude = DATA_UNAVAILABLE;
                        Log.i(TAG, "Location data is unavailable.");
                    }
                    Intent locationIntent = new Intent(Constants.BROADCAST_LOCATION);
                    locationIntent.putExtra(Constants.BROADCAST_CONNECTION_STATUS, isConnected);
                    locationIntent.putExtra(Constants.BROADCAST_LOCATION_LATITUDE, lastKnownLatitude);
                    locationIntent.putExtra(Constants.BROADCAST_LOCATION_LONGITUDE, lastKnownLongitude);
                    sendBroadcast(locationIntent);
                } else if(hasLocationPermissions == NO_LOCATION_PERMISSIONS) {
                    Intent requestIntent = new Intent(Constants.REQUEST_LOCATION_PERMISSION);
                    requestIntent.putExtra(Constants.REQUEST_LOCATION_PERMISSION_PERMISISON_MESSAGE, ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));
                    sendBroadcast(requestIntent);
                    Log.i(TAG, "Location permissions are disabled");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static long updateInterval;

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(DEFAULT_LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_LOCATION_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private String hasLocationPermissions;

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            hasLocationPermissions = NO_LOCATION_PERMISSIONS;
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            hasLocationPermissions = HAS_LOCATION_PERMISSIONS;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
        lastKnownLatitude = location.getLatitude();
        lastKnownLongitude = location.getLongitude();
        Log.i(TAG, "Updating location. " + Double.toString(lastKnownLatitude) + "; " + Double.toString(lastKnownLongitude));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Google API client connected.");
        isConnected = IS_CONNECTED;
        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        startLocationUpdates();
        Log.i(TAG, "Starting location updates.");
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
    public void onDestroy() {
        if(googleApiClient != null) {
            googleApiClient.disconnect();
            isConnected = IS_NOT_CONNECTED;
            Log.i(TAG, "Starting location updates.");
        }
        super.onDestroy();
    }
}
