package co.uk.hive.reactnativegeolocation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.annimon.stream.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

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

    private Function<Void, Void> mSuccess = new Function<Void, Void>() {
        @Override
        public Void apply(Void aVoid) {
            return null;
        }
    };

    private Function<Throwable, Void> mFail = new Function<Throwable, Void>() {
        @Override
        public Void apply(Throwable throwable) {
            return null;
        }
    };

    @Test
    public void startsGeofences() {
        given(mGeofenceRepository.getGeofences()).willReturn(mGeofences);

        mSut.start(mSuccess, mFail);

        verify(mGeofenceEngine).addGeofences(mGeofences, mSuccess, mFail);
    }

    @Test
    public void stopsGeofences() {
        given(mGeofenceRepository.getGeofences()).willReturn(mGeofences);

        mSut.stop(mSuccess, mFail);

        verify(mGeofenceEngine).removeGeofences(Arrays.asList("1", "2"), mSuccess, mFail);
    }

    @Test
    public void interactsWithRepository() {
        mSut.addGeofences(mGeofences);
        verify(mGeofenceRepository).addGeofences(mGeofences);

        mSut.removeAllGeofences();
        verify(mGeofenceRepository).removeAllGeofences();
    }
}