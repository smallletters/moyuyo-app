package com.moyuyo.plugin.payment;

import com.alibaba.fastjson.JSONObject;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

/**
 * 支付原生插件（Android）
 *
 * 功能：Apple Pay / Google Pay 支付
 *
 * 部署同 PetHub 插件。
 */
public class PaymentModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void pay(JSONObject params, UniJSCallback callback) {
        String channel = params.getString("channel");
        // TODO: 调用渠道 SDK 发起支付
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("data", new JSONObject());
        callback.invoke(result);
    }

    @UniJSMethod(uiThread = true)
    public void isAvailable(JSONObject params, UniJSCallback callback) {
        String channel = params.getString("channel");
        // TODO: 检查支付渠道是否可用
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("data", true);
        callback.invoke(result);
    }
}
