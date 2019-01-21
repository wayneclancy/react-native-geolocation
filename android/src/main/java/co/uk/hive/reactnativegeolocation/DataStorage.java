package co.uk.hive.reactnativegeolocation;

import android.content.Context;
import android.content.SharedPreferences;

class DataStorage {
    private static final String SHARED_PREFERENCES_NAME =
            "@connected-home/react-native-geolocation:geofence_repository";
    private static final String KEY_GEOFENCES = "key_geofences";

    private final SharedPreferences mSharedPreferences;

    DataStorage(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
    }

    public void store(String data) {
        mSharedPreferences.edit().putString(KEY_GEOFENCES, data).apply();
    }

    public String load() {
        return mSharedPreferences.getString(KEY_GEOFENCES, "");
    }
}
