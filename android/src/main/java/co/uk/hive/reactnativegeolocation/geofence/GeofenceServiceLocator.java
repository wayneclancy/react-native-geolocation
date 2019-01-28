package co.uk.hive.reactnativegeolocation.geofence;

import android.content.Context;
import co.uk.hive.reactnativegeolocation.BundleTypeAdapterFactory;
import co.uk.hive.reactnativegeolocation.DataMarshaller;
import co.uk.hive.reactnativegeolocation.DataStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GeofenceServiceLocator {
    public static GeofenceController getGeofenceController(Context context) {
        return new GeofenceController(
                new GeofenceEngine(context),
                getGeofenceRepository(context),
                getGeofenceActivator(context),
                new ReRegistrationScheduler(context));
    }

    private static GeofenceRepository getGeofenceRepository(Context context) {
        return new DataStorageGeofenceRepository(getDataStorage(context), getDataMarshaller());
    }

    private static GeofenceActivator getGeofenceActivator(Context context) {
        return new DataStorageGeofenceActivator(getDataStorage(context), getDataMarshaller());
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
