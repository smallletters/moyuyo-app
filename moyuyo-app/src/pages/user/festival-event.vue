<template>
  <view class="festival-event">
    <view class="hero-banner">
      <view class="nav-row">
        <view class="nav-btn" @click="goBack">
          <text class="nav-btn-icon">‹</text>
        </view>
        <view class="nav-btn" @click="onShare">
          <text class="nav-btn-icon">⇲</text>
        </view>
      </view>
      <text class="hero-title">夏日宠物狂欢节</text>
      <text class="hero-subtitle">全场低至5折 · 限时7天</text>
      <view class="countdown-row">
        <view class="countdown-block">
          <text id="countdown-days" class="countdown-num">{{ countdown.days }}</text>
          <text class="countdown-unit">天</text>
        </view>
        <text class="countdown-sep">:</text>
        <view class="countdown-block">
          <text id="countdown-hours" class="countdown-num">{{ countdown.hours }}</text>
          <text class="countdown-unit">时</text>
        </view>
        <text class="countdown-sep">:</text>
        <view class="countdown-block">
          <text id="countdown-mins" class="countdown-num">{{ countdown.mins }}</text>
          <text class="countdown-unit">分</text>
        </view>
        <text class="countdown-sep">:</text>
        <view class="countdown-block">
          <text id="countdown-secs" class="countdown-num">{{ countdown.secs }}</text>
          <text class="countdown-unit">秒</text>
        </view>
      </view>
    </view>

    <scroll-view class="scroll" scroll-y>
      <view class="flash-section">
        <view class="flash-header">
          <view class="flash-title-row">
            <text class="flash-icon">🔔</text>
            <text class="flash-title">限时抢购</text>
          </view>
          <text class="flash-more">更多</text>
        </view>
        <scroll-view class="flash-scroll" scroll-x show-scrollbar="false">
          <view v-for="item in flashItems" :key="item.id" class="flash-card">
            <view class="flash-img" :style="{ background: item.color }">
              <text class="flash-placeholder">{{ item.name.slice(0, 2) }}</text>
            </view>
            <view class="flash-info">
              <text class="flash-name">{{ item.name }}</text>
              <view class="flash-price-row">
                <text class="flash-price">${{ item.price }}</text>
                <text class="flash-original">${{ item.originalPrice }}</text>
              </view>
              <view class="flash-timer-row">
                <text class="flash-tag">{{ item.discount }}</text>
                <text class="flash-timer">{{ item.timer }}</text>
              </view>
            </view>
          </view>
        </scroll-view>
      </view>

      <view class="coupon-section">
        <view class="coupon-header">
          <text class="coupon-icon">🏷️</text>
          <text class="coupon-title">满减专区</text>
        </view>
        <view v-for="coupon in coupons" :key="coupon.id" class="coupon-card">
          <view class="coupon-info">
            <text class="coupon-name">{{ coupon.name }}</text>
            <text class="coupon-desc">{{ coupon.desc }}</text>
          </view>
          <view class="coupon-btn" @click="onClaimCoupon(coupon)">立即领取</view>
        </view>
      </view>

      <view class="event-list-section">
        <text class="section-title">活动列表</text>
        <view v-for="event in events" :key="event.id" class="event-card">
          <view class="event-header">
            <view class="event-icon-wrap" :style="{ background: event.bgColor }">
              <text class="event-icon">{{ event.icon }}</text>
            </view>
            <view class="event-info">
              <text class="event-name">{{ event.name }}</text>
              <text class="event-desc">{{ event.desc }}</text>
            </view>
          </view>
          <view class="event-footer">
            <text class="event-time">⏱ {{ event.timeLeft }}</text>
            <text class="event-prize">🎁 {{ event.prize }}</text>
            <view class="event-btn" @click="onParticipate(event)">参与</view>
          </view>
        </view>
      </view>

      <view class="rules-section">
        <view class="rules-header" @click="showEventRules = !showEventRules">
          <text class="rules-title">📋 活动规则</text>
          <text class="rules-arrow" :class="{ open: showEventRules }">▼</text>
        </view>
        <view v-if="showEventRules" class="rules-content">
          <text class="rule-line">1. 活动时间：2025年7月1日 - 2025年7月31日</text>
          <text class="rule-line">2. 满减券不可叠加使用，每单限用一张</text>
          <text class="rule-line">3. 限时抢购商品数量有限，售完即止</text>
          <text class="rule-line">4. 活动最终解释权归 MOYUYO 所有</text>
        </view>
      </view>

      <view class="rewards-section">
        <text class="section-title">我的奖励</text>
        <view v-if="myRewards.length === 0" class="empty-state">
          <text class="empty-text">暂无奖励，快来参与活动吧</text>
        </view>
        <view v-for="reward in myRewards" :key="reward.id" class="reward-item">
          <text class="reward-icon">{{ reward.icon }}</text>
          <text class="reward-name">{{ reward.name }}</text>
          <text class="reward-status" :class="reward.status">{{ reward.statusText }}</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      countdownTimer: null,
      endTime: Date.now() + 7 * 24 * 60 * 60 * 1000,
      countdown: { days: '03', hours: '08', mins: '42', secs: '15' },
      showEventRules: false,
      flashItems: [
        {
          id: 1,
          name: '高端宠物洗护套装',
          price: '95',
          originalPrice: '189',
          discount: '5折',
          timer: '02:15:30',
          color: 'linear-gradient(135deg, #e8f2ff, #cfe5ff)',
        },
        {
          id: 2,
          name: '舒适胸背带',
          price: '65',
          originalPrice: '129',
          discount: '5折',
          timer: '01:45:12',
          color: 'linear-gradient(135deg, #f2f2f7, #d1d1d6)',
        },
        {
          id: 3,
          name: '互动益智玩具',
          price: '35',
          originalPrice: '69',
          discount: '5折',
          timer: '03:28:45',
          color: 'linear-gradient(135deg, #fff3e0, #ffe0b2)',
        },
      ],
      coupons: [
        { id: 1, name: '满 $300 减 $60', desc: '全品类通用 · 可叠加用券' },
        { id: 2, name: '满 $200 减 $35', desc: '品牌精选 · 部分商品可用' },
        { id: 3, name: '满 $100 减 $15', desc: '新人专享 · 首单可用' },
      ],
      events: [
        {
          id: 1,
          name: '整点秒杀',
          desc: '每日10点/20点开抢',
          icon: '⚡',
          bgColor: 'rgba(219, 201, 138, 0.15)',
          timeLeft: '2小时',
          prize: '5折券',
        },
        {
          id: 2,
          name: '分享有礼',
          desc: '分享活动给好友领奖励',
          icon: '🎁',
          bgColor: 'rgba(171, 185, 173, 0.15)',
          timeLeft: '长期有效',
          prize: '$5积分',
        },
        {
          id: 3,
          name: '新人专享',
          desc: '首单立减 $15',
          icon: '🆕',
          bgColor: 'rgba(201, 110, 95, 0.1)',
          timeLeft: '30天',
          prize: '$15券',
        },
      ],
      myRewards: [
        { id: 1, icon: '🏷️', name: '满 $300 减 $60', status: 'used', statusText: '已使用' },
        { id: 2, icon: '🎫', name: '整点秒杀 5 折券', status: 'active', statusText: '可使用' },
      ],
    }
  },

  mounted() {
    this.startCountdown()
  },

  beforeUnmount() {
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer)
    }
  },

  methods: {
    goBack() {
      uni.navigateBack()
    },

    onShare() {
      uni.showToast({ title: '分享活动', icon: 'none' })
    },

    onClaimCoupon(coupon) {
      uni.showToast({ title: `已领取 ${coupon.name}`, icon: 'success' })
    },

    onParticipate(event) {
      uni.showToast({ title: `参与 ${event.name}`, icon: 'none' })
    },

    startCountdown() {
      const pad = (n) => (n < 10 ? '0' + n : '' + n)
      this.countdownTimer = setInterval(() => {
        const diff = Math.max(0, this.endTime - Date.now())
        this.countdown.days = pad(Math.floor(diff / (1000 * 60 * 60 * 24)))
        this.countdown.hours = pad(Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)))
        this.countdown.mins = pad(Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60)))
        this.countdown.secs = pad(Math.floor((diff % (1000 * 60)) / 1000))
      }, 1000)
    },
  },
}
</script>

<style lang="scss" scoped>
.festival-event {
  min-height: 100vh;
  background: var(--color-background);
}

.hero-banner {
  background: linear-gradient(
    135deg,
    var(--color-primary-dark),
    var(--color-primary),
    var(--color-primary-light)
  );
  padding: 24rpx 24rpx 48rpx;
  text-align: center;
}

.nav-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 32rpx;
}

.nav-btn {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-btn-icon {
  font-size: 32rpx;
  color: #fff;
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
  margin-top: 8rpx;
}

.countdown-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  margin-top: 32rpx;
}

.countdown-block {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.countdown-num {
  width: 96rpx;
  height: 96rpx;
  border-radius: var(--radius-sm);
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: #fff;
}

.countdown-unit {
  font-size: 18rpx;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 8rpx;
}

.countdown-sep {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: rgba(255, 255, 255, 0.5);
  margin-top: -32rpx;
}

.scroll {
  height: calc(100vh - 88rpx);
  padding-bottom: 48rpx;
}

.flash-section {
  background: var(--color-background);
  padding: 24rpx;
}

.flash-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.flash-title-row {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.flash-icon {
  font-size: 28rpx;
}

.flash-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}

.flash-more {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
}

.flash-scroll {
  display: flex;
  gap: 16rpx;
  white-space: nowrap;
}

.flash-card {
  display: inline-block;
  width: 240rpx;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  overflow: hidden;
  border: 1rpx solid var(--color-divider);
  margin-right: 16rpx;
}

.flash-img {
  width: 100%;
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.flash-placeholder {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-semibold);
}

.flash-info {
  padding: 16rpx;
}

.flash-name {
  display: block;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.flash-price-row {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
  margin-top: 8rpx;
}

.flash-price {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-bold);
  color: var(--color-danger);
}

.flash-original {
  font-size: 18rpx;
  color: var(--color-text-tertiary);
  text-decoration: line-through;
}

.flash-timer-row {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 8rpx;
}

.flash-tag {
  padding: 2rpx 8rpx;
  border-radius: 4rpx;
  font-size: 18rpx;
  font-weight: var(--font-weight-bold);
  color: #fff;
  background: var(--color-danger);
}

.flash-timer {
  font-size: 18rpx;
  color: var(--color-danger);
  font-weight: var(--font-weight-semibold);
  font-variant-numeric: tabular-nums;
}

.coupon-section {
  padding: 24rpx;
  background: var(--color-surface);
}

.coupon-header {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 16rpx;
}

.coupon-icon {
  font-size: 28rpx;
}

.coupon-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}

.coupon-card {
  display: flex;
  align-items: center;
  padding: 20rpx;
  border-radius: var(--radius-sm);
  border: 1rpx solid var(--color-divider);
  margin-bottom: 12rpx;
}

.coupon-info {
  flex: 1;
}

.coupon-name {
  display: block;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}

.coupon-desc {
  display: block;
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  margin-top: 4rpx;
}

.coupon-btn {
  padding: 12rpx 24rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary);
  color: var(--color-text);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
}

.event-list-section {
  padding: 24rpx;
}

.section-title {
  display: block;
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: 16rpx;
}

.event-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 20rpx;
  margin-bottom: 12rpx;
  border: 1rpx solid var(--color-divider);
}

.event-header {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.event-icon-wrap {
  width: 72rpx;
  height: 72rpx;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
}

.event-icon {
  font-size: 32rpx;
}

.event-info {
  flex: 1;
}

.event-name {
  display: block;
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.event-desc {
  display: block;
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  margin-top: 4rpx;
}

.event-footer {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-top: 16rpx;
  padding-top: 16rpx;
  border-top: 1rpx solid var(--color-divider);
}

.event-time {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
}

.event-prize {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  flex: 1;
}

.event-btn {
  padding: 10rpx 24rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary);
  color: var(--color-text);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
}

.rules-section {
  margin: 0 24rpx;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  overflow: hidden;
  border: 1rpx solid var(--color-divider);
}

.rules-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
}

.rules-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.rules-arrow {
  font-size: 20rpx;
  color: var(--color-text-tertiary);
  transition: transform 0.2s;
}

.rules-arrow.open {
  transform: rotate(180deg);
}

.rules-content {
  padding: 0 24rpx 24rpx;
}

.rule-line {
  display: block;
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  line-height: 1.8;
}

.rewards-section {
  padding: 24rpx;
}

.empty-state {
  padding: 48rpx 0;
  text-align: center;
}

.empty-text {
  font-size: var(--font-size-sm);
  color: var(--color-text-tertiary);
}

.reward-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 16rpx 20rpx;
  background: var(--color-surface);
  border-radius: var(--radius-sm);
  margin-bottom: 8rpx;
  border: 1rpx solid var(--color-divider);
}

.reward-icon {
  font-size: 28rpx;
}

.reward-name {
  flex: 1;
  font-size: var(--font-size-sm);
  color: var(--color-text);
}

.reward-status {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
}

.reward-status.active {
  color: var(--color-success);
}

.reward-status.used {
  color: var(--color-text-tertiary);
}
</style>
