package co.uk.hive.reactnativegeolocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.uk.hive.reactnativegeolocation.geofence.GeofenceController;
import co.uk.hive.reactnativegeolocation.geofence.GeofenceServiceLocator;
import com.annimon.stream.function.Function;

public class BootCompleteReceiver extends BroadcastReceiver {

    private Function<? super Object, ? super Object> emptyCallback = o -> null;

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofenceController geofenceController = GeofenceServiceLocator.getGeofenceController(context);
        geofenceController.restart(emptyCallback, emptyCallback);
    }
}
