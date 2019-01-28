import { Platform } from 'react-native';
import GeolocationIOS from './index.ios.js';
import GeolocationAndroid from './index.android.js';

const Geolocation = Platform.OS === 'ios'
  ? GeolocationIOS
  : GeolocationAndroid;

export default Geolocation;
