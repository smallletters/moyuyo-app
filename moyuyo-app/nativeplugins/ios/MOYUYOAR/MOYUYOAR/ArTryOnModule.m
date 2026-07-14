#import "ArTryOnModule.h"

@implementation ArTryOnModule

UNI_EXPORT_METHOD_SYNC(startSession, ^(NSDictionary *params, UniJSCallback callback) {
    NSString *productId = params[@"productId"] ?: @"";
    if (productId.length == 0) {
        callback(@{@"success": @NO, @"message": @"productId 不能为空"});
        return;
    }
    callback(@{@"success": @YES, @"data": @{
        @"sessionId": [NSString stringWithFormat:@"ar_%ld", (long)[NSDate date].timeIntervalSince1970],
        @"status": @"started"
    }});
})

UNI_EXPORT_METHOD_SYNC(capturePhoto, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @NO, @"message": @"当前版本暂不支持 AR 拍照"});
})

UNI_EXPORT_METHOD_SYNC(switchProduct, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

UNI_EXPORT_METHOD_SYNC(stopSession, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

@end
