package co.uk.hive.reactnativegeolocation.geofence;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.uk.hive.reactnativegeolocation.BundleTypeAdapterFactory;
import co.uk.hive.reactnativegeolocation.DataMarshaller;
import co.uk.hive.reactnativegeolocation.DataStorage;
import co.uk.hive.reactnativegeolocation.PermissionChecker;
import co.uk.hive.reactnativegeolocation.location.LocationController;

public class GeofenceServiceLocator {

    @Nullable
    private static GeofenceActivator sGeofenceActivator;

    public static GeofenceController getGeofenceController(Context context) {
        final GeofenceActivator geofenceActivator = getGeofenceActivator(context);
        final RestartGeofencingCommand.DelayedSupplier delayedSupplier = new RestartGeofencingCommand.DelayedSupplier(geofenceActivator, new PermissionChecker(context));

        final GeofenceController geofenceController = new GeofenceController(
                new GeofenceEngine(context),
                getGeofenceRepository(context),
                geofenceActivator,
                new ReRegistrationScheduler(context),
                new FailedReRegistrationChecker(geofenceActivator, delayedSupplier)
        );

        delayedSupplier.setController(geofenceController);

        return geofenceController;
    }

    public static LocationController getLocationController(Context context) {
        return new LocationController(context);
    }

    public static RestartGeofencingCommand getRestartGeofencingCommand(Context context) {
        return new RestartGeofencingCommand(
                getGeofenceController(context),
                getGeofenceActivator(context),
                new PermissionChecker(context)
        );
    }

    private static GeofenceRepository getGeofenceRepository(Context context) {
        return new DataStorageGeofenceRepository(getDataStorage(context), getDataMarshaller());
    }

    private static GeofenceActivator getGeofenceActivator(Context context) {
        if (sGeofenceActivator == null) {
            synchronized (GeofenceServiceLocator.class) {
                if (sGeofenceActivator == null) {
                    sGeofenceActivator = new DataStorageGeofenceActivator(getDataStorage(context), getDataMarshaller());
                }
            }
        }
        return sGeofenceActivator;
    }

    private static DataStorage getDataStorage(Context context) {
        return new DataStorage(context);
    }

    private static DataMarshaller getDataMarshaller() {
        return new DataMarshaller(getGson());
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new BundleTypeAdapterFactory())
                .create();
    }
}
