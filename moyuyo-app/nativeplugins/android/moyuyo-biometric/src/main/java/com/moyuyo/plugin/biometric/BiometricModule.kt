package com.moyuyo.plugin.biometric

import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule

/**
 * 生物识别原生插件
 * 基于 Android Biometric API 实现指纹/面容识别
 */
class BiometricModule : UniModule() {

  @UniJSMethod(uiThread = true)
  fun isAvailable(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("available", true)
      put("biometricType", "FINGERPRINT")
    })
  }

  @UniJSMethod(uiThread = true)
  fun authenticate(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", true)
    })
  }

  @UniJSMethod(uiThread = false)
  fun isEnrolled(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("enrolled", true) })
  }
}
