
package co.uk.hive.reactnativegeolocation;

import co.uk.hive.reactnativegeolocation.geofence.Geofence;
import co.uk.hive.reactnativegeolocation.geofence.GeofenceController;
import co.uk.hive.reactnativegeolocation.geofence.GeofenceServiceLocator;
import co.uk.hive.reactnativegeolocation.location.CurrentPositionRequest;
import co.uk.hive.reactnativegeolocation.location.LatLng;
import co.uk.hive.reactnativegeolocation.location.LocationController;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.facebook.react.bridge.*;

import java.util.List;

public class RNGeolocationModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final GeofenceController mGeofenceController;
    private final LocationController mLocationController;

    public RNGeolocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        mGeofenceController = GeofenceServiceLocator.getGeofenceController(reactContext.getApplicationContext());
        mLocationController = GeofenceServiceLocator.getLocationController(reactContext.getApplicationContext());
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
        Function<LatLng, Object> positionCallback = location -> {
            successCallback.invoke(writeLocation(location));
            return null;
        };
        mLocationController.getCurrentPosition(
                readPositionRequest(currentPositionRequest), positionCallback, convertCallback(failureCallback));
    }

    private WritableMap writeLocation(LatLng location) {
        WritableMap coords = Arguments.createMap();
        coords.putDouble("latitude", location.getLatitude());
        coords.putDouble("longitude", location.getLongitude());
        WritableMap result = Arguments.createMap();
        result.putMap("coords", coords);
        return result;
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

    private CurrentPositionRequest readPositionRequest(ReadableMap readableMap) {
        return new CurrentPositionRequest(readableMap.getInt("timeout"));
    }

    private <T> Function<T, Object> convertCallback(Callback callback) {
        return t -> {
            callback.invoke(t);
            return null;
        };
    }
}
