import { NativeModules, AppRegistry, DeviceEventEmitter } from 'react-native';

const TAG = "BackgroundGeolocation"

const { Geolocation } = NativeModules;

export default class RNGeolocation {

  static registerHeadlessTask(task) {
    AppRegistry.registerHeadlessTask(TAG, () => task);
  }

  static ready() {
    Geolocation.ready()
  }

  static onGeofence(geofenceListener) {
    DeviceEventEmitter.addListener('geofence', geofenceListener)
  }

  static startGeofences(successCallback, failureCallback) {
    Geolocation.startGeofences(successCallback, failureCallback)
  }

  static stop(successCallback, failureCallback) {
    Geolocation.stopGeofences(successCallback, failureCallback)
  }

  static addGeofences(geofences) {
    Geolocation.addGeofences(geofences)
  }

  static removeGeofences() {
    Geolocation.removeGeofences()
  }

  static getCurrentPosition(currentPositionRequest, successCallback, failureCallback) {
    Geolocation.getCurrentPosition(currentPositionRequest, successCallback, failureCallback)
  }
}
