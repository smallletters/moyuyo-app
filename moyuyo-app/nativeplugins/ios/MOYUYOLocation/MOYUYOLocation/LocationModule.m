#import "LocationModule.h"

@implementation LocationModule

UNI_EXPORT_METHOD_SYNC(getCurrentLocation, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES, @"latitude": @0.0, @"longitude": @0.0, @"address": @"请在原生端实现定位功能"});
})

UNI_EXPORT_METHOD_SYNC(openMapSelector, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @NO, @"message": @"请在原生端实现地图选择器"});
})

UNI_EXPORT_METHOD_SYNC(showLogisticsMap, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

UNI_EXPORT_METHOD_SYNC(geocode, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES, @"latitude": @0.0, @"longitude": @0.0});
})

@end
