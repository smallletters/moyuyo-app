#import "CameraModule.h"

@implementation CameraModule

UNI_EXPORT_METHOD_SYNC(takePhoto, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @NO, @"message": @"请使用 UIImagePickerController 实现相机功能"});
})

UNI_EXPORT_METHOD_SYNC(pickFromGallery, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES, @"images": @[]});
})

UNI_EXPORT_METHOD_SYNC(compress, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES, @"compressedPath": params[@"imagePath"] ?: @""});
})

@end
