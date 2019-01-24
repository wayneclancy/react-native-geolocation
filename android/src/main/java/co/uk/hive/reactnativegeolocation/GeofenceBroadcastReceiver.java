package co.uk.hive.reactnativegeolocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.facebook.react.HeadlessJsTaskService;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        HeadlessJsTaskService.acquireWakeLockNow(context);
        GeofenceTransitionJobService.enqueueWork(context, intent);
    }
}
