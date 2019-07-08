package co.uk.hive.reactnativegeolocation.geofence;

import android.util.Log;

import uk.co.centrica.hive.reactnativegeolocation.BuildConfig;

class GeofenceLog {

    private static final String TAG = "ReactNativeGeolocation";

    static void d(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
}
