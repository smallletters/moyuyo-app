/**
 * 原生插件桥接层入口
 *
 * 统一管理所有 uni-app 原生插件调用：
 * - PetHubPlugin  — 3D 宠物模型渲染（Filament / SceneKit）
 * - PaymentPlugin — 支付 SDK（Apple Pay / Google Pay）
 * - AuthPlugin    — 社交登录（Apple Sign-In / Google / Facebook）
 */
export { usePetHubPlugin } from './pethub'
export { usePaymentPlugin } from './payment'
export { useAuthPlugin } from './auth'
