/**
 * PetHub 3D 原生插件封装
 *
 * 调用端通过 uni.requireNativePlugin('MOYUYOPetHub') 与原生通信。
 * 降级策略：App 端调用原生插件，H5/小程序端使用静态图片占位。
 */
import type { PetHubPluginProtocol, PetScene, PluginResult } from './types'

const PLUGIN_NAME = 'MOYUYOPetHub'

/** 单例插件引用 */
let plugin: any = null

function getPlugin(): any {
  if (!plugin) {
    try {
      plugin = uni.requireNativePlugin(PLUGIN_NAME)
    } catch {
      console.warn('[PetHub] 原生插件不可用，使用降级模式')
      return null
    }
  }
  return plugin
}

/** 判断当前环境是否支持原生插件 */
function isNativeAvailable(): boolean {
  // #ifdef APP-PLUS
  return !!getPlugin()
  // #endif
  // #ifndef APP-PLUS
  return false
  // #endif
}

/**
 * PetHub 插件调用封装
 */
export function usePetHubPlugin(): PetHubPluginProtocol {
  const call = (method: string, args?: any): Promise<PluginResult> => {
    return new Promise((resolve) => {
      if (!isNativeAvailable()) {
        resolve({ success: false, error: '原生插件不可用' })
        return
      }
      getPlugin()[method](args, (res: any) => {
        resolve(res as PluginResult)
      })
    })
  }

  return {
    loadPet: (petId: string) => call('loadPet', { petId }),
    switchScene: (scene: PetScene) => call('switchScene', { scene }),
    playAnimation: (animName: string) => call('playAnimation', { animName }),
    onInteraction: (type: string) => call('onInteraction', { type }),
    dispose: () => call('dispose')
  }
}
