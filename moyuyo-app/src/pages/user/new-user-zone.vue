<template>
  <view class="new-user-zone">
    <view class="nav-header accent">
      <view class="nav-back" @click="goBack">
        <text class="back-icon light">‹</text>
      </view>
      <text class="nav-title light">新人专区</text>
      <view class="nav-placeholder" />
    </view>

    <scroll-view class="scroll" scroll-y>
      <view class="hero-section">
        <text class="hero-title">欢迎加入 MOYUYO</text>
        <text class="hero-subtitle">新人专属福利已到账</text>
        <view class="hero-countdown">
          <text class="countdown-icon">⏰</text>
          <text class="countdown-text">新人权益有效期还剩 28 天</text>
        </view>
      </view>

      <view class="gift-card">
        <view class="gift-header">
          <text class="gift-title">新人大礼包</text>
          <text class="gift-link">查看全部</text>
        </view>
        <view class="gift-row">
          <view class="gift-item">
            <view class="gift-icon-wrap">
              <text class="gift-icon">🏷️</text>
            </view>
            <text class="gift-name">首单8折券</text>
            <text class="gift-desc">最高减 $20</text>
          </view>
          <view class="gift-item">
            <view class="gift-icon-wrap green">
              <text class="gift-icon">📦</text>
            </view>
            <text class="gift-name">免邮券</text>
            <text class="gift-desc">无门槛使用</text>
          </view>
          <view class="gift-item">
            <view class="gift-icon-wrap success">
              <text class="gift-icon">✓</text>
            </view>
            <text class="gift-name">$5 积分</text>
            <text class="gift-desc success-text">已到账</text>
          </view>
        </view>
      </view>

      <view class="section-header">
        <text class="section-title">新人专属价</text>
        <text class="section-tag">限时特惠</text>
      </view>

      <view class="product-grid">
        <view
          v-for="product in exclusiveProducts"
          :key="product.id"
          class="product-card"
          @click="onProductClick(product)"
        >
          <view class="product-img" :style="{ background: product.color }">
            <text v-if="product.discount" class="discount-tag">{{ product.discount }}</text>
            <text class="product-placeholder">{{ product.name.slice(0, 2) }}</text>
          </view>
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-desc">{{ product.desc }}</text>
            <view class="product-price-row">
              <text class="product-price">${{ product.price }}</text>
              <text class="product-original">${{ product.originalPrice }}</text>
            </view>
          </view>
        </view>
      </view>

      <view class="tasks-section">
        <text class="section-title">完成任务赚积分</text>
        <view v-for="task in tasks" :key="task.id" class="task-item">
          <view class="task-icon-wrap" :class="task.colorClass">
            <text class="task-icon">{{ task.icon }}</text>
          </view>
          <view class="task-info">
            <view class="task-name-row">
              <text class="task-name">{{ task.name }}</text>
              <text class="task-points">+{{ task.points }} 积分</text>
            </view>
            <view class="task-progress">
              <view class="task-bar">
                <view class="task-fill" :style="{ width: task.progress + '%' }" />
              </view>
              <text class="task-status">{{ task.statusText }}</text>
            </view>
          </view>
          <view class="task-action" @click="onTask(task)">{{ task.actionText }}</view>
        </view>
      </view>

      <view class="explore-btn" @click="onExplore">
        <text>查看全部新人价 →</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      exclusiveProducts: [
        {
          id: 1,
          name: '高端宠物洗护套装',
          desc: '温和清洁 持久留香',
          price: '95',
          originalPrice: '189',
          discount: '低至5折',
          color: 'linear-gradient(135deg, #e8f2ff, #cfe5ff)',
        },
        {
          id: 2,
          name: '舒适胸背带',
          desc: '透气网布 均匀受力',
          price: '65',
          originalPrice: '129',
          discount: '低至5折',
          color: 'linear-gradient(135deg, #f2f2f7, #d1d1d6)',
        },
        {
          id: 3,
          name: '互动益智玩具',
          desc: '耐咬材质 释放天性',
          price: '35',
          originalPrice: '69',
          discount: '低至5折',
          color: 'linear-gradient(135deg, #fff3e0, #ffe0b2)',
        },
        {
          id: 4,
          name: '宠物潮流外套',
          desc: '防水面料 春秋百搭',
          price: '110',
          originalPrice: '219',
          discount: '低至5折',
          color: 'linear-gradient(135deg, #fce4ec, #f8bbd0)',
        },
      ],
      tasks: [
        {
          id: 1,
          name: '完善宠物档案',
          points: 50,
          progress: 0,
          statusText: '未完成',
          actionText: '去完成',
          icon: '✏️',
          colorClass: 'brand',
        },
        {
          id: 2,
          name: '设置护理提醒',
          points: 30,
          progress: 0,
          statusText: '未完成',
          actionText: '去完成',
          icon: '⏰',
          colorClass: 'green',
        },
        {
          id: 3,
          name: '完成首单',
          points: 100,
          progress: 0,
          statusText: '未完成',
          actionText: '去完成',
          icon: '⭐',
          colorClass: 'brand',
        },
      ],
    }
  },

  methods: {
    goBack() {
      uni.navigateBack()
    },

    onProductClick(product) {
      uni.showToast({ title: product.name, icon: 'none' })
    },

    onTask(task) {
      uni.showToast({ title: `前往${task.name}`, icon: 'none' })
    },

    onExplore() {
      uni.showToast({ title: '查看全部新人价', icon: 'none' })
    },
  },
}
</script>

<style lang="scss" scoped>
.new-user-zone {
  min-height: 100vh;
  background: var(--color-background);
}

.nav-header.accent {
  background: var(--color-primary-dark);
  border-bottom: none;
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 24rpx;
  background: var(--color-surface);
  border-bottom: 1rpx solid var(--color-divider);
  position: sticky;
  top: 0;
  z-index: 10;
}

.nav-back {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 48rpx;
  color: var(--color-primary);
  line-height: 1;
}

.back-icon.light {
  color: #fff;
}

.nav-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.nav-title.light {
  color: #fff;
}

.nav-placeholder {
  width: 60rpx;
}

.scroll {
  height: calc(100vh - 88rpx);
  padding-bottom: 48rpx;
}

.hero-section {
  background: linear-gradient(180deg, var(--color-primary-dark), var(--color-primary));
  padding: 48rpx 32rpx 64rpx;
  text-align: center;
}

.hero-title {
  display: block;
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: #fff;
}

.hero-subtitle {
  display: block;
  font-size: var(--font-size-sm);
  color: rgba(255, 255, 255, 0.85);
  margin-top: 12rpx;
}

.hero-countdown {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 24rpx;
  padding: 12rpx 24rpx;
  border-radius: var(--radius-pill);
  background: rgba(255, 255, 255, 0.15);
}

.countdown-icon {
  font-size: 24rpx;
}

.countdown-text {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: #fff;
}

.gift-card {
  margin: -32rpx 24rpx 0;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 24rpx;
  position: relative;
  z-index: 2;
  border: 1rpx solid var(--color-divider);
}

.gift-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.gift-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.gift-link {
  font-size: var(--font-size-xs);
  color: var(--color-primary);
  font-weight: var(--font-weight-medium);
}

.gift-row {
  display: flex;
  gap: 16rpx;
}

.gift-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
  padding: 16rpx 8rpx;
  border-radius: var(--radius-sm);
  background: var(--color-surface);
  border: 1rpx solid var(--color-divider);
}

.gift-icon-wrap {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: var(--color-primary-light);
  display: flex;
  align-items: center;
  justify-content: center;
}

.gift-icon-wrap.green {
  background: rgba(171, 185, 173, 0.15);
}

.gift-icon-wrap.success {
  background: rgba(171, 185, 173, 0.15);
}

.gift-icon {
  font-size: 28rpx;
}

.gift-name {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}

.gift-desc {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
}

.gift-desc.success-text {
  color: var(--color-success);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 32rpx 24rpx 16rpx;
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.section-tag {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  padding: 4rpx 12rpx;
  border-radius: var(--radius-pill);
  background: rgba(201, 110, 95, 0.1);
  color: var(--color-danger);
}

.product-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16rpx;
  padding: 0 24rpx;
}

.product-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  overflow: hidden;
  border: 1rpx solid var(--color-divider);
}

.product-img {
  width: 100%;
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.product-placeholder {
  font-size: 28rpx;
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-semibold);
}

.discount-tag {
  position: absolute;
  top: 12rpx;
  left: 12rpx;
  padding: 4rpx 10rpx;
  border-radius: 6rpx;
  font-size: 18rpx;
  font-weight: var(--font-weight-bold);
  color: #fff;
  background: var(--color-danger);
}

.product-info {
  padding: 16rpx;
}

.product-name {
  display: block;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-desc {
  display: block;
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  margin-top: 4rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price-row {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
  margin-top: 12rpx;
}

.product-price {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-bold);
  color: var(--color-danger);
}

.product-original {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  text-decoration: line-through;
}

.tasks-section {
  padding: 32rpx 24rpx;
}

.tasks-section .section-title {
  padding: 0;
  margin-bottom: 16rpx;
}

.task-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 20rpx;
  background: var(--color-surface);
  border-radius: var(--radius-sm);
  border: 1rpx solid var(--color-divider);
  margin-bottom: 12rpx;
}

.task-icon-wrap {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.task-icon-wrap.brand {
  background: var(--color-primary-light);
}

.task-icon-wrap.green {
  background: rgba(171, 185, 173, 0.15);
}

.task-icon {
  font-size: 28rpx;
}

.task-info {
  flex: 1;
  min-width: 0;
}

.task-name-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.task-name {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}

.task-points {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-bold);
  color: var(--color-primary-dark);
}

.task-progress {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-top: 8rpx;
}

.task-bar {
  flex: 1;
  height: 6rpx;
  border-radius: 3rpx;
  background: var(--color-divider);
  overflow: hidden;
}

.task-fill {
  height: 100%;
  border-radius: 3rpx;
  background: var(--color-text-tertiary);
}

.task-status {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  flex-shrink: 0;
}

.task-action {
  padding: 12rpx 24rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary);
  color: var(--color-text);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  flex-shrink: 0;
}

.explore-btn {
  margin: 0 24rpx;
  height: 88rpx;
  border-radius: var(--radius-md);
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}
</style>
