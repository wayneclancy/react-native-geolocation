package co.uk.hive.reactnativegeolocation.geofence;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class GeofenceController {
    private final GeofenceEngine mGeofenceEngine;
    private final GeofenceRepository mGeofenceRepository;

    GeofenceController(GeofenceEngine geofenceEngine,
            GeofenceRepository geofenceRepository) {
        mGeofenceEngine = geofenceEngine;
        mGeofenceRepository = geofenceRepository;
    }

    public void start(Function<? super Object, ? super Object> successCallback, Function<? super Object, ? super Object> failureCallback) {
        mGeofenceRepository.setGeofencesActivated(true);
        mGeofenceEngine.addGeofences(mGeofenceRepository.getGeofences(), successCallback, failureCallback);
    }

    public void stop(Function<? super Object, ? super Object> successCallback, Function<? super Object, ? super Object> failureCallback) {
        mGeofenceRepository.setGeofencesActivated(false);
        List<String> geofenceIds = getGeofenceIds();
        if (!geofenceIds.isEmpty()) {
            mGeofenceEngine.removeGeofences(geofenceIds, successCallback, failureCallback);
        }
    }

    public void restart(Function<? super Object, ? super Object> successCallback, Function<? super Object, ? super Object> failureCallback) {
        if (mGeofenceRepository.areGeofencesActivated()) {
            start(successCallback, failureCallback);
        }
    }

    private List<String> getGeofenceIds() {
        return Stream.of(mGeofenceRepository.getGeofences())
                .map(Geofence::getId)
                .toList();
    }

    public void addGeofences(List<Geofence> geofences) {
        mGeofenceRepository.addGeofences(geofences);
    }

    public void removeAllGeofences() {
        mGeofenceRepository.removeAllGeofences();
    }

    public Optional<Geofence> getGeofenceById(String id) {
        return mGeofenceRepository.getGeofenceById(id);
    }
}
