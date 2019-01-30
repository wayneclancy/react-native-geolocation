package co.uk.hive.reactnativegeolocation.geofence;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import co.uk.hive.reactnativegeolocation.RNMapper;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import uk.co.centrica.hive.reactnativegeolocation.R;

public class GeofenceHeadlessJsTaskService extends HeadlessJsTaskService {

    private static final String HEADLESS_TASK_NAME = "GeofenceEventTask";
    private static final String HEADLESS_TASK_ARGUMENT_NAME = "geofence";
    public static final int NOTIFICATION_ID = 12;
    public static final String NOTIFICATION_CHANNEL_ID = "notification_channel_headless_task_notification";

    private RNMapper mRnMapper;

    @Override
    public void onCreate() {
        super.onCreate();
        mRnMapper = new RNMapper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showOreoNotification();
        return super.onStartCommand(intent, flags, startId);
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

    private void showOreoNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (nm != null && nm.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
                nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        getString(R.string.headless_notification_channel_name),
                        NotificationManager.IMPORTANCE_MIN));
            }
            startForeground(NOTIFICATION_ID, new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentText(getString(R.string.headless_notification_processing))
                    .build());
        }
    }
}
