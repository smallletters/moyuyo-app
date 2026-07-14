#import "PetHubModule.h"
#import <DCUniModule/DCUniBaseModule.h>

@implementation PetHubModule

UNI_EXPORT_METHOD_SYNC(openScene, ^(NSDictionary *params, UniJSCallback callback) {
    NSString *petId = params[@"petId"] ?: @"";
    if (petId.length == 0) {
        callback(@{@"success": @NO, @"message": @"petId 不能为空"});
        return;
    }
    callback(@{@"success": @YES, @"data": @{@"message": @"场景已启动"}});
})

UNI_EXPORT_METHOD_SYNC(applyCosmetic, ^(NSDictionary *params, UniJSCallback callback) {
    NSString *petId = params[@"petId"] ?: @"";
    NSString *itemId = params[@"itemId"] ?: @"";
    if (petId.length == 0 || itemId.length == 0) {
        callback(@{@"success": @NO, @"message": @"参数不完整"});
        return;
    }
    callback(@{@"success": @YES, @"data": @{
        @"petId": petId,
        @"slot": params[@"slot"] ?: @"",
        @"itemId": itemId,
        @"status": @"applied"
    }});
})

UNI_EXPORT_METHOD_SYNC(playAnimation, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

UNI_EXPORT_METHOD_SYNC(captureSnapshot, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @NO, @"message": @"当前版本暂不支持截图"});
})

UNI_EXPORT_METHOD_SYNC(closeScene, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

@end
