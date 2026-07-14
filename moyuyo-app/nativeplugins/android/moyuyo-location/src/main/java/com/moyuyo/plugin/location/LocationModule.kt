package com.moyuyo.plugin.location

import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule

/**
 * 定位/地图原生插件
 * 封装 Google Maps / Fused Location Provider
 */
class LocationModule : UniModule() {

  @UniJSMethod(uiThread = true)
  fun getCurrentLocation(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", true)
      put("latitude", 0.0)
      put("longitude", 0.0)
      put("address", "请在原生端实现定位功能")
      put("country", "")
      put("city", "")
    })
  }

  @UniJSMethod(uiThread = true)
  fun openMapSelector(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", false)
      put("message", "请在原生端实现地图选择器")
    })
  }

  @UniJSMethod(uiThread = false)
  fun showLogisticsMap(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }

  @UniJSMethod(uiThread = false)
  fun geocode(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", true)
      put("latitude", 0.0)
      put("longitude", 0.0)
    })
  }
}
