<template>
  <view class="coupon-transfer-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
      <view class="nav-btn" @click="goBack">
        <text class="nav-back">‹</text>
      </view>
      <text class="nav-title">转赠优惠券</text>
      <view class="nav-btn" />
    </view>

    <!-- 转赠的优惠券卡片 -->
    <view class="coupon-card">
      <view class="coupon-left">
        <text class="coupon-brand">MOYUYO</text>
        <text class="coupon-amount">$10</text>
        <text class="coupon-off">OFF</text>
      </view>
      <view class="coupon-divider-area">
        <view class="divider-dot top" />
        <view class="divider-line" />
        <view class="divider-dot bottom" />
      </view>
      <view class="coupon-right">
        <view class="coupon-info">
          <text class="coupon-name">CARE 洗护专场满减券</text>
          <text class="coupon-condition">满 $50 可用</text>
        </view>
        <view class="coupon-validity">
          <text class="validity-icon">⏰</text>
          <text class="validity-text">有效期至 2026-07-15</text>
        </view>
      </view>
    </view>

    <!-- 转赠方式标题 -->
    <text class="section-label">选择转赠方式</text>

    <!-- 方式一：App 内好友 -->
    <view
      class="transfer-option"
      :class="{ active: transferMethod === 'friend' }"
      @click="transferMethod = 'friend'"
    >
      <view class="option-header">
        <view class="option-icon-wrap">
          <text class="option-emoji">👥</text>
        </view>
        <view class="option-info">
          <text class="option-title">App 内好友</text>
          <text class="option-desc">选择好友直接转赠</text>
        </view>
        <view class="radio-outer">
          <view v-if="transferMethod === 'friend'" class="radio-inner" />
        </view>
      </view>
      <!-- 搜索好友 -->
      <view v-if="transferMethod === 'friend'" class="search-field">
        <text class="search-icon">🔍</text>
        <input class="search-input" type="text" placeholder="搜索好友昵称">
      </view>
      <!-- 好友列表 -->
      <scroll-view v-if="transferMethod === 'friend'" scroll-x class="friend-list">
        <view class="friend-row">
          <view
            v-for="friend in friends"
            :key="friend.name"
            class="friend-item"
            @click="selectFriend(friend)"
          >
            <view class="friend-avatar" :style="{ background: friend.avatarBg }" />
            <text class="friend-name">{{ friend.name }}</text>
          </view>
          <view class="friend-item" @click="showMoreFriends">
            <view class="friend-avatar add">
              <text class="add-icon">＋</text>
            </view>
            <text class="friend-name more">更多</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 方式二：分享链接 -->
    <view
      class="transfer-option"
      :class="{ active: transferMethod === 'link' }"
      @click="transferMethod = 'link'"
    >
      <view class="option-header">
        <view class="option-icon-wrap">
          <text class="option-emoji">🔗</text>
        </view>
        <view class="option-info">
          <text class="option-title">分享链接</text>
          <text class="option-desc">生成链接，发送给好友领取</text>
        </view>
        <view class="radio-outer">
          <view v-if="transferMethod === 'link'" class="radio-inner" />
        </view>
      </view>
      <!-- 链接展示 -->
      <view v-if="transferMethod === 'link'" class="link-field">
        <text class="link-copy-icon">📋</text>
        <text class="link-url">https://moyuyo.com/c/clm/X9k2F</text>
        <view class="link-copy-btn" @click="copyLink">
          <text class="link-copy-text">复制</text>
        </view>
      </view>
    </view>

    <!-- 方式三：二维码 -->
    <view
      class="transfer-option"
      :class="{ active: transferMethod === 'qrcode' }"
      @click="transferMethod = 'qrcode'"
    >
      <view class="option-header">
        <view class="option-icon-wrap">
          <text class="option-emoji">📱</text>
        </view>
        <view class="option-info">
          <text class="option-title">二维码</text>
          <text class="option-desc">好友扫码即可领取优惠券</text>
        </view>
        <view class="radio-outer">
          <view v-if="transferMethod === 'qrcode'" class="radio-inner" />
        </view>
      </view>
      <!-- 二维码占位 -->
      <view v-if="transferMethod === 'qrcode'" class="qrcode-area">
        <view class="qrcode-placeholder">
          <text
            v-for="i in 49"
            :key="i"
            class="qrcode-grid"
            :style="{ opacity: ((i * 7) % 3) * 0.3 + 0.2 }"
          />
        </view>
        <text class="qrcode-hint">MOYUYO 优惠券领取码</text>
      </view>
    </view>

    <!-- 底部提示 -->
    <view class="transfer-hint">
      <text class="hint-icon">ℹ️</text>
      <text class="hint-text">受赠人需在 24 小时内领取，逾期自动退回至您的账户</text>
    </view>

    <!-- 底部操作按钮 -->
    <view class="bottom-bar">
      <view class="confirm-btn" @click="confirmTransfer">
        <text class="confirm-text">确认转赠</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

// 当前选中的转赠方式
const transferMethod = ref('friend')

// 好友列表
const friends = ref([
  { name: '小糯米', avatarBg: 'linear-gradient(135deg, #ff9a9e, #fecfef)' },
  { name: '柴柴爸', avatarBg: 'linear-gradient(135deg, #a18cd1, #fbc2eb)' },
  { name: '猫咪酱', avatarBg: 'linear-gradient(135deg, #84fab0, #8fd3f4)' },
])

// 返回
const goBack = () => {
  uni.navigateBack()
}

// 选择好友
const selectFriend = (friend) => {
  uni.showToast({ title: `已选择 ${friend.name}`, icon: 'none' })
}

// 更多好友
const showMoreFriends = () => {
  uni.showToast({ title: '好友列表开发中', icon: 'none' })
}

// 复制链接
const copyLink = () => {
  uni.setClipboardData({
    data: 'https://moyuyo.com/c/clm/X9k2F',
    success: () => {
      uni.showToast({ title: '链接已复制', icon: 'success' })
    },
  })
}

// 确认转赠
const confirmTransfer = () => {
  uni.showModal({
    title: '确认转赠',
    content: '确认将此优惠券转赠给好友？',
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '转赠成功', icon: 'success' })
      }
    },
  })
}
</script>

<style lang="scss" scoped>
.coupon-transfer-page {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 160rpx;
}

// 导航栏
.nav-bar {
  display: flex;
  align-items: center;
  height: 96rpx;
  padding: 0 32rpx;
  background: var(--color-text);
}
.nav-btn {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.nav-back {
  font-size: 40rpx;
  color: #fff;
}
.nav-title {
  flex: 1;
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

// 优惠券卡片
.coupon-card {
  margin: 32rpx 24rpx;
  display: flex;
  border-radius: 28rpx;
  overflow: hidden;
  background: var(--color-card);
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}
.coupon-left {
  min-width: 180rpx;
  padding: 36rpx 28rpx;
  background: linear-gradient(135deg, var(--color-primary), #66abff);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.coupon-brand {
  font-size: 22rpx;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.85);
  letter-spacing: 4rpx;
}
.coupon-amount {
  font-size: 52rpx;
  font-weight: 700;
  color: #fff;
  margin-top: 8rpx;
}
.coupon-off {
  font-size: 28rpx;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.9);
}
.coupon-divider-area {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 0 16rpx;
}
.divider-line {
  width: 2rpx;
  flex: 1;
  border-left: 2rpx dashed var(--color-border);
}
.divider-dot {
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  background: var(--color-background);
  position: absolute;
}
.divider-dot.top {
  top: -12rpx;
}
.divider-dot.bottom {
  bottom: -12rpx;
}
.coupon-right {
  flex: 1;
  padding: 36rpx 28rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.coupon-name {
  font-size: 30rpx;
  font-weight: 600;
  color: var(--color-text);
}
.coupon-condition {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 12rpx;
}
.coupon-validity {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 20rpx;
}
.validity-icon {
  font-size: 26rpx;
}
.validity-text {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

// 区域标签
.section-label {
  font-size: 26rpx;
  font-weight: 600;
  color: var(--color-text);
  padding: 0 28rpx;
  margin-bottom: 16rpx;
}

// 转赠方式选项
.transfer-option {
  margin: 0 24rpx 16rpx;
  padding: 24rpx;
  border-radius: 28rpx;
  background: var(--color-card);
  border: 3rpx solid var(--color-border);
  transition: border-color 0.18s ease;
}
.transfer-option.active {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2rpx var(--color-primary);
}
.option-header {
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.option-icon-wrap {
  width: 64rpx;
  height: 64rpx;
  border-radius: 20rpx;
  background: #e8f2ff;
  display: flex;
  align-items: center;
  justify-content: center;
}
.option-emoji {
  font-size: 32rpx;
}
.option-info {
  flex: 1;
}
.option-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}
.option-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}
.radio-outer {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  border: 4rpx solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color 0.18s ease;
}
.transfer-option.active .radio-outer {
  border-color: var(--color-primary);
}
.radio-inner {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background: var(--color-primary);
}

// 搜索好友
.search-field {
  display: flex;
  align-items: center;
  gap: 12rpx;
  height: 72rpx;
  padding: 0 24rpx;
  border-radius: 20rpx;
  background: var(--color-background);
  margin-top: 20rpx;
  margin-bottom: 20rpx;
}
.search-icon {
  font-size: 28rpx;
}
.search-input {
  flex: 1;
  font-size: 28rpx;
  color: var(--color-text);
  border: none;
  background: transparent;
}

// 好友列表
.friend-list {
  white-space: nowrap;
}
.friend-row {
  display: inline-flex;
  gap: 24rpx;
  padding: 8rpx 0;
}
.friend-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}
.friend-avatar {
  width: 88rpx;
  height: 88rpx;
  border-radius: 50%;
}
.friend-avatar.add {
  border: 3rpx dashed var(--color-border);
  background: var(--color-card);
  display: flex;
  align-items: center;
  justify-content: center;
}
.add-icon {
  font-size: 36rpx;
  color: var(--color-text-secondary);
}
.friend-name {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  max-width: 100rpx;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.friend-name.more {
  color: var(--color-text-secondary);
}

// 链接展示
.link-field {
  display: flex;
  align-items: center;
  gap: 12rpx;
  height: 72rpx;
  padding: 0 24rpx;
  border-radius: 20rpx;
  background: var(--color-background);
  border: 1rpx solid var(--color-border);
  margin-top: 20rpx;
}
.link-copy-icon {
  font-size: 28rpx;
}
.link-url {
  flex: 1;
  font-size: 24rpx;
  color: var(--color-text-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.link-copy-btn {
  padding: 8rpx 20rpx;
  border-radius: 12rpx;
  background: #e8f2ff;
}
.link-copy-text {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--color-primary);
}

// 二维码区域
.qrcode-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 0;
}
.qrcode-placeholder {
  width: 200rpx;
  height: 200rpx;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-template-rows: repeat(7, 1fr);
  gap: 4rpx;
  padding: 16rpx;
  border-radius: 16rpx;
  background: var(--color-card);
}
.qrcode-grid {
  border-radius: 2rpx;
  background: var(--color-text);
}
.qrcode-hint {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 20rpx;
}

// 底部提示
.transfer-hint {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
  padding: 0 28rpx;
  margin-bottom: 32rpx;
}
.hint-icon {
  font-size: 28rpx;
  margin-top: 2rpx;
}
.hint-text {
  font-size: 24rpx;
  line-height: 1.5;
  color: var(--color-text-secondary);
}

// 底部操作
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 32rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: linear-gradient(to top, var(--color-background) 60%, transparent);
}
.confirm-btn {
  height: 104rpx;
  border-radius: 28rpx;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}
.confirm-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}
</style>
