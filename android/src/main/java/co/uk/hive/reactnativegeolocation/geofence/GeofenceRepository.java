package co.uk.hive.reactnativegeolocation.geofence;

import com.annimon.stream.Optional;

import java.util.List;

public interface GeofenceRepository {
    void addGeofences(List<Geofence> geofences);

    void removeAllGeofences();

    List<Geofence> getGeofences();

    Optional<Geofence> getGeofenceById(String id);

    void setGeofencesActivated(boolean enabled);

    boolean areGeofencesEnabled();
}
