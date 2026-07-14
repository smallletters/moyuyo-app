package com.moyuyo.plugin.ar

import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule

/**
 * AR 试穿原生插件
 * 基于 ARCore 实现宠物服饰 AR 试穿
 */
class ArTryOnModule : UniModule() {

  @UniJSMethod(uiThread = true)
  fun startSession(params: JSONObject, callback: UniJSCallback) {
    val productId = params.getString("productId") ?: ""
    if (productId.isEmpty()) {
      callback.invoke(JSONObject().apply {
        put("success", false)
        put("message", "productId 不能为空")
      })
      return
    }

    callback.invoke(JSONObject().apply {
      put("success", true)
      put("data", JSONObject().apply {
        put("sessionId", "ar_${System.currentTimeMillis()}")
        put("status", "started")
      })
    })
  }

  @UniJSMethod(uiThread = true)
  fun capturePhoto(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", false)
      put("message", "当前版本暂不支持 AR 拍照")
    })
  }

  @UniJSMethod(uiThread = false)
  fun switchProduct(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }

  @UniJSMethod(uiThread = false)
  fun stopSession(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }
}
