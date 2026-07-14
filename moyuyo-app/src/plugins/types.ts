/**
 * MOYUYO UTSDK 桥接协议定义
 *
 * 所有 uni-app 与原生插件的通信接口统一在此声明。
 * 调用端（TypeScript）与原生端（Kotlin / Swift）双方遵循此协议。
 *
 * 命名规范：方法名 camelCase，参数名 camelCase，回调统一 Promise
 */

// ============================================================
// 通用类型
// ============================================================

/** 插件调用结果 */
export interface PluginResult<T = void> {
  success: boolean
  data?: T
  error?: string
}

/** 3D 场景类型 */
export type PetScene = 'grassland' | 'livingroom' | 'training' | 'studio'

/** 支付渠道 */
export type PaymentChannel = 'apple_pay' | 'google_pay'

/** 社交登录提供商 */
export type AuthProvider = 'apple' | 'google' | 'facebook'

/** 支付结果 */
export interface PaymentResult {
  transactionId: string
  channel: PaymentChannel
  amount: number
  currency: string
  timestamp: number
}

/** 社交登录用户信息 */
export interface AuthUserInfo {
  provider: AuthProvider
  id: string
  email?: string
  name?: string
  avatar?: string
  accessToken: string
}

// ============================================================
// PetHub 3D 插件协议
// ============================================================

export interface PetHubPluginProtocol {
  /** 加载 3D 宠物模型 */
  loadPet(petId: string): Promise<PluginResult>
  /** 切换 IP 场景 */
  switchScene(scene: PetScene): Promise<PluginResult>
  /** 播放宠物动画 */
  playAnimation(animName: string): Promise<PluginResult>
  /** 宠物互动反馈（点击/抚摸） */
  onInteraction(type: string): Promise<PluginResult>
  /** 释放 3D 资源 */
  dispose(): Promise<PluginResult>
}

// ============================================================
// 支付插件协议
// ============================================================

export interface PaymentPluginProtocol {
  /** 发起支付 */
  pay(channel: PaymentChannel, orderInfo: Record<string, any>): Promise<PluginResult<PaymentResult>>
  /** 查询是否支持该支付渠道 */
  isAvailable(channel: PaymentChannel): Promise<PluginResult<boolean>>
}

// ============================================================
// 社交登录插件协议
// ============================================================

export interface AuthPluginProtocol {
  /** 发起社交登录 */
  login(provider: AuthProvider): Promise<PluginResult<AuthUserInfo>>
  /** 退出登录 */
  logout(provider: AuthProvider): Promise<PluginResult>
  /** 检查是否已授权 */
  isAuthorized(provider: AuthProvider): Promise<PluginResult<boolean>>
}
