package co.uk.hive.reactnativegeolocation.geofence;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import co.uk.hive.reactnativegeolocation.RNMapper;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

public class GeofenceHeadlessJsTaskService extends HeadlessJsTaskService {

    private static final String HEADLESS_TASK_NAME = "GeofenceEventTask";
    private static final String HEADLESS_TASK_ARGUMENT_NAME = "geofence";

    private RNMapper mRnMapper;

    @Override
    public void onCreate() {
        super.onCreate();
        mRnMapper = new RNMapper();
    }

    @Override
    protected @Nullable
    HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return new HeadlessJsTaskConfig(
                    HEADLESS_TASK_NAME,
                    mRnMapper.writeGeofenceTaskParams(HEADLESS_TASK_ARGUMENT_NAME, extras),
                    5000,
                    false
            );
        }
        return null;
    }

    public static void start(Context context, Bundle params) {
        Intent intent = new Intent(context, GeofenceHeadlessJsTaskService.class).putExtras(params);
        HeadlessJsTaskService.acquireWakeLockNow(context);
        ContextCompat.startForegroundService(context, intent);
    }
}

