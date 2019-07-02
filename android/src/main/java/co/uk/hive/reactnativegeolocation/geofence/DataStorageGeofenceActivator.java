package co.uk.hive.reactnativegeolocation.geofence;

import co.uk.hive.reactnativegeolocation.DataMarshaller;
import co.uk.hive.reactnativegeolocation.DataStorage;

class DataStorageGeofenceActivator implements GeofenceActivator {

    private static final String KEY_ACTIVATED = "key_activated";
    private static final String KEY_FAILED_REGISTRATION = "key_failed_reregistration";

    private boolean mActivated;
    private boolean mFailedReRegistration;

    private final DataStorage mDataStorage;
    private final DataMarshaller mDataMarshaller;

    public DataStorageGeofenceActivator(DataStorage dataStorage, DataMarshaller dataMarshaller) {
        mDataStorage = dataStorage;
        mDataMarshaller = dataMarshaller;
        load();
    }

    @Override
    public void setGeofencesActivated(boolean activated) {
        mActivated = activated;
        save();
    }

    @Override
    public boolean areGeofencesActivated() {
        return mActivated;
    }

    @Override
    public boolean isFailedReRegistration() {
        return mFailedReRegistration;
    }

    @Override
    public void setFailedReRegistration(boolean value) {
        mFailedReRegistration = value;
        save();
    }

    private void save() {
        mDataStorage.store(KEY_ACTIVATED, mDataMarshaller.marshal(mActivated));
        mDataStorage.store(KEY_FAILED_REGISTRATION, mDataMarshaller.marshal(mFailedReRegistration));
    }

    private void load() {
        mActivated = mDataMarshaller.unmarshal(mDataStorage.load(KEY_ACTIVATED), Boolean.class, mActivated);
        mFailedReRegistration = mDataMarshaller.unmarshal(mDataStorage.load(KEY_FAILED_REGISTRATION), Boolean.class, mFailedReRegistration);
    }
}
