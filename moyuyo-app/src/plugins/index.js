/**
 * 原生插件统一导出
 * - 提供所有原生插件的统一入口
 * - 支持 APP 端直接调用、H5 端降级处理
 */
export { createPay, getPaymentChannels } from './payment'
export { loginWithApple, loginWithGoogle, loginWithFacebook } from './auth'
export { openPetHub3d, applyPetCosmetic } from './petHub3d'
export { startArTryOn, captureArPhoto } from './arTryOn'
export { registerPush, handlePushMessage, getPushToken } from './push'
export { takePhoto, pickImage, compressImage } from './camera'
export { getCurrentLocation, openMapSelector } from './location'
export { authenticateBiometric, isBiometricAvailable } from './biometric'
