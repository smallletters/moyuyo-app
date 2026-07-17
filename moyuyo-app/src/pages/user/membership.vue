<template>
  <view class="membership-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
      <view class="nav-btn" @click="goBack">
        <text class="nav-back">‹</text>
      </view>
      <text class="nav-title">MOYUYO+</text>
      <view class="nav-btn" />
    </view>

    <!-- Hero 会员卡区域 -->
    <view class="hero-section">
      <view class="hero-bg">
        <view class="hero-content">
          <view class="avatar-wrap">
            <text class="avatar-emoji">🐱</text>
          </view>
          <text class="user-name">宠物达人小雅</text>
          <view class="member-badge">
            <text class="member-badge-text">金卡会员</text>
          </view>

          <!-- 成长进度 -->
          <view class="progress-section">
            <view class="progress-header">
              <text class="progress-hint">距铂金会员还需 2,200 成长值</text>
              <text class="progress-percent">56%</text>
            </view>
            <view class="progress-track">
              <view class="progress-fill" :style="{ width: '56%' }" />
            </view>
          </view>

          <!-- 升级 CTA -->
          <view class="upgrade-btn" @click="onUpgrade">
            <text class="upgrade-text">立即升级 ¥99/年</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 会员等级权益对比 -->
    <view class="compare-section">
      <text class="section-title">会员等级权益对比</text>
      <scroll-view scroll-x class="compare-scroll">
        <view class="compare-table">
          <!-- 表头 -->
          <view class="compare-header">
            <view class="compare-th first">
              <text class="th-text">权益项目</text>
            </view>
            <view class="compare-th current">
              <text class="th-text current-text">金卡</text>
              <text class="th-sub">当前</text>
            </view>
            <view class="compare-th">
              <text class="th-text">铂金</text>
            </view>
            <view class="compare-th">
              <text class="th-text">黑钻</text>
            </view>
          </view>
          <!-- 数据行 -->
          <view v-for="row in compareData" :key="row.label" class="compare-row">
            <view class="compare-cell first">
              <text class="cell-label">{{ row.label }}</text>
            </view>
            <view class="compare-cell current">
              <text class="cell-value">{{ row.gold }}</text>
            </view>
            <view class="compare-cell">
              <text class="cell-value">{{ row.platinum }}</text>
            </view>
            <view class="compare-cell">
              <text class="cell-value bold">{{ row.diamond }}</text>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 会员专属特权 -->
    <view class="privilege-section">
      <text class="section-title">会员专属特权</text>
      <scroll-view scroll-x class="privilege-scroll">
        <view class="privilege-row">
          <view v-for="item in privileges" :key="item.title" class="privilege-card">
            <view class="privilege-icon-wrap">
              <text class="privilege-emoji">{{ item.icon }}</text>
            </view>
            <text class="privilege-title">{{ item.title }}</text>
            <text class="privilege-desc">{{ item.desc }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 底部链接 -->
    <view class="bottom-links">
      <view class="link-item" @click="goRules">
        <text class="link-text">了解会员规则</text>
        <text class="link-arrow">›</text>
      </view>
      <view class="link-divider" />
      <view class="link-item" @click="goPoints">
        <text class="link-text">查看积分明细</text>
        <text class="link-arrow">›</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

// 权益对比数据
const compareData = ref([
  { label: '积分加速', gold: '1.2x', platinum: '1.5x', diamond: '2x' },
  { label: '专属折扣', gold: '95折', platinum: '9折', diamond: '85折' },
  { label: '免费退换货', gold: '2次/年', platinum: '4次/年', diamond: '无限' },
  { label: '生日礼品', gold: '有', platinum: '有+优先', diamond: '有+定制' },
  { label: '专属客服', gold: '在线', platinum: '专属', diamond: '1对1' },
  { label: '每年配送券', gold: '4张', platinum: '8张', diamond: '12张' },
])

// 专属特权
const privileges = ref([
  { icon: '🏷️', title: '新品优先购', desc: '新品提前48小时购买权' },
  { icon: '⭐', title: '会员日特惠', desc: '每月8号会员专属折扣' },
  { icon: '❤️', title: '免费宠物体检', desc: '年度一次基础体检' },
  { icon: '📦', title: '专属IP周边', desc: 'MOYUYO IP限定周边' },
])

// 返回
const goBack = () => {
  uni.navigateBack()
}

// 升级
const onUpgrade = () => {
  uni.navigateTo({ url: '/pages/user/prime-page' })
}

// 会员规则
const goRules = () => {
  uni.showToast({ title: '会员规则页面开发中', icon: 'none' })
}

// 积分明细
const goPoints = () => {
  uni.navigateTo({ url: '/pages/user/points-shop' })
}
</script>

<style lang="scss" scoped>
.membership-page {
  min-height: 100vh;
  background: var(--color-background);
}

// 导航栏
.nav-bar {
  display: flex;
  align-items: center;
  height: 88rpx;
  padding: 0 32rpx;
  background: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
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
  color: var(--color-text);
}
.nav-title {
  flex: 1;
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
}

// Hero 区域
.hero-section {
  position: relative;
  min-height: 480rpx;
  overflow: hidden;
}
.hero-bg {
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.25), rgba(0, 0, 0, 0.55));
  padding: 48rpx 32rpx;
}
.hero-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 2;
}
.avatar-wrap {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4rpx solid rgba(255, 255, 255, 0.6);
  margin-bottom: 16rpx;
}
.avatar-emoji {
  font-size: 56rpx;
}
.user-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #fff;
}
.member-badge {
  margin-top: 12rpx;
  padding: 6rpx 24rpx;
  border-radius: 999rpx;
  background: rgba(0, 122, 255, 0.2);
}
.member-badge-text {
  font-size: 24rpx;
  font-weight: 600;
  color: #fff;
}

// 成长进度
.progress-section {
  margin-top: 32rpx;
  width: 100%;
  max-width: 520rpx;
}
.progress-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}
.progress-hint {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.75);
}
.progress-percent {
  font-size: 24rpx;
  font-weight: 600;
  color: #fff;
}
.progress-track {
  height: 12rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.2);
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  border-radius: 999rpx;
  background: linear-gradient(90deg, #2e8dff, #007aff);
  transition: width 0.6s ease;
}

// 升级按钮
.upgrade-btn {
  margin-top: 32rpx;
  height: 88rpx;
  padding: 0 56rpx;
  border-radius: 999rpx;
  background: linear-gradient(135deg, #007aff, #f5a623);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 32rpx rgba(0, 122, 255, 0.3);
}
.upgrade-text {
  font-size: 28rpx;
  font-weight: 600;
  color: #fff;
}

// 区域标题
.section-title {
  font-size: 34rpx;
  font-weight: 600;
  color: var(--color-text);
  padding: 36rpx 32rpx 24rpx;
}

// 权益对比
.compare-section {
  padding-bottom: 16rpx;
}
.compare-scroll {
  white-space: nowrap;
}
.compare-table {
  display: inline-flex;
  flex-direction: column;
  min-width: 600rpx;
  padding: 0 32rpx;
}
.compare-header {
  display: flex;
  margin-bottom: 8rpx;
}
.compare-th {
  flex: 1;
  padding: 16rpx;
  text-align: center;
}
.compare-th.first {
  flex: 1.2;
  text-align: left;
}
.compare-th.current {
  background: #cfe5ff;
  border-radius: 24rpx 24rpx 0 0;
}
.th-text {
  font-size: 24rpx;
  font-weight: 600;
  color: var(--color-text);
}
.th-text.current-text {
  color: #004fad;
}
.th-sub {
  font-size: 20rpx;
  color: var(--color-text-secondary);
  display: block;
  margin-top: 4rpx;
}
.compare-row {
  display: flex;
  border-bottom: 1rpx solid var(--color-border);
}
.compare-cell {
  flex: 1;
  padding: 20rpx 16rpx;
  text-align: center;
}
.compare-cell.first {
  flex: 1.2;
  text-align: left;
}
.compare-cell.current {
  background: #e8f2ff;
}
.cell-label {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.cell-value {
  font-size: 24rpx;
  color: var(--color-text);
}
.cell-value.bold {
  font-weight: 600;
}

// 专属特权
.privilege-scroll {
  white-space: nowrap;
}
.privilege-row {
  display: inline-flex;
  gap: 20rpx;
  padding: 0 32rpx 24rpx;
}
.privilege-card {
  width: 260rpx;
  padding: 28rpx;
  border: 1rpx solid var(--color-border);
  border-radius: 24rpx;
  background: var(--color-card);
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-height: 280rpx;
  white-space: normal;
}
.privilege-icon-wrap {
  width: 64rpx;
  height: 64rpx;
  border-radius: 20rpx;
  background: var(--color-background);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}
.privilege-emoji {
  font-size: 36rpx;
}
.privilege-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 8rpx;
}
.privilege-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  line-height: 1.5;
}

// 底部链接
.bottom-links {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 40rpx;
  padding: 32rpx;
  border-top: 1rpx solid var(--color-border);
  margin-top: 20rpx;
}
.link-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}
.link-text {
  font-size: 24rpx;
  font-weight: 500;
  color: var(--color-primary);
}
.link-arrow {
  font-size: 26rpx;
  color: var(--color-primary);
}
.link-divider {
  width: 2rpx;
  height: 24rpx;
  background: var(--color-border);
}
</style>
