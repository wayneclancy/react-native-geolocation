package co.uk.hive.reactnativegeolocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionChecker {

    private final Context mContext;

    public PermissionChecker(Context context) {
        mContext = context;
    }

    public boolean isLocationPermissionGranted() {
        final int status = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        return PackageManager.PERMISSION_GRANTED == status;
    }
}
