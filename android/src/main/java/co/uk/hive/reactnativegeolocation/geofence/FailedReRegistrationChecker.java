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
            mRestartGeofencingCommandSupplier.get().run();
        }
    }

    private boolean isFailedBroadcastReregistration() {
        return mGeofenceActivator.isFailedReRegistration();
    }
}
