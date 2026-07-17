<template>
  <view class="booking-detail">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">预约详情</text>
      <view class="header-spacer" />
    </view>

    <scroll-view class="content" scroll-y>
      <!-- 预约状态大卡片 -->
      <view class="status-card" :class="'status-' + booking.status">
        <view class="status-top">
          <view class="status-icon-wrap">
            <text class="status-emoji">{{ statusEmoji }}</text>
          </view>
          <view class="status-info">
            <text class="status-title">{{ statusTitle }}</text>
            <text class="status-desc">{{ statusDesc }}</text>
          </view>
        </view>
        <!-- 预约编号 -->
        <view class="booking-id-row">
          <text class="booking-id-label">预约编号</text>
          <text class="booking-id-value">{{ booking.bookingId }}</text>
        </view>
      </view>

      <!-- 预约项目信息 -->
      <view class="info-card">
        <text class="card-title">预约项目</text>
        <view class="service-row">
          <image :src="booking.image" class="service-image" mode="aspectFill" />
          <view class="service-info">
            <text class="service-name">{{ booking.serviceName }}</text>
            <text class="service-spec">{{ booking.serviceSpec }}</text>
            <view class="service-price-row">
              <text class="service-price">¥{{ booking.price }}</text>
              <text class="service-duration">{{ booking.duration }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 预约时间信息 -->
      <view class="info-card">
        <text class="card-title">预约时间</text>
        <view class="time-row">
          <view class="time-icon-wrap">
            <text class="time-emoji">📅</text>
          </view>
          <view class="time-info">
            <text class="time-date">{{ booking.date }}</text>
            <text class="time-slot">{{ booking.timeSlot }}</text>
          </view>
        </view>
      </view>

      <!-- 预约人信息 -->
      <view class="info-card">
        <text class="card-title">预约信息</text>
        <view class="detail-rows">
          <view class="detail-row">
            <text class="detail-label">联系人</text>
            <text class="detail-value">{{ booking.contactName }}</text>
          </view>
          <view class="detail-divider" />
          <view class="detail-row">
            <text class="detail-label">联系电话</text>
            <text class="detail-value">{{ booking.contactPhone }}</text>
          </view>
          <view class="detail-divider" />
          <view class="detail-row">
            <text class="detail-label">宠物</text>
            <text class="detail-value">{{ booking.petName }}（{{ booking.petType }}）</text>
          </view>
          <view v-if="booking.remark" class="detail-divider" />
          <view v-if="booking.remark" class="detail-row">
            <text class="detail-label">备注</text>
            <text class="detail-value">{{ booking.remark }}</text>
          </view>
        </view>
      </view>

      <!-- 门店信息 -->
      <view class="info-card">
        <text class="card-title">门店信息</text>
        <view class="store-row">
          <view class="store-info">
            <text class="store-name">{{ booking.storeName }}</text>
            <text class="store-address">{{ booking.storeAddress }}</text>
          </view>
          <view class="store-actions">
            <view class="store-action-btn" @click="callStore">
              <text class="store-action-icon">📞</text>
            </view>
            <view class="store-action-btn" @click="navigateStore">
              <text class="store-action-icon">🧭</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 费用明细 -->
      <view class="info-card">
        <text class="card-title">费用明细</text>
        <view class="fee-rows">
          <view class="fee-row">
            <text class="fee-label">服务费用</text>
            <text class="fee-value">¥{{ booking.price }}</text>
          </view>
          <view v-if="booking.discount" class="fee-row">
            <text class="fee-label fee-label-discount">优惠折扣</text>
            <text class="fee-value fee-value-discount">-¥{{ booking.discount }}</text>
          </view>
          <view class="fee-divider" />
          <view class="fee-row fee-total">
            <text class="fee-label-total">应付金额</text>
            <text class="fee-value-total">¥{{ totalAmount }}</text>
          </view>
        </view>
      </view>

      <!-- 底部操作按钮 -->
      <view class="action-section">
        <!-- 待确认状态 -->
        <template v-if="booking.status === 'pending'">
          <button class="action-btn action-btn-primary" @click="cancelBooking">取消预约</button>
        </template>
        <!-- 已确认状态 -->
        <template v-if="booking.status === 'confirmed'">
          <button class="action-btn action-btn-outline" @click="rescheduleBooking">改期</button>
          <button class="action-btn action-btn-primary" @click="cancelBooking">取消预约</button>
        </template>
        <!-- 已完成状态 -->
        <template v-if="booking.status === 'completed'">
          <button class="action-btn action-btn-primary" @click="rebookService">再次预约</button>
        </template>
        <!-- 已取消状态 -->
        <template v-if="booking.status === 'cancelled'">
          <button class="action-btn action-btn-primary" @click="rebookService">重新预约</button>
        </template>
      </view>

      <!-- 温馨提示 -->
      <view class="tip-box">
        <text class="tip-icon">💡</text>
        <text class="tip-text">
          请提前 15 分钟到店，如需改期或取消请至少提前 2 小时操作。迟到超过 15 分钟预约将自动取消。
        </text>
      </view>

      <view class="bottom-spacer" />
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

// 预约详情 mock 数据
const booking = ref({
  bookingId: 'BK20260715001',
  status: 'confirmed',
  serviceName: '宠物美容 - 全套洗护',
  serviceSpec: '含洗澡 / 吹干 / 修剪 / 美甲',
  price: '128.00',
  discount: '12.80',
  duration: '约 90 分钟',
  date: '2026年7月18日（周五）',
  timeSlot: '14:00 - 15:30',
  contactName: '张小明',
  contactPhone: '138****5678',
  petName: '小橘',
  petType: '橘猫',
  remark: '性格较胆小，请温柔对待',
  storeName: 'MOYUYO 体验店 · 朝阳店',
  storeAddress: '北京市朝阳区建国路 88 号',
  image: '/static/images/service-grooming.png',
})

// 状态图标
const statusEmoji = computed(() => {
  const map = {
    pending: '⏳',
    confirmed: '✓',
    completed: '🎉',
    cancelled: '✕',
  }
  return map[booking.value.status] || '⏳'
})

// 状态标题
const statusTitle = computed(() => {
  const map = {
    pending: '待确认',
    confirmed: '预约已确认',
    completed: '服务已完成',
    cancelled: '预约已取消',
  }
  return map[booking.value.status] || '未知状态'
})

// 状态描述
const statusDesc = computed(() => {
  const map = {
    pending: '门店正在确认您的预约，请稍候',
    confirmed: '请按时到店享受服务',
    completed: '感谢您的光临，欢迎再次预约',
    cancelled: '预约已取消，可重新预约',
  }
  return map[booking.value.status] || ''
})

// 应付金额
const totalAmount = computed(() => {
  const price = parseFloat(booking.value.price)
  const discount = parseFloat(booking.value.discount || '0')
  return (price - discount).toFixed(2)
})

// 返回
const goBack = () => {
  uni.navigateBack()
}

// 取消预约
const cancelBooking = () => {
  uni.showModal({
    title: '取消预约',
    content: '确定要取消此预约吗？取消后需重新预约。',
    confirmColor: '#C96E5F',
    success: (res) => {
      if (res.confirm) {
        booking.value.status = 'cancelled'
        uni.showToast({ title: '预约已取消', icon: 'success' })
      }
    },
  })
}

// 改期
const rescheduleBooking = () => {
  uni.showToast({ title: '选择新的预约时间', icon: 'none' })
}

// 再次预约
const rebookService = () => {
  uni.navigateTo({ url: '/pages/goods/product-subscribe' })
}

// 拨打门店电话
const callStore = () => {
  uni.makePhoneCall({ phoneNumber: '010-88888888' })
}

// 导航到门店
const navigateStore = () => {
  uni.openLocation({
    latitude: 39.908823,
    longitude: 116.39747,
    name: booking.value.storeName,
    address: booking.value.storeAddress,
  })
}
</script>

<style lang="scss" scoped>
.booking-detail {
  min-height: 100vh;
  background: var(--color-background);
}

.header {
  position: sticky;
  top: 0;
  z-index: 30;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 88rpx;
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(20px);
  border-bottom: 1rpx solid var(--color-border);
}

.back-btn {
  position: absolute;
  left: 16rpx;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 72rpx;
  height: 72rpx;
  border-radius: 999rpx;
  background: rgba(0, 0, 0, 0.04);
}

.back-icon {
  font-size: 48rpx;
  color: var(--color-text);
  line-height: 1;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
}

.header-spacer {
  width: 72rpx;
}

.content {
  height: calc(100vh - 88rpx);
  padding: 0 32rpx;
}

.bottom-spacer {
  height: 48rpx;
}

/* 状态大卡片 */
.status-card {
  margin-top: 32rpx;
  padding: 40rpx;
  border-radius: 24rpx;
  color: #ffffff;
}

.status-confirmed {
  background: linear-gradient(135deg, var(--color-success), #8fa698);
}

.status-pending {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.status-completed {
  background: linear-gradient(135deg, var(--color-primary), #c4a85a);
}

.status-cancelled {
  background: linear-gradient(135deg, var(--color-text-secondary), #7a756e);
}

.status-top {
  display: flex;
  align-items: center;
  gap: 24rpx;
}

.status-icon-wrap {
  width: 80rpx;
  height: 80rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.status-emoji {
  font-size: 36rpx;
}

.status-info {
  display: flex;
  flex-direction: column;
}

.status-title {
  font-size: 32rpx;
  font-weight: 700;
  color: #ffffff;
}

.status-desc {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
}

.booking-id-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 32rpx;
  padding-top: 24rpx;
  border-top: 1rpx solid rgba(255, 255, 255, 0.2);
}

.booking-id-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
}

.booking-id-value {
  font-size: 24rpx;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.9);
}

/* 通用信息卡片 */
.info-card {
  margin-top: 24rpx;
  padding: 32rpx;
  border-radius: 24rpx;
  background: var(--color-card);
  border: 1rpx solid var(--color-border);
}

.card-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 24rpx;
}

/* 预约项目信息 */
.service-row {
  display: flex;
  gap: 24rpx;
}

.service-image {
  width: 128rpx;
  height: 128rpx;
  border-radius: 20rpx;
  flex-shrink: 0;
}

.service-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.service-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.service-spec {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 8rpx;
}

.service-price-row {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
  margin-top: 16rpx;
}

.service-price {
  font-size: 32rpx;
  font-weight: 700;
  color: var(--color-text);
}

.service-duration {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

/* 预约时间 */
.time-row {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.time-icon-wrap {
  width: 64rpx;
  height: 64rpx;
  border-radius: 16rpx;
  background: rgba(219, 201, 138, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.time-emoji {
  font-size: 28rpx;
}

.time-info {
  display: flex;
  flex-direction: column;
}

.time-date {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.time-slot {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}

/* 详情行 */
.detail-rows {
  display: flex;
  flex-direction: column;
}

.detail-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
}

.detail-label {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}

.detail-value {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}

.detail-divider {
  height: 1rpx;
  background: var(--color-border);
}

/* 门店信息 */
.store-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.store-info {
  flex: 1;
  min-width: 0;
}

.store-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.store-address {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 8rpx;
}

.store-actions {
  display: flex;
  gap: 16rpx;
  flex-shrink: 0;
  margin-left: 24rpx;
}

.store-action-btn {
  width: 72rpx;
  height: 72rpx;
  border-radius: 999rpx;
  background: rgba(0, 0, 0, 0.04);
  display: flex;
  align-items: center;
  justify-content: center;
}

.store-action-icon {
  font-size: 28rpx;
}

/* 费用明细 */
.fee-rows {
  display: flex;
  flex-direction: column;
}

.fee-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 0;
}

.fee-label {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}

.fee-label-discount {
  color: var(--color-success);
}

.fee-value {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}

.fee-value-discount {
  color: var(--color-success);
}

.fee-divider {
  height: 1rpx;
  background: var(--color-border);
  margin: 8rpx 0;
}

.fee-total {
  padding: 24rpx 0 8rpx;
}

.fee-label-total {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.fee-value-total {
  font-size: 36rpx;
  font-weight: 700;
  color: var(--color-text);
}

/* 操作按钮 */
.action-section {
  margin-top: 32rpx;
  display: flex;
  gap: 24rpx;
}

.action-btn {
  flex: 1;
  height: 88rpx;
  border-radius: 999rpx;
  font-size: 28rpx;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn-primary {
  background: var(--color-primary);
  color: #ffffff;
  border: none;
}

.action-btn-primary::after {
  border: none;
}

.action-btn-outline {
  background: transparent;
  color: var(--color-text);
  border: 2rpx solid var(--color-border);
}

.action-btn-outline::after {
  border: none;
}

/* 温馨提示 */
.tip-box {
  display: flex;
  align-items: flex-start;
  gap: 16rpx;
  margin-top: 32rpx;
  margin-bottom: 32rpx;
  padding: 24rpx;
  border-radius: 20rpx;
  background: rgba(0, 0, 0, 0.04);
}

.tip-icon {
  font-size: 28rpx;
  flex-shrink: 0;
  margin-top: 4rpx;
}

.tip-text {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  line-height: 1.6;
}
</style>
