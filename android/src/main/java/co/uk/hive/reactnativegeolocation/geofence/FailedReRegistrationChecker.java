package co.uk.hive.reactnativegeolocation.geofence;

public class FailedReRegistrationChecker {

    private final GeofenceActivator mGeofenceActivator;
    private final RestartGeofencingCommand.DelayedSupplier mRestartGeofencingCommandSupplier;

    public FailedReRegistrationChecker(GeofenceActivator geofenceActivator,
                                       RestartGeofencingCommand.DelayedSupplier restartGeofencingCommandSupplier) {
        mGeofenceActivator = geofenceActivator;
        mRestartGeofencingCommandSupplier = restartGeofencingCommandSupplier;
    }

    public void retry() {
        if (isFailedBroadcastReregistration()) {
            GeofenceLog.d("Failed broadcast re-registration. Attempting to restart geofencing");
            mRestartGeofencingCommandSupplier.get().run();
        }
    }

    private boolean isFailedBroadcastReregistration() {
        return mGeofenceActivator.isFailedReRegistration();
    }
}
