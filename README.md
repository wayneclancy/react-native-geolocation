
# react-native-geolocation

## Getting started

`$ npm install react-native-geolocation --save`

### Mostly automatic installation

`$ react-native link react-native-geolocation`

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNGeolocationPackage;` to the imports at the top of the file
  - Add `new RNGeolocationPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-geolocation'
  	project(':react-native-geolocation').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-geolocation/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-geolocation')
  	```

## Usage
```javascript
import RNGeolocation from 'react-native-geolocation';

// TODO: What to do with the module?
RNGeolocation;
```
  
