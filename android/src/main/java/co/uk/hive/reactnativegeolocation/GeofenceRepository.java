package co.uk.hive.reactnativegeolocation;

import com.annimon.stream.Optional;

import java.util.List;

interface GeofenceRepository {
    void addGeofences(List<Geofence> geofences);

    void removeAllGeofences();

    List<Geofence> getGeofences();

    Optional<Geofence> getGeofenceById(String id);

    void setGeofencesActivated(boolean enabled);

    boolean areGeofencesEnabled();
}
