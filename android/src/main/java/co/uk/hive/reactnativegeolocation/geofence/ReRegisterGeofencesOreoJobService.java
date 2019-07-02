package co.uk.hive.reactnativegeolocation.geofence;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import co.uk.hive.reactnativegeolocation.LocationChecker;
import co.uk.hive.reactnativegeolocation.PermissionChecker;

import com.annimon.stream.function.Function;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReRegisterGeofencesOreoJobService extends JobService {

    private Function<? super Object, ? super Object> mEmptyCallback = o -> null;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        LocationChecker locationChecker = new LocationChecker(this);
        if (locationChecker.isLocationEnabled() && isLocationPermissionGranted()) {
            GeofenceController geofenceController = GeofenceServiceLocator.getGeofenceController(this);
            geofenceController.restart(mEmptyCallback, mEmptyCallback);
        }

        ReRegistrationScheduler scheduler = new ReRegistrationScheduler(this);
        scheduler.scheduleReRegistration();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private boolean isLocationPermissionGranted() {
        return new PermissionChecker(this).isLocationPermissionGranted();
    }
}
