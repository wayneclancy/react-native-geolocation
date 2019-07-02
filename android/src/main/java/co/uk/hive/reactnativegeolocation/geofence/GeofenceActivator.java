package co.uk.hive.reactnativegeolocation.geofence;

public interface GeofenceActivator {
    void setGeofencesActivated(boolean enabled);

    boolean areGeofencesActivated();

    boolean isFailedReRegistration();

    void setFailedReRegistration(boolean value);
}
