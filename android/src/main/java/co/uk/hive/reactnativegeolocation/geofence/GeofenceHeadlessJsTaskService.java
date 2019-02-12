package co.uk.hive.reactnativegeolocation.geofence;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import co.uk.hive.reactnativegeolocation.RNMapper;
import com.facebook.react.JobHeadlessJsTaskService;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

public class GeofenceHeadlessJsTaskService extends JobHeadlessJsTaskService {

    private static final int JOB_ID = 434;

    private static final String HEADLESS_TASK_NAME = "GeofenceEventTask";
    private static final String HEADLESS_TASK_ARGUMENT_NAME = "geofence";
    public static final int MAX_EXECUTION_DELAY_MILLIS = 1000;

    private RNMapper mRnMapper;

    @Override
    public void onCreate() {
        super.onCreate();
        mRnMapper = new RNMapper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PersistentLog.log("GeofenceHeadlessJsTaskService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected @Nullable
    HeadlessJsTaskConfig getTaskConfig(Bundle extras) {
        HeadlessJsTaskConfig config = null;
        if (extras != null) {
            config = new HeadlessJsTaskConfig(
                    HEADLESS_TASK_NAME,
                    mRnMapper.writeGeofenceTaskParams(HEADLESS_TASK_ARGUMENT_NAME, extras),
                    5000,
                    true
            );
        }
        PersistentLog.log("GeofenceHeadlessJsTaskService getTaskConfig returns " + config);
        return config;
    }

    public static void start(Context context, PersistableBundle params) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            PersistentLog.log("Scheduling GeofenceHeadlessJsTaskService: " + params.toString());
            jobScheduler.schedule(buildJobInfo(context, params));
        }
    }

    private static JobInfo buildJobInfo(Context context, PersistableBundle params) {
        return new JobInfo.Builder(JOB_ID, new ComponentName(context, GeofenceHeadlessJsTaskService.class))
                .setExtras(params)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
    }
}
