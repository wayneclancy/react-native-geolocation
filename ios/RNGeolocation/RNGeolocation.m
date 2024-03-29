//
//  RNGeolocation.m
//  RNGeolocation
//
//  Created by Andrew on 30/08/2019.
//  Copyright © 2019 Centrica. All rights reserved.
//

#import "RNGeolocation.h"
#import <CoreLocation/CoreLocation.h>
#import "RNGeolocation-Swift.h"

static NSString *const EVENT_GEOFENCE = @"geofence";

@interface RNGeolocation() <CLLocationManagerDelegate>

@property (nonatomic, strong) RNHiveGeolocationManager *locationManager;

@end

@implementation RNGeolocation {
    NSMutableDictionary *listeners;
}

RCT_EXPORT_MODULE();

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (instancetype)init {
    self = [super init];
    self.locationManager = [[RNHiveGeolocationManager alloc] init];
    return self;
}

- (NSArray<NSString *> *)supportedEvents {
    return @[EVENT_GEOFENCE];
}

- (void)invalidate {
    @synchronized(listeners) {
        [listeners removeAllObjects];
    }
}

#pragma mark - React Native API Methods

RCT_EXPORT_METHOD(addEventListener:(NSString *)event) {
    @synchronized(listeners) {
        if ([listeners objectForKey:event]) {
            // Increment listener-count for this event
            NSInteger count = [[listeners objectForKey:event] integerValue];
            count++;
            [listeners setObject:@(count) forKey:event];
        } else {
            // First listener for this event
            [listeners setObject:@(1) forKey:event];
            if ([event isEqualToString:EVENT_GEOFENCE]) {
                __typeof(self) __weak weakSelf = self;
                [self.locationManager onGeofenceEventWithResponder:^(RNHiveGeofenceEvent * _Nullable geofenceEvent, NSError * _Nullable error) {
                    [weakSelf sendEvent:EVENT_GEOFENCE body:geofenceEvent.dictionary];
                }];
            }
        }
    }
}

RCT_EXPORT_METHOD(ready) {
    [self.locationManager configure];
    __typeof(self) __weak weakSelf = self;
    [self.locationManager onGeofenceEventWithResponder:^(RNHiveGeofenceEvent * _Nullable geofenceEvent, NSError * _Nullable error) {
        [weakSelf sendEvent:EVENT_GEOFENCE body:geofenceEvent.dictionary];
    }];
}

RCT_EXPORT_METHOD(startGeofences:(RCTResponseSenderBlock)success failure:(RCTResponseSenderBlock)failure) {
    [self.locationManager startMonitoringGeofences:^(NSArray<CLCircularRegion *> * _Nullable regions, NSError * _Nullable error) {
        if (error) {
            failure(@[@(error.code)]);
        } else {
            success(@[]);
        }
    }];
}

RCT_EXPORT_METHOD(stopGeofences:(RCTResponseSenderBlock)success failure:(RCTResponseSenderBlock)failure) {
    [self.locationManager stopMonitoringGeofences:^(NSArray<CLCircularRegion *> * _Nullable regions, NSError * _Nullable error) {
        if (error) {
            failure(@[@(error.code)]);
        } else {
            success(@[]);
        }
    }];
}

RCT_EXPORT_METHOD(addGeofences:(NSArray <NSDictionary *> *)geofences) {
    [self.locationManager addGeofencesWithArray:geofences];
}

RCT_EXPORT_METHOD(removeGeofences) {
    [self.locationManager removeAllGeofences];
}

RCT_EXPORT_METHOD(getCurrentPosition:(NSDictionary *)options success:(RCTResponseSenderBlock)success failure:(RCTResponseSenderBlock)failure) {
    [self.locationManager requestLocationWithCompletion:^(NSArray<CLLocation *> * _Nullable locations, NSError * _Nullable error) {
        if (!error && locations != nil) {
            success(@[[locations.firstObject dictionary]]);
        } else {
            failure(@[@(error.code)]);
        }
    }];
}

- (void)sendEvent:(NSString *)event body:(id)body {
    [self sendEventWithName:event body:body];
}

@end

