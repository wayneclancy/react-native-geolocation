package co.uk.hive.reactnativegeolocation.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import co.uk.hive.reactnativegeolocation.LocationServicesChecker;

public class LocationController {
    private final Context mContext;
    private final FusedLocationProviderClient mLocationClient;
    private final LocationServicesChecker mLocationServicesChecker;

    public LocationController(Context context) {
        mContext = context;
        mLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationServicesChecker = new LocationServicesChecker(mContext);
    }

    @SuppressLint("MissingPermission")
    public void getCurrentPosition(CurrentPositionRequest currentPositionRequest,
                                   Function<LatLng, Object> successCallback,
                                   Function<Object, Object> failureCallback) {
        if (mLocationClient == null) {
            failureCallback.apply(LocationError.LOCATION_UNKNOWN);
            return;
        }

        if (!hasPermissions()) {
            failureCallback.apply(LocationError.PERMISSION_DENIED);
            return;
        }

        if (!isLocationEnabled()) {
            failureCallback.apply(LocationError.LOCATION_UNKNOWN);
            return;
        }

        mLocationClient.requestLocationUpdates(getLocationRequest(currentPositionRequest), new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    successCallback.apply(new LatLng(
                            locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude()));
                } else {
                    failureCallback.apply(LocationError.LOCATION_UNKNOWN);
                }
            }
        }, Looper.getMainLooper());
    }

    private boolean hasPermissions() {
        return Stream.of(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .allMatch(permission -> ActivityCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isLocationEnabled() {
        return mLocationServicesChecker.isLocationEnabled();
    }

    private LocationRequest getLocationRequest(CurrentPositionRequest currentPositionRequest) {
        return new LocationRequest()
                .setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
}
