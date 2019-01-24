
package co.uk.hive.reactnativegeolocation;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.List;

public class RNGeolocationModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final GeofenceController mGeofenceController;

    public RNGeolocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        mGeofenceController = GeofenceServiceLocator.getGeofenceController(reactContext.getApplicationContext());
    }

    @Override
    public String getName() {
        return "RNGeolocation";
    }

    @ReactMethod
    public void startGeofences(Callback successCallback, Callback failureCallback) {
        mGeofenceController.start(convertCallback(successCallback), convertCallback(failureCallback));
    }

    @ReactMethod
    public void stopGeofences(Callback successCallback, Callback failureCallback) {
        mGeofenceController.stop(convertCallback(successCallback), convertCallback(failureCallback));
    }

    @ReactMethod
    public void addGeofences(ReadableArray geofencesArray) {
        List<Geofence> geofences = Stream.range(0, geofencesArray.size())
                .map(geofencesArray::getMap)
                .map(this::readGeofence)
                .toList();
        mGeofenceController.addGeofences(geofences);
    }

    @ReactMethod
    public void removeGeofences() {
        mGeofenceController.removeAllGeofences();
    }

    @ReactMethod
    public void getCurrentPosition(ReadableMap currentPositionRequest,
            Callback successCallback, Callback failureCallback) {
        // TODO
    }

    private Geofence readGeofence(ReadableMap readableMap) {
        return new Geofence(
                readableMap.getString("identifier"),
                readableMap.getInt("radius"),
                readableMap.getDouble("latitude"),
                readableMap.getDouble("longitude"),
                readableMap.getBoolean("notifyOnEntry"),
                readableMap.getBoolean("notifyOnExit"),
                readableMap.getBoolean("notifyOnDwell"),
                readableMap.getInt("loiteringDelay"),
                Arguments.toBundle(readableMap.getMap("extras"))
        );
    }

    private <T> Function<T, Object> convertCallback(Callback callback) {
        return t -> {
            callback.invoke(t);
            return null;
        };
    }
}
