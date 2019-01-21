package co.uk.hive.reactnativegeolocation;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.util.List;

class GeofenceController {
    private final GeofenceHandler mGeofenceHandler;
    private final GeofenceRepository mGeofenceRepository;

    GeofenceController(GeofenceHandler geofenceHandler,
            GeofenceRepository geofenceRepository) {
        mGeofenceHandler = geofenceHandler;
        mGeofenceRepository = geofenceRepository;
    }

    void start(Function<Void, Void> successCallback, Function<Throwable, Void> failureCallback) {
        mGeofenceHandler.addGeofences(mGeofenceRepository.getGeofences(),
                successCallback, failureCallback);
    }

    void stop(Function<Void, Void> successCallback, Function<Throwable, Void> failureCallback) {
        mGeofenceHandler.removeGeofences(getGeofenceIds(mGeofenceRepository.getGeofences()),
                successCallback, failureCallback);
    }

    void addGeofences(List<Geofence> geofences) {
        mGeofenceRepository.addGeofences(geofences);
    }

    void removeAllGeofences() {
        mGeofenceRepository.removeAllGeofences();
    }

    Optional<Geofence> getGeofenceById(String id) {
        return mGeofenceRepository.getGeofenceById(id);
    }

    private List<String> getGeofenceIds(List<Geofence> geofences) {
        return Stream.of(geofences).map(Geofence::getId).toList();
    }
}
