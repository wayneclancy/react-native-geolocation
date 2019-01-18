
package co.uk.hive.reactnativegeolocation;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNGeolocationModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNGeolocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNGeolocation";
    }


    @ReactMethod
    public void registerHeadlessTask() {
        // TODO
    }

    @ReactMethod
    public void configure() {
        // TODO
    }

    @ReactMethod
    public void onGeofence() {
        // TODO; see BackgroundGeolocation.on('geofence', ...)
    }

    @ReactMethod
    public void startGeofences() {
        // TODO
    }

    @ReactMethod
    public void stop() {
        // TODO
    }

    @ReactMethod
    public void addGeofence() {
        // TODO
    }

    @ReactMethod
    public void removeGeofence() {
        // TODO
    }

    @ReactMethod
    public void getCurrentPosition() {
        // TODO
    }
}
