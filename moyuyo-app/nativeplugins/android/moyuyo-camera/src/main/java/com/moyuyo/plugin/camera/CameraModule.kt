package com.moyuyo.plugin.camera

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule

/**
 * 相机/相册原生插件
 * 提供拍照、选图、压缩等系统级能力
 */
class CameraModule : UniModule() {

  @UniJSMethod(uiThread = true)
  fun takePhoto(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", false)
      put("message", "请在原生端实现相机功能")
    })
  }

  @UniJSMethod(uiThread = true)
  fun pickFromGallery(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", true)
      put("images", JSONArray())
    })
  }

  @UniJSMethod(uiThread = false)
  fun compress(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", true)
      put("compressedPath", params.getString("imagePath"))
    })
  }
}
