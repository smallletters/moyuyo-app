package com.moyuyo.plugin.pethub;

import com.alibaba.fastjson.JSONObject;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

/**
 * PetHub 3D 原生插件（Android）
 *
 * 功能：3D宠物模型加载、场景切换、动画播放
 * 渲染引擎：Filament
 *
 * 部署：
 *   1. 编译此模块为 AAR
 *   2. 放置到 nativeplugins/MOYUYOPetHub/android/ 目录下
 *   3. manifest.json -> App原生插件配置 中添加 "MOYUYOPetHub"
 */
public class PetHubModule extends UniModule {

    @UniJSMethod(uiThread = true)
    public void loadPet(JSONObject params, UniJSCallback callback) {
        String petId = params.getString("petId");
        // TODO: Filament 引擎加载 3D 模型
        JSONObject result = new JSONObject();
        result.put("success", true);
        callback.invoke(result);
    }

    @UniJSMethod(uiThread = true)
    public void switchScene(JSONObject params, UniJSCallback callback) {
        String scene = params.getString("scene");
        // TODO: 切换 IP 场景贴图
        JSONObject result = new JSONObject();
        result.put("success", true);
        callback.invoke(result);
    }

    @UniJSMethod(uiThread = true)
    public void playAnimation(JSONObject params, UniJSCallback callback) {
        String animName = params.getString("animName");
        // TODO: 播放骨骼动画
        JSONObject result = new JSONObject();
        result.put("success", true);
        callback.invoke(result);
    }

    @UniJSMethod(uiThread = true)
    public void onInteraction(JSONObject params, UniJSCallback callback) {
        String type = params.getString("type");
        // TODO: 点击/抚摸反馈
        JSONObject result = new JSONObject();
        result.put("success", true);
        callback.invoke(result);
    }

    @UniJSMethod(uiThread = true)
    public void dispose(JSONObject params, UniJSCallback callback) {
        // TODO: 释放 GPU 资源
        JSONObject result = new JSONObject();
        result.put("success", true);
        callback.invoke(result);
    }
}
