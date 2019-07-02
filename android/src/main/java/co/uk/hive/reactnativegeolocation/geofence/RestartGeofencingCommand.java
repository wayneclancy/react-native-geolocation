package co.uk.hive.reactnativegeolocation.geofence;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;

import co.uk.hive.reactnativegeolocation.PermissionChecker;

public class RestartGeofencingCommand {

    private static Function<? super Object, ? super Object> mEmptyCallback = o -> null;

    private final GeofenceController mGeofenceController;
    private final PermissionChecker mPermissionChecker;
    private final GeofenceActivator mGeofenceActivator;

    public RestartGeofencingCommand(GeofenceController geofenceController,
                                    GeofenceActivator geofenceActivator,
                                    PermissionChecker permissionChecker) {
        mGeofenceController = geofenceController;
        mGeofenceActivator = geofenceActivator;
        mPermissionChecker = permissionChecker;
    }

    public void run() {
        if (!isLocationPermissionGranted()) {
            mGeofenceActivator.setFailedReRegistration(true);
            return;
        }

        mGeofenceActivator.setFailedReRegistration(false);
        mGeofenceController.restart(mEmptyCallback, mEmptyCallback);
    }

    private boolean isLocationPermissionGranted() {
        return mPermissionChecker.isLocationPermissionGranted();
    }

    /**
     * Avoids cyclic dependency
     */
    public static class DelayedSupplier implements Supplier<RestartGeofencingCommand> {
        private final GeofenceActivator mGeofenceActivator;
        private final PermissionChecker mPermissionChecker;
        private GeofenceController mGeofenceController;

        DelayedSupplier(GeofenceActivator geofenceActivator, PermissionChecker permissionChecker) {
            mGeofenceActivator = geofenceActivator;
            mPermissionChecker = permissionChecker;
        }

        void setController(GeofenceController geofenceController) {
            mGeofenceController = geofenceController;
        }

        @Override
        public RestartGeofencingCommand get() {
            if (mGeofenceController == null) {
                throw new IllegalStateException("Geofence controller not set before calling get");
            }

            return new RestartGeofencingCommand(mGeofenceController, mGeofenceActivator, mPermissionChecker);
        }
    }
}
