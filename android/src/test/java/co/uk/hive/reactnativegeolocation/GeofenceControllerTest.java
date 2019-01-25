package co.uk.hive.reactnativegeolocation;

import co.uk.hive.reactnativegeolocation.geofence.Geofence;
import co.uk.hive.reactnativegeolocation.geofence.GeofenceController;
import co.uk.hive.reactnativegeolocation.geofence.GeofenceEngine;
import co.uk.hive.reactnativegeolocation.geofence.GeofenceRepository;
import com.annimon.stream.function.Function;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SuppressWarnings("Convert2Lambda")
@RunWith(MockitoJUnitRunner.class)
public class GeofenceControllerTest {

    @Mock
    private GeofenceEngine mGeofenceEngine;

    @Mock
    private GeofenceRepository mGeofenceRepository;

    @InjectMocks
    private GeofenceController mSut;

    private List<Geofence> mGeofences = Arrays.asList(
            TestData.createGeofence("1"),
            TestData.createGeofence("2"));

    private Function<? super Object, ? super Object> mCallback = new Function<Object, Object>() {
        @Override
        public Object apply(Object ignored) {
            return null;
        }
    };

    @Test
    public void startsGeofences() {
        given(mGeofenceRepository.getGeofences()).willReturn(mGeofences);

        mSut.start(mCallback, mCallback);

        verify(mGeofenceEngine).addGeofences(mGeofences, mCallback, mCallback);
    }

    @Test
    public void stopsGeofences() {
        given(mGeofenceRepository.getGeofences()).willReturn(mGeofences);

        mSut.stop(mCallback, mCallback);

        verify(mGeofenceEngine).removeGeofences(Arrays.asList("1", "2"), mCallback, mCallback);
    }

    @Test
    public void interactsWithRepository() {
        mSut.addGeofences(mGeofences);
        verify(mGeofenceRepository).addGeofences(mGeofences);

        mSut.removeAllGeofences();
        verify(mGeofenceRepository).removeAllGeofences();
    }
}