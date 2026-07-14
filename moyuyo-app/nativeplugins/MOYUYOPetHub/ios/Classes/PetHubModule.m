//
//  PetHubModule.m
//  MOYUYOPetHub
//
//  PetHub 3D 原生插件（iOS）
//  渲染引擎：SceneKit / RealityKit
//

#import "PetHubModule.h"
#import <WeexSDK/WeexSDK.h>

@implementation PetHubModule

WX_EXPORT_METHOD(@selector(loadPet:callback:))
WX_EXPORT_METHOD(@selector(switchScene:callback:))
WX_EXPORT_METHOD(@selector(playAnimation:callback:))
WX_EXPORT_METHOD(@selector(onInteraction:callback:))
WX_EXPORT_METHOD(@selector(dispose:callback:))

- (void)loadPet:(NSDictionary *)params callback:(WXModuleCallback)callback {
    NSString *petId = params[@"petId"];
    // TODO: SceneKit 引擎加载 3D 模型
    if (callback) callback(@{@"success": @YES});
}

- (void)switchScene:(NSDictionary *)params callback:(WXModuleCallback)callback {
    NSString *scene = params[@"scene"];
    // TODO: 切换 IP 场景
    if (callback) callback(@{@"success": @YES});
}

- (void)playAnimation:(NSDictionary *)params callback:(WXModuleCallback)callback {
    NSString *animName = params[@"animName"];
    // TODO: 播放骨骼动画
    if (callback) callback(@{@"success": @YES});
}

- (void)onInteraction:(NSDictionary *)params callback:(WXModuleCallback)callback {
    NSString *type = params[@"type"];
    // TODO: 互动反馈
    if (callback) callback(@{@"success": @YES});
}

- (void)dispose:(NSDictionary *)params callback:(WXModuleCallback)callback {
    // TODO: 释放资源
    if (callback) callback(@{@"success": @YES});
}

@end
