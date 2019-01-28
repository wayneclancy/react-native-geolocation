package co.uk.hive.reactnativegeolocation.location;

/**
 * Only timeout parameter is used at the moment.
 *
 * See https://transistorsoft.github.io/react-native-background-geolocation/interfaces/_react_native_background_geolocation_.currentpositionrequest.html
 */
public class CurrentPositionRequest {
    private final int timeout;

    public CurrentPositionRequest(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }
}
