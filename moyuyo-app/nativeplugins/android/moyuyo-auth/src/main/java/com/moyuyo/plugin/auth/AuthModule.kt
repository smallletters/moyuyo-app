package com.moyuyo.plugin.auth

import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule
import android.app.Activity
import android.content.Intent

/**
 * 社交登录原生插件
 * 封装 Google Sign-In / Facebook Login / Apple Sign-In
 */
class AuthModule : UniModule() {

  @UniJSMethod(uiThread = true)
  fun loginWithGoogle(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", false)
      put("message", "Google 登录请在原生端配置 OAuth 客户端 ID")
    })
  }

  @UniJSMethod(uiThread = true)
  fun loginWithApple(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", false)
      put("message", "Android 端不支持 Apple 登录")
    })
  }

  @UniJSMethod(uiThread = true)
  fun loginWithFacebook(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", false)
      put("message", "Facebook 登录请在原生端配置 App ID")
    })
  }

  @UniJSMethod(uiThread = false)
  fun isAppInstalled(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("installed", false) })
  }

  @UniJSMethod(uiThread = false)
  fun logout(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }
}
