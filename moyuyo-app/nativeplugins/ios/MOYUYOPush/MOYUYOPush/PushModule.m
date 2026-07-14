#import "PushModule.h"

@implementation PushModule

UNI_EXPORT_METHOD_SYNC(register, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES, @"token": @"apns_placeholder_token"});
})

UNI_EXPORT_METHOD_SYNC(getToken, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"token": @"apns_placeholder_token"});
})

UNI_EXPORT_METHOD_SYNC(handleMessage, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

UNI_EXPORT_METHOD_SYNC(setBadge, ^(NSDictionary *params, UniJSCallback callback) {
    NSInteger count = [params[@"count"] integerValue];
    [UIApplication sharedApplication].applicationIconBadgeNumber = count;
    callback(@{@"success": @YES});
})

UNI_EXPORT_METHOD_SYNC(clearBadge, ^(NSDictionary *params, UniJSCallback callback) {
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
    callback(@{@"success": @YES});
})

UNI_EXPORT_METHOD_SYNC(setEnabled, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

UNI_EXPORT_METHOD_SYNC(isEnabled, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"enabled": @YES});
})

@end
