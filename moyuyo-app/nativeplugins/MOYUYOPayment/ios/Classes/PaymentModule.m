//
//  PaymentModule.m
//  MOYUYOPayment
//
//  支付原生插件（iOS）
//  功能：Apple Pay 支付
//

#import "PaymentModule.h"
#import <WeexSDK/WeexSDK.h>

@implementation PaymentModule

WX_EXPORT_METHOD(@selector(pay:callback:))
WX_EXPORT_METHOD(@selector(isAvailable:callback:))

- (void)pay:(NSDictionary *)params callback:(WXModuleCallback)callback {
    NSString *channel = params[@"channel"];
    // TODO: 调用 PassKit 发起 Apple Pay
    if (callback) callback(@{@"success": @YES, @"data": @{}});
}

- (void)isAvailable:(NSDictionary *)params callback:(WXModuleCallback)callback {
    // TODO: 检查 Apple Pay 是否可用
    if (callback) callback(@{@"success": @YES, @"data": @YES});
}

@end
