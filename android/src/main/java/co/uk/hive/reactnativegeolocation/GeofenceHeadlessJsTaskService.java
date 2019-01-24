package co.uk.hive.reactnativegeolocation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

public class GeofenceHeadlessJsTaskService extends HeadlessJsTaskService {

    @Override
    protected @Nullable
    HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return new HeadlessJsTaskConfig(
                    "geofence",
                    Arguments.fromBundle(extras),
                    5000,
                    false
            );
        }
        return null;
    }

    public static void start(Context context, Bundle params) {
        Intent intent = new Intent(context, GeofenceHeadlessJsTaskService.class).putExtras(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}

