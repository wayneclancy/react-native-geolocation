package co.uk.hive.reactnativegeolocation;

import android.os.Bundle;
import co.uk.hive.reactnativegeolocation.geofence.Geofence;
import co.uk.hive.reactnativegeolocation.location.CurrentPositionRequest;
import co.uk.hive.reactnativegeolocation.location.LatLng;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

public class RNMapper {
    Geofence readGeofence(ReadableMap map) {
        return new Geofence(
                readString(map,"identifier", null),
                readInt(map,"radius", 0),
                readDouble(map,"latitude", 0d),
                readDouble(map,"longitude", 0d),
                readBoolean(map,"notifyOnEntry", true),
                readBoolean(map,"notifyOnExit", true),
                readBoolean(map,"notifyOnDwell", false),
                readInt(map,"loiteringDelay", 0),
                readBundle(map, "extras"));
    }

    WritableMap writeLocation(LatLng location) {
        WritableMap coords = Arguments.createMap();
        coords.putDouble("latitude", location.getLatitude());
        coords.putDouble("longitude", location.getLongitude());
        WritableMap result = Arguments.createMap();
        result.putMap("coords", coords);
        return result;
    }

    CurrentPositionRequest readPositionRequest(ReadableMap readableMap) {
        // Add request fields if needed
        return new CurrentPositionRequest();
    }

    private static String readString(ReadableMap map, String key, String defaultValue) {
        return map.hasKey(key) ? map.getString(key) : defaultValue;
    }

    private static int readInt(ReadableMap map, String key, int defaultValue) {
        return map.hasKey(key) ? map.getInt(key) : defaultValue;
    }

    private static double readDouble(ReadableMap map, String key, double defaultValue) {
        return map.hasKey(key) ? map.getDouble(key) : defaultValue;
    }

    private static boolean readBoolean(ReadableMap map, String key, boolean defaultValue) {
        return map.hasKey(key) ? map.getBoolean(key) : defaultValue;
    }
    private static Bundle readBundle(ReadableMap map, String key) {
        return map.hasKey(key) ? Arguments.toBundle(map.getMap(key)) : Bundle.EMPTY;
    }

    public WritableMap writeGeofenceTaskParams(String name, Bundle params) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putBundle("params", params);
        return Arguments.fromBundle(args);
    }
}
