package com.moyuyo.plugin.push

import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule

/**
 * 推送通知原生插件
 * 封装 FCM（Firebase Cloud Messaging）
 */
class PushModule : UniModule() {

  @UniJSMethod(uiThread = true)
  fun register(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", true)
      put("token", "fcm_placeholder_token")
    })
  }

  @UniJSMethod(uiThread = true)
  fun getToken(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("token", "fcm_placeholder_token") })
  }

  @UniJSMethod(uiThread = false)
  fun handleMessage(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }

  @UniJSMethod(uiThread = false)
  fun setBadge(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }

  @UniJSMethod(uiThread = false)
  fun clearBadge(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }

  @UniJSMethod(uiThread = false)
  fun setEnabled(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }

  @UniJSMethod(uiThread = false)
  fun isEnabled(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("enabled", true) })
  }
}
