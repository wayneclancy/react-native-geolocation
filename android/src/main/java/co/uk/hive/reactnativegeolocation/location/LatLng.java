package co.uk.hive.reactnativegeolocation.location;

public class LatLng {
    private final double latitude;
    private final double longitude;
    private final double accuracyInMeters;

    public LatLng(double latitude, double longitude, double accuracyInMeters) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracyInMeters = accuracyInMeters;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccuracyInMeters() {
        return accuracyInMeters;
    }
}
