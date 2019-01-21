package co.uk.hive.reactnativegeolocation;

import android.os.Bundle;

public class TestData {
    public static Geofence createGeofence(String id) {
        return new Geofence(id, 0, 0, 0, false, false, false, 0, new Bundle());
    }
}
