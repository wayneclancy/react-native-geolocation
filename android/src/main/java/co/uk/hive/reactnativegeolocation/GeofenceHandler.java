package co.uk.hive.reactnativegeolocation;

import com.annimon.stream.function.Function;

import java.util.List;

interface GeofenceHandler {
    void addGeofences(List<Geofence> geofenceRequests, Function<Void, Void> successCallback,
            Function<Throwable, Void> failureCallback);

    void removeGeofences(List<String> geofenceIds, Function<Void, Void> successCallback,
            Function<Throwable, Void> failureCallback);
}
