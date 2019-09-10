//
//  HiveGeofence.swift
//  Presence
//
//  Created by Andrew on 30/07/2019.
//  Copyright © 2019 Centrica. All rights reserved.
//

import Foundation
import CoreLocation
import MapKit


@objc(RNHiveGeofence)
class RNHiveGeofence: NSObject, Codable {
    
    @objc var coordinate: CLLocationCoordinate2D
    @objc var radius: CLLocationDistance
    @objc var identifier: String
    @objc var notifyOnEntry: Bool
    @objc var notifyOnExit: Bool
    var extras: [String: Codable]?
    
    enum RNHiveGeofenceKeys: String, CodingKey {
        case latitude = "latitude"
        case longitude = "longitude"
        case radius = "radius"
        case identifier = "identifier"
        case notifyOnEntry = "notifyOnEntry"
        case notifyOnExit = "notifyOnExit"
        case extras = "extras"
    }
    
    init?(dictionary: [String: Any]) {
        guard let latitude = dictionary[RNHiveGeofenceKeys.latitude.rawValue] as? CLLocationDegrees,
            let longitude = dictionary[RNHiveGeofenceKeys.longitude.rawValue] as? CLLocationDegrees,
            let identifier = dictionary[RNHiveGeofenceKeys.identifier.rawValue] as? String,
            let notifyOnEntry = dictionary[RNHiveGeofenceKeys.notifyOnEntry.rawValue] as? Bool,
            let notifyOnExit = dictionary[RNHiveGeofenceKeys.notifyOnExit.rawValue] as? Bool,
            let radius = dictionary[RNHiveGeofenceKeys.radius.rawValue] as? CLLocationDistance else {
                return nil
        }
        self.identifier = identifier
        self.coordinate = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        self.radius = radius
        self.notifyOnEntry = notifyOnEntry
        self.notifyOnExit = notifyOnExit
        self.extras = dictionary[RNHiveGeofenceKeys.extras.rawValue] as? [String: Codable]
        
    }
    
    init(coordinate: CLLocationCoordinate2D, radius: CLLocationDistance, identifier: String = UUID().uuidString, notifyOnEntry: Bool = true, notifyOnExit: Bool = true, extras: [String: Codable]? = nil) {
        self.coordinate = coordinate
        self.radius = radius
        self.identifier = identifier
        self.notifyOnEntry = notifyOnEntry
        self.notifyOnExit = notifyOnExit
        self.extras = extras
    }
    
    var title: String? {
        return String(format: "Point at: %3.2f, %3.2f", coordinate.latitude, coordinate.longitude)
    }
    
    var subtitle: String? {
        return "Radius: \(radius)"
    }
    
    required init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: RNHiveGeofenceKeys.self)
        let latitude = try values.decode(Double.self, forKey: .latitude)
        let longitude = try values.decode(Double.self, forKey: .longitude)
        
        coordinate = CLLocationCoordinate2DMake(latitude, longitude)
        radius = try values.decode(Double.self, forKey: .radius)
        identifier = try values.decode(String.self, forKey: .identifier)
        notifyOnEntry = try values.decode(Bool.self, forKey: .notifyOnEntry)
        notifyOnExit = try values.decode(Bool.self, forKey: .notifyOnExit)
        
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: RNHiveGeofenceKeys.self)
        try container.encode(coordinate.latitude, forKey: .latitude)
        try container.encode(coordinate.longitude, forKey: .longitude)
        try container.encode(radius, forKey: .radius)
        try container.encode(identifier, forKey: .identifier)
        try container.encode(notifyOnEntry, forKey: .notifyOnEntry)
        try container.encode(notifyOnExit, forKey: .notifyOnExit)
        //        try container.encode(extras, forKey: .extras)
    }
    
    func updateValues(_ geofence: RNHiveGeofence) {
        self.coordinate = geofence.coordinate
        self.radius = geofence.radius
        self.notifyOnExit = geofence.notifyOnExit
        self.notifyOnEntry = geofence.notifyOnEntry
    }
}

extension RNHiveGeofence {
    enum RNHiveGeofenceDictionaryKeys: String, CodingKey {
        case identifier = "identifier"
    }
    
    @objc var dictionary: [String: Any]? {
        let dictionary = [RNHiveGeofenceDictionaryKeys.identifier.rawValue: self.identifier]
        return dictionary
    }
}
