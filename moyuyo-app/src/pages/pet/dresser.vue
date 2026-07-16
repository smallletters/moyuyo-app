<template>
  <view class="dresser">
    <!-- 顶部导航栏 -->
    <view class="nav-header">
      <view class="nav-btn" @tap="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="nav-title">宠物装扮</text>
      <view class="nav-btn" @tap="toggleHistory">
        <text class="history-icon">⏱</text>
      </view>
    </view>

    <scroll-view scroll-y class="scroll">
      <!-- 宠物预览区 -->
      <view class="preview-section">
        <view class="preview-container">
          <view class="preview-glow" />
          <view class="preview-circle">
            <view class="pet-body">
              <view class="pet-ear-left" />
              <view class="pet-ear-right" />
              <view class="pet-eye-left" />
              <view class="pet-eye-right" />
              <view class="pet-nose" />
            </view>
            <view class="current-outfit-badge">
              <text>{{ currentOutfit ? currentOutfit.name : '当前穿戴: 无' }}</text>
            </view>
          </view>
          <view class="rotate-btn" @tap="handleRotate">
            <text>⟳</text>
          </view>
        </view>
        <text class="preview-hint">点击下方配饰即可实时预览装扮效果</text>
      </view>

      <!-- 已保存套装 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">我的套装</text>
          <text class="section-count">{{ savedOutfits.length }}/3 套</text>
        </view>
        <scroll-view scroll-x class="outfit-scroll" show-scrollbar="false">
          <view
            v-for="outfit in savedOutfits"
            :key="outfit.id"
            class="outfit-circle"
            :class="{ active: activeOutfit === outfit.id }"
            :style="{
              boxShadow:
                activeOutfit === outfit.id ? '0 0 0 5rpx var(--primary)' : 'var(--shadow-xs)',
              background: outfit.bg,
            }"
            @tap="activeOutfit = outfit.id"
          >
            <view class="outfit-inner">
              <text class="outfit-icon">{{ outfit.icon }}</text>
            </view>
            <text
              class="outfit-label"
              :style="{ color: activeOutfit === outfit.id ? 'var(--primary)' : 'var(--text-400)' }"
            >
              {{ outfit.label }}
            </text>
          </view>
        </scroll-view>
      </view>

      <!-- IP 专属装扮区 -->
      <view class="section">
        <view class="section-header">
          <text class="ip-icon">✦</text>
          <text class="section-title">IP 联名专属</text>
        </view>
        <scroll-view scroll-x class="ip-scroll" show-scrollbar="false">
          <view
            v-for="item in ipItems"
            :key="item.id"
            class="ip-card"
            :style="{ background: item.bg }"
            @tap="handleTryOn(item)"
          >
            <view class="ip-icon-wrapper">
              <text class="ip-item-icon">{{ item.icon }}</text>
            </view>
            <text class="ip-item-name">{{ item.name }}</text>
            <text class="ip-item-badge">{{ item.badge }}</text>
          </view>
        </scroll-view>
      </view>

      <!-- 装扮分类 Tab -->
      <view class="category-tabs">
        <view
          v-for="cat in categories"
          :key="cat.key"
          class="cat-tab"
          :class="{ active: activeCategory === cat.key }"
          :style="{
            color: activeCategory === cat.key ? 'var(--primary)' : 'var(--text-400)',
            borderBottom:
              activeCategory === cat.key ? '4rpx solid var(--primary)' : '4rpx solid transparent',
          }"
          @tap="activeCategory = cat.key"
        >
          <text>{{ cat.label }}</text>
        </view>
      </view>

      <!-- 装扮网格 -->
      <view class="outfit-grid">
        <view
          v-for="item in filteredOutfits"
          :key="item.id"
          class="outfit-item"
          :class="{
            owned: item.owned,
            wearing: currentOutfit && currentOutfit.id === item.id,
            locked: !item.owned,
          }"
          :style="{
            borderColor:
              currentOutfit && currentOutfit.id === item.id ? 'var(--primary)' : 'var(--border)',
            borderWidth: currentOutfit && currentOutfit.id === item.id ? '4rpx' : '2rpx',
          }"
          @tap="handleOutfitTap(item)"
        >
          <view class="outfit-thumb" :style="{ background: item.color }">
            <view v-if="!item.owned" class="lock-overlay">
              <text class="lock-icon">🔒</text>
            </view>
            <view v-if="currentOutfit && currentOutfit.id === item.id" class="wearing-badge">
              <text>穿戴中</text>
            </view>
            <view class="outfit-thumb-icon">
              <text>{{ item.icon }}</text>
            </view>
          </view>
          <text
            class="outfit-item-name"
            :style="{ color: item.owned ? 'var(--foreground)' : 'var(--text-400)' }"
          >
            {{ item.name }}
          </text>
          <text
            v-if="!item.owned"
            class="unlock-hint"
            :style="{
              color: item.unlockType === 'points' ? 'var(--brand-600)' : 'var(--brand-600)',
            }"
          >
            {{ item.unlockHint }}
          </text>
        </view>
      </view>
    </scroll-view>

    <!-- 底部固定操作栏 -->
    <view class="bottom-bar">
      <view class="points-display">
        <text class="points-icon">✦</text>
        <text class="points-value">{{ points }}</text>
      </view>
      <view class="save-btn" @tap="handleSaveOutfit">
        <text>✓ 保存装扮</text>
      </view>
    </view>
  </view>
</template>

<script>
import { petDresserApi } from '@/api'

export default {
  data() {
    return {
      petId: null,
      activeOutfit: null,
      activeCategory: 'all',
      currentOutfit: null,
      points: 0,
      categories: [
        { key: 'all', label: '全部' },
        { key: 'collar', label: '项圈' },
        { key: 'bow', label: '蝴蝶结' },
        { key: 'hat', label: '帽子' },
        { key: 'glasses', label: '眼镜' },
        { key: 'clothes', label: '衣服' },
      ],
      savedOutfits: [],
      ipItems: [],
      outfits: [],
    }
  },
  computed: {
    filteredOutfits() {
      if (this.activeCategory === 'all') return this.outfits
      return this.outfits.filter((o) => o.category === this.activeCategory)
    },
  },
  onLoad(query) {
    this.petId = query.petId || null
    this.loadData()
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    async loadData() {
      if (!this.petId) return
      try {
        const data = await petDresserApi.getPetOutfits(this.petId)
        this.outfits = data.items || data.outfits || data || []
        this.savedOutfits = data.savedOutfits || []
        this.ipItems = data.ipItems || []
        this.points = data.points || 0
        const equipped = this.outfits.find((o) => o.equipped)
        if (equipped) {
          this.currentOutfit = { id: equipped.id, name: equipped.name }
          this.activeOutfit = equipped.id
        }
      } catch (e) {
        console.warn('[dresser] load failed', e)
        this.outfits = []
      }
    },
    toggleHistory() {
      uni.showToast({ title: '装扮记录', icon: 'none' })
    },
    handleRotate() {
      uni.showToast({ title: '旋转宠物', icon: 'none' })
    },
    handleTryOn(item) {
      this.currentOutfit = { id: item.id, name: item.name }
      uni.showToast({ title: `试穿「${item.name}」`, icon: 'success' })
    },
    handleOutfitTap(item) {
      if (!item.owned) {
        uni.showToast({ title: `未解锁：${item.unlockHint}`, icon: 'none' })
        return
      }
      this.currentOutfit = { id: item.id, name: item.name }
    },
    async handleSaveOutfit() {
      if (!this.petId || !this.currentOutfit) {
        uni.showToast({ title: '请先选择装扮', icon: 'none' })
        return
      }
      try {
        await petDresserApi.equipOutfit(this.petId, this.currentOutfit.id)
        uni.showToast({ title: '装扮已保存', icon: 'success' })
        this.loadData()
      } catch (e) {
        console.warn('[dresser] save failed', e)
        uni.showToast({ title: '保存失败', icon: 'none' })
      }
    },
  },
}
</script>

<style scoped lang="scss">
.dresser {
  min-height: 100vh;
  background: var(--background);
}
.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 30rpx;
  background: var(--background-800);
  position: sticky;
  top: 0;
  z-index: 30;
}
.nav-btn {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.12);
  display: flex;
  align-items: center;
  justify-content: center;
}
.back-icon {
  color: var(--text-50);
  font-size: 40rpx;
}
.history-icon {
  color: var(--text-50);
  font-size: 32rpx;
}
.nav-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--text-50);
}
.preview-section {
  background: var(--background-200);
  padding: 40rpx 30rpx 30rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.preview-container {
  position: relative;
  display: flex;
  justify-content: center;
}
.preview-glow {
  position: absolute;
  inset: -24rpx;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(0, 122, 255, 0.12) 0%, transparent 70%);
}
.preview-circle {
  width: 384rpx;
  height: 384rpx;
  border-radius: 50%;
  background: linear-gradient(145deg, #cfe5ff, #9fcbff, #66abff);
  position: relative;
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}
.pet-body {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.pet-ear-left {
  position: absolute;
  top: 48rpx;
  left: 100rpx;
  width: 48rpx;
  height: 64rpx;
  border-radius: 50% 50% 30% 30%;
  background: #e8a882;
  transform: rotate(-12deg);
}
.pet-ear-right {
  position: absolute;
  top: 48rpx;
  right: 100rpx;
  width: 48rpx;
  height: 64rpx;
  border-radius: 50% 50% 30% 30%;
  background: #e8a882;
  transform: rotate(12deg);
}
.pet-eye-left,
.pet-eye-right {
  position: absolute;
  top: 160rpx;
  width: 24rpx;
  height: 28rpx;
  border-radius: 50%;
  background: var(--background-800);
}
.pet-eye-left {
  left: 136rpx;
}
.pet-eye-right {
  right: 136rpx;
}
.pet-nose {
  position: absolute;
  top: 216rpx;
  left: 50%;
  transform: translateX(-50%);
  width: 20rpx;
  height: 16rpx;
  border-radius: 50%;
  background: #c47a5a;
}
.current-outfit-badge {
  position: absolute;
  bottom: 30rpx;
  left: 50%;
  transform: translateX(-50%);
  padding: 8rpx 24rpx;
  border-radius: 999rpx;
  font-size: 20rpx;
  font-weight: 600;
  color: var(--text-50);
  background: rgba(0, 0, 0, 0.55);
  backdrop-filter: blur(8px);
  white-space: nowrap;
}
.rotate-btn {
  position: absolute;
  bottom: -8rpx;
  right: 0;
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: var(--card);
  box-shadow: var(--shadow-sm);
  border: 2rpx solid var(--border);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  color: var(--icon-600);
}
.preview-hint {
  font-size: 24rpx;
  color: var(--text-400);
  margin-top: 30rpx;
}
.section {
  padding: 30rpx;
}
.section-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 24rpx;
}
.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--foreground);
}
.section-count {
  font-size: 24rpx;
  color: var(--text-400);
  margin-left: auto;
}
.ip-icon {
  font-size: 32rpx;
  color: var(--brand-300);
}
.outfit-scroll {
  display: flex;
  flex-direction: row;
  white-space: nowrap;
}
.outfit-circle {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
  margin-right: 32rpx;
  flex-shrink: 0;
}
.outfit-inner {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  background: var(--background-200);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 4rpx;
}
.outfit-icon {
  font-size: 40rpx;
}
.outfit-label {
  font-size: 20rpx;
  font-weight: 500;
}
.ip-scroll {
  display: flex;
  flex-direction: row;
  white-space: nowrap;
}
.ip-card {
  width: 280rpx;
  border-radius: 24rpx;
  padding: 24rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}
.ip-icon-wrapper {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}
.ip-item-icon {
  font-size: 40rpx;
  color: #ffffff;
}
.ip-item-name {
  font-size: 24rpx;
  font-weight: 700;
  color: #ffffff;
  display: block;
}
.ip-item-badge {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.7);
  display: block;
  margin-top: 4rpx;
}
.category-tabs {
  display: flex;
  border-bottom: 2rpx solid var(--border);
  padding: 0 30rpx;
  overflow-x: auto;
}
.cat-tab {
  padding: 20rpx 30rpx;
  font-size: 28rpx;
  white-space: nowrap;
  flex-shrink: 0;
}
.outfit-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  padding: 24rpx 30rpx 200rpx;
}
.outfit-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
  padding: 20rpx;
  border-radius: 24rpx;
  background: var(--card);
  border-style: solid;
}
.outfit-thumb {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 20rpx;
  position: relative;
  overflow: hidden;
}
.outfit-thumb-icon {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 56rpx;
  color: rgba(255, 255, 255, 0.9);
}
.wearing-badge {
  position: absolute;
  top: 12rpx;
  right: 12rpx;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  font-size: 18rpx;
  font-weight: 700;
  background: var(--primary);
  color: var(--primary-foreground);
}
.lock-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
}
.lock-icon {
  font-size: 48rpx;
}
.outfit-item-name {
  font-size: 22rpx;
  font-weight: 500;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}
.unlock-hint {
  font-size: 18rpx;
  font-weight: 600;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  background: var(--background-200);
}
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px);
  border-top: 2rpx solid var(--border);
  padding: 24rpx 30rpx;
  padding-bottom: calc(env(safe-area-inset-bottom, 0px) + 24rpx);
  display: flex;
  align-items: center;
  gap: 24rpx;
  z-index: 40;
}
.points-display {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 16rpx 24rpx;
  border-radius: 20rpx;
  background: var(--background-200);
}
.points-icon {
  font-size: 32rpx;
  color: var(--brand-300);
}
.points-value {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--text-600);
}
.save-btn {
  flex: 1;
  height: 96rpx;
  border-radius: 24rpx;
  background: var(--primary);
  color: var(--primary-foreground);
  font-size: 28rpx;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
}
</style>
