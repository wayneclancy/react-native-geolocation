package co.uk.hive.reactnativegeolocation;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import static com.google.android.gms.location.Geofence.*;

class GeofenceEngine {

    private final GeofencingClient mGeofencingClient;
    private final Context mContext;
    private PendingIntent mPendingIntent;

    GeofenceEngine(Context context) {
        mContext = context;
        mGeofencingClient = LocationServices.getGeofencingClient(mContext);

        Intent intent = new Intent(mContext, GeofenceBroadcastReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    void addGeofences(List<Geofence> geofenceRequests, Function<? super Object, ? super Object> successCallback,
            Function<? super Object, ? super Object> failureCallback) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("Location permission needs to be granted before calling addGeofences");
        }

        List<com.google.android.gms.location.Geofence> geofences = Stream.of(geofenceRequests)
                .map(geofence -> new com.google.android.gms.location.Geofence.Builder()
                        .setRequestId(geofence.getId())
                        .setCircularRegion(
                                geofence.getLatitude(),
                                geofence.getLongitude(),
                                geofence.getRadius())
                        .setLoiteringDelay(geofence.getLoiteringDelay())
                        .setTransitionTypes(defineTransitionTypes(geofence))
                        .setExpirationDuration(NEVER_EXPIRE)
                        .build())
                .toList();

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .addGeofences(geofences)
                .setInitialTrigger(defineInitialTrigger())
                .build();

        mGeofencingClient.addGeofences(
                geofencingRequest, mPendingIntent)
                .addOnSuccessListener(successCallback::apply)
                .addOnFailureListener(failureCallback::apply);
    }

    void removeGeofences(List<String> geofenceIds, Function<? super Object, ? super Object> successCallback,
                         Function<? super Object, ? super Object> failureCallback) {
        mGeofencingClient.removeGeofences(geofenceIds)
                .addOnSuccessListener(successCallback::apply)
                .addOnFailureListener(failureCallback::apply);
    }

    private int defineTransitionTypes(Geofence geofence) {
        return (geofence.isNotifyOnEnter() ? GEOFENCE_TRANSITION_ENTER : 0)
                | (geofence.isNotifyOnExit() ? GEOFENCE_TRANSITION_EXIT : 0)
                | (geofence.isNotifyOnDwell() ? GEOFENCE_TRANSITION_DWELL : 0);
    }

    private int defineInitialTrigger() {
        return 0; // do not notify at the moment of setting the geofence
    }
}
