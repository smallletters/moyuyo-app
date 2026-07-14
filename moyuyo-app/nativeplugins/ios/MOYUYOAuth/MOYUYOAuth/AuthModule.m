#import "AuthModule.h"

@implementation AuthModule

UNI_EXPORT_METHOD_SYNC(loginWithApple, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @NO, @"message": @"Apple 登录请在 Xcode 中配置 Sign In with Apple 能力"});
})

UNI_EXPORT_METHOD_SYNC(loginWithGoogle, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @NO, @"message": @"Google 登录请配置 GoogleService-Info.plist"});
})

UNI_EXPORT_METHOD_SYNC(loginWithFacebook, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @NO, @"message": @"Facebook 登录请配置 FacebookAppID"});
})

UNI_EXPORT_METHOD_SYNC(isAppInstalled, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"installed": @NO});
})

UNI_EXPORT_METHOD_SYNC(logout, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

@end
