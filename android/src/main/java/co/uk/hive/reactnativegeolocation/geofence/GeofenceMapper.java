package co.uk.hive.reactnativegeolocation.geofence;

import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.location.GeofencingEvent;

import static com.google.android.gms.location.Geofence.*;

class GeofenceMapper {
    Bundle toBundle(GeofencingEvent event, Geofence geofence) {
        Bundle bundle = new Bundle();
        bundle.putString("action", getGeofenceAction(event.getGeofenceTransition()));
        bundle.putString("identifier", geofence.getId());
        bundle.putBundle("extras", geofence.getExtras());
        bundle.putBundle("location", getLocationBundle(event.getTriggeringLocation()));
        return bundle;
    }

    private Bundle getLocationBundle(Location triggeringLocation) {
        Bundle bundle = new Bundle();
        Bundle coordsBundle = new Bundle();
        coordsBundle.putDouble("latitude", triggeringLocation.getLatitude());
        coordsBundle.putDouble("longitude", triggeringLocation.getLongitude());
        bundle.putBundle("coords", coordsBundle);
        return bundle;
    }

    private String getGeofenceAction(int geofenceTransition) {
        switch (geofenceTransition) {
            case GEOFENCE_TRANSITION_ENTER:
                return "ENTER";
            case GEOFENCE_TRANSITION_EXIT:
                return "EXIT";
            case GEOFENCE_TRANSITION_DWELL:
                return "DWELL";
            default:
                return null;
        }
    }
}
