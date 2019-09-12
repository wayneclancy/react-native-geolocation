//
//  RNGeolocationTests.swift
//  RNGeolocationTests
//
//  Created by Andrew on 06/09/2019.
//  Copyright © 2019 Centrica. All rights reserved.
//

import XCTest
import CoreLocation
import UserNotifications

class RNGeolocationTests: XCTestCase {
    
    let mockGeofencesArrayDict: [[String: Any]] = [
        ["identifier": "arriving",
         "latitude": 51.50998,
         "longitude": -0.1337,
         "notifyOnEntry": true,
         "notifyOnExit": false,
         "radius": 1609.0,
         "extras": ["setTime": "Tue Sep 03 2019 17:23:47 GMT+0000",
                 "temperature": ["coolTemperature": 21.5,
                                 "heatTemperature": 14.5,
                                 "targetTemperature": 20]]
        ],
        
        ["identifier": "leaving",
         "latitude": 51.50998,
         "longitude": -0.1337,
         "notifyOnEntry": false,
         "notifyOnExit": true,
         "radius": 213.0,
         "extras": ["setTime": "Tue Sep 03 2019 17:23:47 GMT+0000",
                    "temperature": ["coolTemperature": 21.5,
                                    "heatTemperature": 14.5,
                                    "targetTemperature": 10]]
        ]
    ]
    
    let mockModifiedGeofencesArrayDict: [[String: Any]] = [
        ["identifier": "arriving",
         "latitude": 28.00,
         "longitude": 28.00,
         "notifyOnEntry": true,
         "notifyOnExit": false,
         "radius": 2000.0,
         "extras": ["setTime": "Tue Sep 03 2019 17:23:47 GMT+0000",
                    "temperature": ["coolTemperature": 21.5,
                                    "heatTemperature": 14.5,
                                    "targetTemperature": 20]]
        ],
        
        ["identifier": "leaving",
         "latitude": 28.00,
         "longitude": 28.00,
         "notifyOnEntry": false,
         "notifyOnExit": true,
         "radius": 100.0,
         "extras": ["setTime": "Tue Sep 03 2019 17:23:47 GMT+0000",
                    "temperature": ["coolTemperature": 21.5,
                                    "heatTemperature": 14.5,
                                    "targetTemperature": 10]]
        ]
    ]
    
    let mockEntryGeofenceDict: [String: Any] = ["identifier": "arriving",
                                                "latitude": 51.50998,
                                                "longitude": -0.1337,
                                                "notifyOnEntry": true,
                                                "notifyOnExit": false,
                                                "radius": 1609.0,
                                                "extras": ["setTime": "Tue Sep 03 2019 17:23:47 GMT+0000",
                                                           "temperature": ["coolTemperature": 21.5,
                                                                           "heatTemperature": 14.5,
                                                                           "targetTemperature": 20]]
                                                ]
    
    
    let mockExitGeofenceDict: [String: Any] = ["identifier": "leaving",
                                               "latitude": 51.50998,
                                               "longitude": -0.1337,
                                               "notifyOnEntry": false,
                                               "notifyOnExit": true,
                                               "radius": 213.0,
                                               "extras": ["setTime": "Tue Sep 03 2019 17:23:47 GMT+0000",
                                                          "temperature": ["coolTemperature": 21.5,
                                                                          "heatTemperature": 14.5,
                                                                          "targetTemperature": 10]]
                                                ]
    
    func test_RNHiveGeofence_Instantiation() {
        let geofence = RNHiveGeofence(dictionary: mockEntryGeofenceDict)
        XCTAssertNotNil(geofence)
        XCTAssertEqual(geofence!.identifier, "arriving")
        XCTAssertEqual(geofence!.coordinate.latitude, 51.50998, accuracy: 0.1)
        XCTAssertEqual(geofence!.coordinate.longitude, -0.1337, accuracy: 0.1)
        XCTAssertEqual(geofence!.radius, 1609)
        XCTAssertTrue(geofence!.notifyOnEntry)
        XCTAssertFalse(geofence!.notifyOnExit)
    }
    
    func test_CLLocation_to_Dictionary() {
        let location = CLLocation(coordinate: CLLocationCoordinate2D(latitude: 51.50998, longitude: -0.1337), altitude: 0, horizontalAccuracy: 5, verticalAccuracy: -1, timestamp: Date())
        let dict = location.dictionary
        XCTAssertNotNil(dict)
        XCTAssertNotNil(dict!["coords"])
        XCTAssertNotNil((dict!["coords"] as! [String: Any])["latitude"])
        XCTAssertEqual(((dict!["coords"] as! [String: Any])["latitude"] as! Double), 51.50998, accuracy: 0.1)
        XCTAssertEqual(((dict!["coords"] as! [String: Any])["longitude"] as! Double), -0.1337, accuracy: 0.1)
    }
    
    func test_RNHiveGeofenceEvent_Instantiation() {
        let entryGeofence = RNHiveGeofence(dictionary: mockEntryGeofenceDict)
        let entryLocation = CLLocation(coordinate: CLLocationCoordinate2D(latitude: 51.50998, longitude: -0.1337), altitude: 0, horizontalAccuracy: 5, verticalAccuracy: -1, timestamp: Date())
        let entryRegion = CLCircularRegion(center: entryGeofence!.coordinate, radius: entryGeofence!.radius, identifier: entryGeofence!.identifier)
        let entryGeofenceEvent = RNHiveGeofenceEvent(geofence: entryGeofence!, location: entryLocation, region: entryRegion, time: Date(), type: .entry)
        
        let entryEventDict = entryGeofenceEvent.dictionary
        XCTAssertNotNil(entryEventDict)
        XCTAssertNotNil(entryEventDict!["action"])
        XCTAssertNotNil(entryEventDict!["identifier"])
        XCTAssertNotNil(entryEventDict!["location"])
        XCTAssertNotNil(entryEventDict!["geofence"])
        XCTAssert((entryEventDict!["action"] as! String) == "ENTER")
        XCTAssert((entryEventDict!["identifier"] as! String) == "arriving")
        
        
        let exitGeofence = RNHiveGeofence(dictionary: mockExitGeofenceDict)
        let exitLocation = CLLocation(coordinate: CLLocationCoordinate2D(latitude: 51.50998, longitude: -0.1337), altitude: 0, horizontalAccuracy: 5, verticalAccuracy: -1, timestamp: Date())
        let exitRegion = CLCircularRegion(center: exitGeofence!.coordinate, radius: exitGeofence!.radius, identifier: exitGeofence!.identifier)
        let exitGeofenceEvent = RNHiveGeofenceEvent(geofence: exitGeofence!, location: exitLocation, region: exitRegion, time: Date(), type: .exit)
        
        let exitEventDict = exitGeofenceEvent.dictionary
        XCTAssertNotNil(exitEventDict)
        XCTAssertNotNil(exitEventDict!["action"])
        XCTAssertNotNil(exitEventDict!["identifier"])
        XCTAssertNotNil(exitEventDict!["location"])
        XCTAssertNotNil(exitEventDict!["geofence"])
        XCTAssert((exitEventDict!["action"] as! String) == "EXIT")
        XCTAssert((exitEventDict!["identifier"] as! String) == "leaving")
        
    }
    
    func test_RNHiveGeofenceManager_AddGeofences() {
        let geofenceManager = RNHiveGeolocationManager()
        geofenceManager.addGeofences(array: mockGeofencesArrayDict)
        let geofences = geofenceManager.allGeofences()
        XCTAssertNotNil(geofences)
        XCTAssertEqual(geofences.count, 2)
        
        let entryGeofence = geofences.first
        XCTAssertEqual(entryGeofence!.identifier, "arriving")
        XCTAssertEqual(entryGeofence!.coordinate.latitude, 51.50998, accuracy: 0.1)
        XCTAssertEqual(entryGeofence!.coordinate.longitude, -0.1337, accuracy: 0.1)
        XCTAssertEqual(entryGeofence!.radius, 1609)
        XCTAssertTrue(entryGeofence!.notifyOnEntry)
        XCTAssertFalse(entryGeofence!.notifyOnExit)
        
        let exitGeofence = geofences[1]
        XCTAssertEqual(exitGeofence.identifier, "leaving")
        XCTAssertEqual(exitGeofence.coordinate.latitude, 51.50998, accuracy: 0.1)
        XCTAssertEqual(exitGeofence.coordinate.longitude, -0.1337, accuracy: 0.1)
        XCTAssertEqual(exitGeofence.radius, 213)
        XCTAssertFalse(exitGeofence.notifyOnEntry)
        XCTAssertTrue(exitGeofence.notifyOnExit)
    }
    
    func test_RNHiveGeofenceManager_ModifyGeofences() {
        let geofenceManager = RNHiveGeolocationManager()
        geofenceManager.addGeofences(array: mockGeofencesArrayDict)
        geofenceManager.addGeofences(array: mockModifiedGeofencesArrayDict)
        
        let geofences = geofenceManager.allGeofences()
        XCTAssertNotNil(geofences)
        XCTAssertEqual(geofences.count, 2)
        
        let entryGeofence = geofences.first
        XCTAssertEqual(entryGeofence!.identifier, "arriving")
        XCTAssertEqual(entryGeofence!.coordinate.latitude, 28.00, accuracy: 0.1)
        XCTAssertEqual(entryGeofence!.coordinate.longitude, 28.00, accuracy: 0.1)
        XCTAssertEqual(entryGeofence!.radius, 2000)
        XCTAssertTrue(entryGeofence!.notifyOnEntry)
        XCTAssertFalse(entryGeofence!.notifyOnExit)
        
        let exitGeofence = geofences[1]
        XCTAssertEqual(exitGeofence.identifier, "leaving")
        XCTAssertEqual(exitGeofence.coordinate.latitude, 28.00, accuracy: 0.1)
        XCTAssertEqual(exitGeofence.coordinate.longitude, 28.00, accuracy: 0.1)
        XCTAssertEqual(exitGeofence.radius, 100)
        XCTAssertFalse(exitGeofence.notifyOnEntry)
        XCTAssertTrue(exitGeofence.notifyOnExit)
    }
    
    func test_RNHiveGeofenceManager_StartMonitoringGeofences() {
        let geofenceManager = RNHiveGeolocationManager()
        geofenceManager.addGeofences(array: mockGeofencesArrayDict)
        geofenceManager.startMonitoringGeofences(nil)
        var monitoredRegions = geofenceManager.monitoredRegions()
        XCTAssertNotNil(monitoredRegions)
        XCTAssertEqual(monitoredRegions.count, 2)
        
        let entryRegion = monitoredRegions.first

        XCTAssertEqual(entryRegion!.identifier, "arriving")
        XCTAssertEqual(entryRegion!.center.latitude, 51.50998, accuracy: 0.1)
        XCTAssertEqual(entryRegion!.center.longitude, -0.1337, accuracy: 0.1)
        XCTAssertEqual(entryRegion!.radius, 1609)
        XCTAssertTrue(entryRegion!.notifyOnEntry)
        XCTAssertFalse(entryRegion!.notifyOnExit)
        
        let exitRegion = monitoredRegions[1]
        
        XCTAssertEqual(exitRegion.identifier, "leaving")
        XCTAssertEqual(exitRegion.center.latitude, 51.50998, accuracy: 0.1)
        XCTAssertEqual(exitRegion.center.longitude, -0.1337, accuracy: 0.1)
        XCTAssertEqual(exitRegion.radius, 213)
        XCTAssertFalse(exitRegion.notifyOnEntry)
        XCTAssertTrue(exitRegion.notifyOnExit)
        
        geofenceManager.stopMonitoringGeofences(nil)
        monitoredRegions = geofenceManager.monitoredRegions()
        XCTAssertNotNil(monitoredRegions)
        XCTAssertEqual(monitoredRegions.count, 0)

    }

}
