/**
 * 社交登录原生插件封装
 *
 * 调用端通过 uni.requireNativePlugin('MOYUYOAuth') 调用原生社交登录。
 * 降级策略：H5 端使用 WebView OAuth 跳转。
 */
import type { AuthPluginProtocol, AuthProvider, PluginResult, AuthUserInfo } from './types'

const PLUGIN_NAME = 'MOYUYOAuth'

let plugin: any = null

function getPlugin(): any {
  if (!plugin) {
    try {
      plugin = uni.requireNativePlugin(PLUGIN_NAME)
    } catch {
      console.warn('[Auth] 原生插件不可用')
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
 * 社交登录插件调用封装
 */
export function useAuthPlugin(): AuthPluginProtocol {
  const call = (method: string, args?: any): Promise<PluginResult> => {
    return new Promise((resolve) => {
      if (!isNativeAvailable()) {
        resolve({ success: false, error: '原生登录插件不可用' })
        return
      }
      getPlugin()[method](args, (res: any) => {
        resolve(res as PluginResult)
      })
    })
  }

  return {
    login: (provider: AuthProvider) => call('login', { provider }),
    logout: (provider: AuthProvider) => call('logout', { provider }),
    isAuthorized: (provider: AuthProvider) => call('isAuthorized', { provider }),
  }
}
