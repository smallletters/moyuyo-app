<template>
  <view class="prime-page">
    <!-- 顶部品牌区域 -->
    <view class="prime-header">
      <view class="header-nav">
        <view class="nav-btn" @click="goBack">
          <text class="nav-back">‹</text>
        </view>
        <text class="nav-label">MOYUYO Prime</text>
      </view>
      <view class="header-content">
        <view class="prime-logo">
          <text class="logo-star">⭐</text>
        </view>
        <view class="header-text">
          <text class="header-title">MOYUYO Prime</text>
          <text class="header-subtitle">专属于你的高端宠物生活</text>
        </view>
      </view>
    </view>

    <!-- 未开通视图 -->
    <view v-if="!isActivated" class="not-activated">
      <!-- 订阅方案选择 -->
      <view class="plan-card">
        <view class="plan-row">
          <view
            class="plan-option"
            :class="{ selected: selectedPlan === 'monthly' }"
            @click="selectPlan('monthly')"
          >
            <text class="plan-name">月付</text>
            <view class="plan-price-row">
              <text class="plan-price">$9.99</text>
              <text class="plan-unit">/月</text>
            </view>
          </view>
          <view
            class="plan-option"
            :class="{ selected: selectedPlan === 'yearly' }"
            @click="selectPlan('yearly')"
          >
            <view class="recommend-tag">
              <text class="recommend-text">推荐</text>
            </view>
            <view class="plan-name-row">
              <text class="plan-name">年付</text>
              <view class="save-badge">
                <text class="save-text">省 $20.88</text>
              </view>
            </view>
            <view class="plan-price-row">
              <text class="plan-price">$99</text>
              <text class="plan-unit">/年</text>
            </view>
            <text class="plan-equivalent">约 $8.25/月</text>
          </view>
        </view>

        <!-- 立即开通按钮 -->
        <view class="activate-btn" @click="activatePrime">
          <text class="activate-text">👑 立即开通</text>
        </view>

        <!-- 免费试用提示 -->
        <view class="trial-hint">
          <text class="trial-icon">🎁</text>
          <text class="trial-text">30天免费试用，随时可取消</text>
        </view>

        <!-- 合规提示 -->
        <view class="legal-hint">
          <text class="legal-text">
            开通即表示你同意自动续费服务。试用期内免费，试用结束后将按所选方案自动扣费。你可以在「我的
            > 订阅管理」中随时取消自动续费，取消后服务将持续至当前周期结束。
          </text>
        </view>
      </view>

      <!-- 会员权益列表 -->
      <view class="benefits-card">
        <text class="benefits-title">会员专属权益</text>
        <text class="benefits-subtitle">开通 Prime 即享全部权益</text>
        <view v-for="benefit in benefits" :key="benefit.title" class="benefit-row">
          <view class="benefit-icon-wrap" :style="{ background: benefit.bg }">
            <text class="benefit-emoji">{{ benefit.icon }}</text>
          </view>
          <view class="benefit-info">
            <text class="benefit-name">{{ benefit.title }}</text>
            <text class="benefit-desc">{{ benefit.desc }}</text>
          </view>
          <text class="benefit-arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 已开通视图 -->
    <view v-else class="activated">
      <!-- 会员状态卡片 -->
      <view class="status-card">
        <view class="status-header">
          <view class="status-logo">
            <text class="logo-star-small">⭐</text>
          </view>
          <view class="status-info">
            <view class="status-title-row">
              <text class="status-title">MOYUYO Prime 会员</text>
              <view class="status-badge">
                <text class="status-badge-text">已开通</text>
              </view>
            </view>
            <text class="status-plan">年付方案 · 自动续费已开启</text>
          </view>
        </view>
        <view class="expiry-row">
          <text class="expiry-icon">📅</text>
          <text class="expiry-label">到期时间</text>
          <text class="expiry-date">2027年7月8日</text>
        </view>
      </view>

      <!-- 本月权益统计 -->
      <view class="stats-card">
        <text class="stats-title">本月已享权益</text>
        <view class="stats-row">
          <view v-for="stat in monthStats" :key="stat.label" class="stat-item">
            <view class="stat-icon-wrap" :style="{ background: stat.bg }">
              <text class="stat-emoji">{{ stat.icon }}</text>
            </view>
            <text class="stat-value">{{ stat.value }}</text>
            <text class="stat-label">{{ stat.label }}</text>
          </view>
        </view>
      </view>

      <!-- 管理订阅 -->
      <view class="manage-card">
        <view
          v-for="item in manageList"
          :key="item.label"
          class="manage-item"
          @click="onManageClick(item)"
        >
          <view class="manage-left">
            <text class="manage-icon">{{ item.icon }}</text>
            <text class="manage-label">{{ item.label }}</text>
          </view>
          <view class="manage-right">
            <text v-if="item.extra" class="manage-extra">{{ item.extra }}</text>
            <text class="manage-arrow">›</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

// 是否已开通
const isActivated = ref(false)
// 当前选中的方案
const selectedPlan = ref('yearly')

// 权益列表
const benefits = ref([
  { icon: '🚚', title: '全场免运费', desc: '无门槛，全品类包邮', bg: '#e8f2ff' },
  { icon: '🏷️', title: '专属会员价', desc: '额外 5-10% off 折扣', bg: '#e9f9ee' },
  { icon: '⚡', title: '优先发货', desc: '24小时内极速发货', bg: '#e8f2ff' },
  { icon: '🔄', title: '免费退换货', desc: '退货运费全免', bg: '#e9f9ee' },
  { icon: '🎧', title: '专属客服', desc: '1v1 优先响应', bg: '#e8f2ff' },
  { icon: '🔥', title: 'Prime Day 专属大促', desc: '会员限定大促专场', bg: '#ffecea' },
  { icon: '🪙', title: '每月赠送 $10 积分', desc: '自动到账，购物抵扣', bg: '#e8f2ff' },
  { icon: '✨', title: '新品优先购', desc: '抢先体验新品', bg: '#e9f9ee' },
  { icon: '🔓', title: 'Pet Hub 全部场景解锁', desc: '健康、社交、护理全场景', bg: '#e8f2ff' },
])

// 本月统计
const monthStats = ref([
  { icon: '🚚', value: '12', label: '免运费次数', bg: '#e8f2ff' },
  { icon: '🐷', value: '¥86', label: '累计节省', bg: '#e9f9ee' },
  { icon: '⚡', value: '8', label: '优先发货单', bg: '#e8f2ff' },
])

// 管理列表
const manageList = ref([
  { icon: '💳', label: '管理订阅', extra: '' },
  { icon: '🧾', label: '查看账单', extra: '' },
  { icon: '🎁', label: '赠送积分', extra: '本月 $10' },
])

// 返回
const goBack = () => {
  uni.navigateBack()
}

// 选择方案
const selectPlan = (plan) => {
  selectedPlan.value = plan
}

// 开通 Prime
const activatePrime = () => {
  isActivated.value = true
  uni.showToast({ title: '开通成功', icon: 'success' })
}

// 管理点击
const onManageClick = (item) => {
  uni.showToast({ title: item.label, icon: 'none' })
}
</script>

<style lang="scss" scoped>
.prime-page {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 60rpx;
}

// 顶部品牌区域
.prime-header {
  background: linear-gradient(135deg, #0064d6, #007aff, #2e8dff);
  padding: 24rpx 32rpx 64rpx;
}
.header-nav {
  display: flex;
  align-items: center;
  gap: 20rpx;
  margin-bottom: 40rpx;
}
.nav-btn {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
}
.nav-back {
  font-size: 40rpx;
  color: #fff;
}
.nav-label {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
}
.header-content {
  display: flex;
  align-items: center;
  gap: 24rpx;
}
.prime-logo {
  width: 88rpx;
  height: 88rpx;
  border-radius: 28rpx;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}
.logo-star {
  font-size: 48rpx;
}
.header-title {
  font-size: 44rpx;
  font-weight: 700;
  color: #fff;
}
.header-subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 4rpx;
}

// 未开通视图
.not-activated {
  padding: 0 24rpx;
  margin-top: -40rpx;
}

// 方案卡片
.plan-card {
  background: var(--color-card);
  border-radius: 24rpx;
  padding: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}
.plan-row {
  display: flex;
  gap: 20rpx;
  margin-bottom: 28rpx;
}
.plan-option {
  flex: 1;
  padding: 24rpx;
  border: 4rpx solid var(--color-border);
  border-radius: 24rpx;
  position: relative;
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease;
}
.plan-option.selected {
  border-color: var(--color-primary);
  background: #e8f2ff;
}
.plan-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}
.plan-name-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 12rpx;
}
.recommend-tag {
  position: absolute;
  top: -16rpx;
  right: 20rpx;
  padding: 4rpx 16rpx;
  border-radius: 999rpx;
  background: var(--color-primary);
}
.recommend-text {
  font-size: 20rpx;
  font-weight: 700;
  color: #fff;
}
.save-badge {
  padding: 4rpx 12rpx;
  border-radius: 999rpx;
  background: #e9f9ee;
}
.save-text {
  font-size: 22rpx;
  font-weight: 700;
  color: var(--color-success);
}
.plan-price-row {
  display: flex;
  align-items: baseline;
  gap: 4rpx;
  margin-top: 12rpx;
}
.plan-price {
  font-size: 44rpx;
  font-weight: 700;
  color: var(--color-text);
}
.plan-unit {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}
.plan-equivalent {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}

// 开通按钮
.activate-btn {
  height: 96rpx;
  border-radius: 24rpx;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}
.activate-text {
  font-size: 32rpx;
  font-weight: 700;
  color: #fff;
}

// 试用提示
.trial-hint {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  margin-top: 20rpx;
}
.trial-icon {
  font-size: 26rpx;
}
.trial-text {
  font-size: 24rpx;
  font-weight: 500;
  color: var(--color-primary);
}

// 合规提示
.legal-hint {
  margin-top: 24rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid var(--color-border);
}
.legal-text {
  font-size: 24rpx;
  line-height: 1.6;
  color: var(--color-text-secondary);
}

// 权益列表
.benefits-card {
  margin-top: 32rpx;
  padding: 28rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}
.benefits-title {
  font-size: 32rpx;
  font-weight: 700;
  color: var(--color-text);
}
.benefits-subtitle {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-bottom: 12rpx;
}
.benefit-row {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 24rpx 0;
  border-bottom: 1rpx solid var(--color-border);
}
.benefit-row:last-child {
  border-bottom: none;
}
.benefit-icon-wrap {
  width: 64rpx;
  height: 64rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.benefit-emoji {
  font-size: 32rpx;
}
.benefit-info {
  flex: 1;
  min-width: 0;
}
.benefit-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}
.benefit-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}
.benefit-arrow {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}

// 已开通视图
.activated {
  padding: 0 24rpx;
  margin-top: -40rpx;
}

// 会员状态卡片
.status-card {
  background: var(--color-card);
  border-radius: 24rpx;
  padding: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}
.status-header {
  display: flex;
  align-items: center;
  gap: 20rpx;
  margin-bottom: 24rpx;
}
.status-logo {
  width: 80rpx;
  height: 80rpx;
  border-radius: 24rpx;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  background: #e8f2ff;
}
.logo-star-small {
  font-size: 40rpx;
}
.status-info {
  flex: 1;
}
.status-title-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.status-title {
  font-size: 28rpx;
  font-weight: 700;
  color: var(--color-text);
}
.status-badge {
  padding: 4rpx 16rpx;
  border-radius: 999rpx;
  background: #e9f9ee;
}
.status-badge-text {
  font-size: 20rpx;
  font-weight: 700;
  color: var(--color-success);
}
.status-plan {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 8rpx;
}
.expiry-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 20rpx;
  border-radius: 16rpx;
  background: var(--color-background);
}
.expiry-icon {
  font-size: 28rpx;
}
.expiry-label {
  font-size: 28rpx;
  color: var(--color-text);
}
.expiry-date {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
  margin-left: auto;
}

// 本月统计
.stats-card {
  margin-top: 24rpx;
  padding: 28rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}
.stats-title {
  font-size: 32rpx;
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: 20rpx;
}
.stats-row {
  display: flex;
  gap: 16rpx;
}
.stat-item {
  flex: 1;
  padding: 24rpx 16rpx;
  border-radius: 24rpx;
  background: var(--color-background);
  text-align: center;
}
.stat-icon-wrap {
  width: 56rpx;
  height: 56rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12rpx;
}
.stat-emoji {
  font-size: 28rpx;
}
.stat-value {
  font-size: 36rpx;
  font-weight: 700;
  color: var(--color-text);
}
.stat-label {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}

// 管理订阅
.manage-card {
  margin-top: 24rpx;
  padding: 12rpx 28rpx;
  background: var(--color-card);
  border-radius: 24rpx;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}
.manage-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 0;
  border-bottom: 1rpx solid var(--color-border);
}
.manage-item:last-child {
  border-bottom: none;
}
.manage-left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.manage-icon {
  font-size: 36rpx;
}
.manage-label {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}
.manage-right {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.manage-extra {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-primary);
}
.manage-arrow {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}
</style>
