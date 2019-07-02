package co.uk.hive.reactnativegeolocation.geofence;

import android.os.Build;
import android.util.Log;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class GeofenceController {
    private final GeofenceEngine mGeofenceEngine;
    private final GeofenceRepository mGeofenceRepository;
    private final GeofenceActivator mGeofenceActivator;
    private final ReRegistrationScheduler mReRegistrationScheduler;
    private final FailedReRegistrationChecker mFailedReRegistrationChecker;

    GeofenceController(GeofenceEngine geofenceEngine,
                       GeofenceRepository geofenceRepository,
                       GeofenceActivator geofenceActivator,
                       ReRegistrationScheduler reRegistrationScheduler,
                       FailedReRegistrationChecker failedReRegistrationChecker) {
        mGeofenceEngine = geofenceEngine;
        mGeofenceRepository = geofenceRepository;
        mGeofenceActivator = geofenceActivator;
        mReRegistrationScheduler = reRegistrationScheduler;
        mFailedReRegistrationChecker = failedReRegistrationChecker;
    }

    public void start(Function<? super Object, ? super Object> successCallback, Function<? super Object, ? super Object> failureCallback) {
        if (mGeofenceRepository.getGeofences().isEmpty()) {
            Log.w(getClass().getSimpleName(), "Starting geofences with none set, exiting");
            return;
        }
        mGeofenceActivator.setGeofencesActivated(true);
        mGeofenceEngine.addGeofences(mGeofenceRepository.getGeofences(), successCallback, failureCallback);
    }

    public void stop(Function<? super Object, ? super Object> successCallback, Function<? super Object, ? super Object> failureCallback) {
        if (mGeofenceRepository.getGeofences().isEmpty()) {
            Log.w(getClass().getSimpleName(), "Stopping geofences with none set, exiting");
            return;
        }
        mGeofenceActivator.setGeofencesActivated(false);
        List<String> geofenceIds = getGeofenceIds();
        if (!geofenceIds.isEmpty()) {
            mGeofenceEngine.removeGeofences(geofenceIds, successCallback, failureCallback);
        }
    }

    public void restart(Function<? super Object, ? super Object> successCallback, Function<? super Object, ? super Object> failureCallback) {
        if (mGeofenceActivator.areGeofencesActivated()) {
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

    public void setupReregistration() {
        // Implicit broadcast would be triggered but when permissions are
        // disabled, then geofences are not re-added. So we need to re-run
        // the logic anyway in case permissions have been granted.
        mFailedReRegistrationChecker.retry();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mReRegistrationScheduler.scheduleReRegistration();
        }
    }
}
