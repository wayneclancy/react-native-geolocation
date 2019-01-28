package co.uk.hive.reactnativegeolocation.geofence;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import co.uk.hive.reactnativegeolocation.ForegroundChecker;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceEventJobService extends JobIntentService {
    private static final String TAG = GeofenceEventJobService.class.getSimpleName();
    private static final int JOB_ID = 571;
    private static final String GEOFENCE_EVENT_NAME = "geofence";

    private GeofenceController mGeofenceController;
    private ForegroundChecker mForegroundChecker;
    private GeofenceMapper mGeofenceMapper = new GeofenceMapper();

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GeofenceEventJobService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (mGeofenceController == null) {
            mGeofenceController = GeofenceServiceLocator.getGeofenceController(getApplicationContext());
        }

        if (mForegroundChecker == null) {
            mForegroundChecker = new ForegroundChecker(this);
        }

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        Stream.of(geofencingEvent.getTriggeringGeofences())
                .map(gmsGeofence -> mGeofenceController.getGeofenceById(gmsGeofence.getRequestId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(geofence -> sendEvent(geofence, geofencingEvent));
    }

    private void sendEvent(Geofence geofence, GeofencingEvent event) {
        PersistableBundle bundle = mGeofenceMapper.toBundle(event, geofence);
        if (mForegroundChecker.isAppInForeground()) {
            emitRNEvent(bundle);
        } else {
            runHeadlessJsTask(bundle);
        }
    }

    private void emitRNEvent(PersistableBundle bundle) {
        ReactNativeHost reactNativeHost = ((ReactApplication) getApplication()).getReactNativeHost();
        ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
        //noinspection ConstantConditions
        reactInstanceManager.getCurrentReactContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(GEOFENCE_EVENT_NAME, Arguments.fromBundle(new Bundle(bundle)));
    }

    private void runHeadlessJsTask(PersistableBundle bundle) {
        GeofenceHeadlessJsTaskService.start(this, bundle);
    }
}
