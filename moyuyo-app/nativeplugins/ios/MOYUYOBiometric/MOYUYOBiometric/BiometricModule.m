#import "BiometricModule.h"
#import <LocalAuthentication/LocalAuthentication.h>

@implementation BiometricModule

UNI_EXPORT_METHOD_SYNC(isAvailable, ^(NSDictionary *params, UniJSCallback callback) {
    LAContext *context = [[LAContext alloc] init];
    NSError *error = nil;
    BOOL available = [context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error];

    NSString *type = @"NONE";
    if (available) {
        if (@available(iOS 11.0, *)) {
            type = context.biometryType == LABiometryTypeFaceID ? @"FACE_ID" : @"TOUCH_ID";
        } else {
            type = @"TOUCH_ID";
        }
    }
    callback(@{@"available": @(available), @"biometricType": type});
})

UNI_EXPORT_METHOD_SYNC(authenticate, ^(NSDictionary *params, UniJSCallback callback) {
    LAContext *context = [[LAContext alloc] init];
    NSString *reason = params[@"reason"] ?: @"验证身份以继续";

    [context evaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics
        localizedReason:reason
        reply:^(BOOL success, NSError * _Nullable error) {
            if (success) {
                callback(@{@"success": @YES});
            } else {
                callback(@{@"success": @NO, @"message": error.localizedDescription ?: @"身份验证失败"});
            }
        }];
})

UNI_EXPORT_METHOD_SYNC(isEnrolled, ^(NSDictionary *params, UniJSCallback callback) {
    LAContext *context = [[LAContext alloc] init];
    NSError *error = nil;
    BOOL available = [context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error];
    callback(@{@"enrolled": @(available && error == nil)});
})

@end
