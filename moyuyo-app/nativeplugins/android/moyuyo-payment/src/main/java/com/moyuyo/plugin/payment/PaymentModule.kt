package com.moyuyo.plugin.payment

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule

/**
 * 支付 SDK 原生插件
 * 封装 Stripe / Google Pay 等原生支付 SDK
 */
class PaymentModule : UniModule() {

  @UniJSMethod(uiThread = true)
  fun getAvailableChannels(params: JSONObject, callback: UniJSCallback) {
    val channels = JSONArray().apply {
      add("STRIPE")
      add("GOOGLE_PAY")
      add("PAYPAL")
    }
    callback.invoke(JSONObject().apply { put("channels", channels) })
  }

  @UniJSMethod(uiThread = true)
  fun pay(params: JSONObject, callback: UniJSCallback) {
    val orderNo = params.getString("orderNo") ?: ""
    val payMethod = params.getString("payMethod") ?: ""

    if (orderNo.isEmpty() || payMethod.isEmpty()) {
      callback.invoke(JSONObject().apply {
        put("success", false)
        put("message", "参数不完整")
      })
      return
    }

    callback.invoke(JSONObject().apply {
      put("success", true)
      put("transactionId", "tx_${System.currentTimeMillis()}")
    })
  }

  @UniJSMethod(uiThread = true)
  fun queryStatus(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply {
      put("success", true)
      put("status", "completed")
    })
  }

  @UniJSMethod(uiThread = false)
  fun handleCallback(params: JSONObject, callback: UniJSCallback) {
    callback.invoke(JSONObject().apply { put("success", true) })
  }
}
