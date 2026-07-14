/**
 * 支付原生插件封装
 *
 * 调用端通过 uni.requireNativePlugin('MOYUYOPayment') 调用原生支付。
 * 降级策略：H5 端回退到 WebView 支付。
 */
import type { PaymentPluginProtocol, PaymentChannel, PluginResult, PaymentResult } from './types'

const PLUGIN_NAME = 'MOYUYOPayment'

let plugin: any = null

function getPlugin(): any {
  if (!plugin) {
    try {
      plugin = uni.requireNativePlugin(PLUGIN_NAME)
    } catch {
      console.warn('[Payment] 原生插件不可用')
      return null
    }
  }
  return plugin
}

function isNativeAvailable(): boolean {
  // #ifdef APP-PLUS
  return !!getPlugin()
  // #endif
  // #ifndef APP-PLUS
  return false
  // #endif
}

/**
 * 支付插件调用封装
 */
export function usePaymentPlugin(): PaymentPluginProtocol {
  const call = (method: string, args?: any): Promise<PluginResult> => {
    return new Promise((resolve) => {
      if (!isNativeAvailable()) {
        resolve({ success: false, error: '原生支付插件不可用' })
        return
      }
      getPlugin()[method](args, (res: any) => {
        resolve(res as PluginResult)
      })
    })
  }

  return {
    pay: (channel: PaymentChannel, orderInfo: Record<string, any>) =>
      call('pay', { channel, ...orderInfo }),
    isAvailable: (channel: PaymentChannel) =>
      call('isAvailable', { channel })
  }
}
