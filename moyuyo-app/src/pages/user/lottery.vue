<template>
  <view class="lottery">
    <view class="nav-header dark">
      <view class="nav-back" @click="goBack">
        <text class="back-icon light">‹</text>
      </view>
      <text class="nav-title light">幸运抽奖</text>
      <view class="nav-placeholder" />
    </view>

    <scroll-view class="scroll" scroll-y>
      <view class="chances-card">
        <view class="chances-left">
          <view class="chances-icon">
            <text class="ticket-icon">🎟️</text>
          </view>
          <view class="chances-info">
            <text class="chances-num">剩余 {{ remainingSpins }} 次抽奖机会</text>
            <text class="chances-hint">消费满 $50 再获得 1 次</text>
          </view>
        </view>
        <text class="chances-arrow">›</text>
      </view>

      <view class="wheel-section">
        <view class="wheel-container">
          <view class="wheel-outer-ring" />
          <view class="wheel-pointer" />
          <view class="wheel-body" :style="{ transform: 'rotate(' + rotation + 'deg)' }">
            <view class="wheel-labels">
              <text
                v-for="(prize, idx) in prizes"
                :key="idx"
                class="wheel-label"
                :style="{
                  transform: 'rotate(' + (idx * 45 + 22.5) + 'deg)',
                  transformOrigin: '0% 50%',
                }"
              >
                {{ prize.name }}
              </text>
            </view>
          </view>
          <view class="wheel-center" @click="onSpin">
            <text class="center-text">{{ isSpinning ? '...' : '开始' }}</text>
          </view>
        </view>
        <text class="wheel-tip">点击「开始」按钮进行抽奖</text>
      </view>

      <view class="prize-list-section">
        <text class="section-title">🎁 奖品列表</text>
        <view class="prize-grid">
          <view v-for="prize in prizes" :key="prize.name" class="prize-item">
            <view class="prize-icon-wrap" :style="{ background: prize.bg }">
              <text class="prize-icon">{{ prize.icon }}</text>
            </view>
            <text class="prize-name">{{ prize.name }}</text>
          </view>
        </view>
      </view>

      <view class="history-section">
        <view class="history-header" @click="showHistory = !showHistory">
          <view class="history-title-row">
            <text class="history-title">🕐 抽奖记录</text>
            <text class="history-count">{{ spinHistory.length }}</text>
          </view>
          <text class="history-arrow" :class="{ open: showHistory }">▼</text>
        </view>
        <view v-if="showHistory" class="history-list">
          <view v-for="(record, idx) in spinHistory" :key="idx" class="history-item">
            <view class="history-icon-wrap" :style="{ background: record.bg }">
              <text class="history-icon">{{ record.icon }}</text>
            </view>
            <text class="history-name">{{ record.name }}</text>
            <text class="history-time">{{ record.time }}</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <view v-if="showPrizeModal" class="modal-overlay" @click="showPrizeModal = false">
      <view class="modal-content" @click.stop>
        <view class="modal-icon-wrap" :style="{ background: currentPrize.bg }">
          <text class="modal-icon">{{ currentPrize.icon }}</text>
        </view>
        <text class="modal-title">
          {{ currentPrize.name === '谢谢参与' ? '差一点就中了' : '恭喜中奖' }}
        </text>
        <text class="modal-desc">{{ currentPrize.desc }}</text>
        <view class="modal-btn" @click="showPrizeModal = false">收下奖品</view>
      </view>
    </view>
  </view>
</template>

<script>
import { lotteryApi } from '@/api'

export default {
  data() {
    return {
      prizes: [],
      rotation: 0,
      isSpinning: false,
      remainingSpins: 0,
      showHistory: false,
      showPrizeModal: false,
      currentPrize: null,
      spinHistory: [],
    }
  },

  onLoad() {
    this.loadLotteries()
    this.loadLotteryStats()
  },

  methods: {
    goBack() {
      uni.navigateBack()
    },

    async loadLotteries() {
      try {
        const res = await lotteryApi.getLotteries()
        this.prizes = res.data || []
        this.currentPrize = this.prizes[0] || null
      } catch (err) {
        uni.showToast({ title: '加载奖品信息失败', icon: 'none' })
      }
    },

    async loadLotteryStats() {
      try {
        const res = await lotteryApi.getLotteryStats()
        this.remainingSpins = res.data.remainingSpins || 0
        this.spinHistory = res.data.history || []
      } catch (err) {
        console.error('加载抽奖统计失败', err)
      }
    },

    async onSpin() {
      if (this.isSpinning) return
      if (this.remainingSpins <= 0) {
        uni.showToast({ title: '抽奖次数已用完', icon: 'none' })
        return
      }

      this.isSpinning = true

      try {
        const res = await lotteryApi.spin()
        const prize = res.data

        const sectorAngle = 360 / this.prizes.length
        const prizeIndex = this.prizes.findIndex((p) => p.name === prize.name)
        const targetIdx = prizeIndex >= 0 ? prizeIndex : 0
        const targetAngle = 360 * 5 + (360 - (targetIdx * sectorAngle + sectorAngle / 2))
        this.rotation += targetAngle

        setTimeout(() => {
          this.isSpinning = false
          this.remainingSpins--
          this.currentPrize = prize
          this.showPrizeModal = true
          this.spinHistory.unshift({
            name: prize.name,
            icon: prize.icon,
            bg: prize.bg,
            time: new Date().toLocaleString('zh-CN', {
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit',
            }),
          })
        }, 4200)
      } catch (err) {
        this.isSpinning = false
        uni.showToast({ title: err.message || '抽奖失败', icon: 'none' })
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.lottery {
  min-height: 100vh;
  background: var(--color-background);
}

.nav-header.dark {
  background: var(--color-text);
  border-bottom: none;
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 24rpx;
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

.chances-card {
  margin: 24rpx;
  padding: 24rpx;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--color-primary-dark), var(--color-primary));
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.chances-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.chances-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}

.ticket-icon {
  font-size: 32rpx;
}

.chances-info {
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.chances-num {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: #fff;
}

.chances-hint {
  font-size: var(--font-size-xs);
  color: rgba(255, 255, 255, 0.75);
}

.chances-arrow {
  font-size: 32rpx;
  color: #fff;
}

.wheel-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx;
}

.wheel-container {
  position: relative;
  width: 520rpx;
  height: 520rpx;
}

.wheel-outer-ring {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  border: 8rpx solid var(--color-primary);
  box-shadow:
    0 0 0 12rpx var(--color-primary-light),
    0 0 0 20rpx var(--color-primary-light),
    var(--shadow-lg);
}

.wheel-pointer {
  position: absolute;
  top: -4rpx;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  width: 0;
  height: 0;
  border-left: 24rpx solid transparent;
  border-right: 24rpx solid transparent;
  border-top: 48rpx solid var(--color-primary);
  filter: drop-shadow(0 2rpx 4rpx rgba(0, 0, 0, 0.2));
}

.wheel-body {
  position: absolute;
  inset: 8rpx;
  border-radius: 50%;
  overflow: hidden;
  background: conic-gradient(
    #dbc98a 0deg 45deg,
    #abb9ad 45deg 90deg,
    #e6b97a 90deg 135deg,
    #b38a5a 135deg 180deg,
    #af8f6f 180deg 225deg,
    #c96e5f 225deg 270deg,
    #8fa8b6 270deg 315deg,
    #d9b4b0 315deg 360deg
  );
  transition: transform 4s cubic-bezier(0.17, 0.67, 0.12, 0.99);
}

.wheel-labels {
  position: absolute;
  inset: 0;
}

.wheel-label {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 50%;
  height: 4rpx;
  display: flex;
  align-items: center;
  font-size: 18rpx;
  font-weight: var(--font-weight-bold);
  color: #fff;
  text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.3);
  padding-left: 40%;
  white-space: nowrap;
}

.wheel-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 20;
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  background: var(--color-primary);
  border: 6rpx solid #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 24rpx rgba(219, 201, 138, 0.4);
}

.center-text {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}

.wheel-tip {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  margin-top: 24rpx;
  text-align: center;
}

.prize-list-section {
  padding: 0 24rpx 24rpx;
}

.section-title {
  display: block;
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
  margin-bottom: 16rpx;
}

.prize-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16rpx;
}

.prize-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.prize-icon-wrap {
  width: 80rpx;
  height: 80rpx;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
}

.prize-icon {
  font-size: 32rpx;
}

.prize-name {
  font-size: 20rpx;
  color: var(--color-text-secondary);
  text-align: center;
}

.history-section {
  margin: 0 24rpx;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  overflow: hidden;
  border: 1rpx solid var(--color-divider);
}

.history-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
}

.history-title-row {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.history-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.history-count {
  font-size: var(--font-size-xs);
  padding: 2rpx 12rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary-light);
  color: var(--color-primary-dark);
}

.history-arrow {
  font-size: 20rpx;
  color: var(--color-text-tertiary);
  transition: transform 0.2s;
}

.history-arrow.open {
  transform: rotate(180deg);
}

.history-list {
  padding: 0 24rpx 24rpx;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 16rpx 0;
  border-bottom: 1rpx solid var(--color-divider);
}

.history-item:last-child {
  border-bottom: none;
}

.history-icon-wrap {
  width: 56rpx;
  height: 56rpx;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
}

.history-icon {
  font-size: 28rpx;
}

.history-name {
  flex: 1;
  font-size: var(--font-size-sm);
  color: var(--color-text);
}

.history-time {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
}

.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 50;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  width: 80%;
  max-width: 560rpx;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 48rpx;
  text-align: center;
}

.modal-icon-wrap {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24rpx;
}

.modal-icon {
  font-size: 56rpx;
}

.modal-title {
  display: block;
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
  margin-bottom: 12rpx;
}

.modal-desc {
  display: block;
  font-size: var(--font-size-sm);
  color: var(--color-text-tertiary);
  margin-bottom: 32rpx;
}

.modal-btn {
  height: 80rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}
</style>
