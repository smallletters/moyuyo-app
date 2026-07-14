#import "PaymentModule.h"

@implementation PaymentModule

UNI_EXPORT_METHOD_SYNC(getAvailableChannels, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"channels": @[@"STRIPE", @"APPLE_PAY", @"PAYPAL"]});
})

UNI_EXPORT_METHOD_SYNC(pay, ^(NSDictionary *params, UniJSCallback callback) {
    NSString *orderNo = params[@"orderNo"] ?: @"";
    NSString *payMethod = params[@"payMethod"] ?: @"";
    if (orderNo.length == 0 || payMethod.length == 0) {
        callback(@{@"success": @NO, @"message": @"参数不完整"});
        return;
    }
    callback(@{@"success": @YES, @"transactionId": [NSString stringWithFormat:@"tx_%ld", (long)[NSDate date].timeIntervalSince1970]});
})

UNI_EXPORT_METHOD_SYNC(queryStatus, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES, @"status": @"completed"});
})

UNI_EXPORT_METHOD_SYNC(handleCallback, ^(NSDictionary *params, UniJSCallback callback) {
    callback(@{@"success": @YES});
})

@end
