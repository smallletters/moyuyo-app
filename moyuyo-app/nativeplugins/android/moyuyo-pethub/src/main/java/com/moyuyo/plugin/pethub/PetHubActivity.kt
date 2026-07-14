package com.moyuyo.plugin.pethub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 3D Pet Hub 场景 Activity
 * 基于 Filament 引擎的全屏 3D 渲染页面
 */
class PetHubActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pethub)
  }
}
