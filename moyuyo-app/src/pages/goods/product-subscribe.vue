<template>
  <view class="product-subscribe">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">订阅此商品</text>
      <view class="header-spacer" />
    </view>

    <scroll-view class="content" scroll-y>
      <!-- 商品信息卡片 -->
      <view class="product-card">
        <image :src="product.image" class="product-image" mode="aspectFill" />
        <view class="product-info">
          <text class="product-name">{{ product.name }}</text>
          <text class="product-desc">{{ product.desc }}</text>
          <view class="product-price-row">
            <text class="product-price">¥{{ product.price }}</text>
            <text class="product-original-price">¥{{ product.originalPrice }}</text>
          </view>
          <!-- 订阅优惠标签 -->
          <view class="sub-save-tag">
            <text class="sub-save-emoji">✨</text>
            <text class="sub-save-text">
              订阅可省 {{ product.savePercent }}%，低至 ¥{{ product.subPrice }}/期
            </text>
          </view>
        </view>
      </view>

      <!-- 规格选择 -->
      <view class="spec-section">
        <text class="section-title">选择规格</text>
        <view class="spec-options">
          <view
            v-for="spec in specs"
            :key="spec.key"
            class="spec-option"
            :class="{ selected: selectedSpec === spec.key }"
            @click="selectedSpec = spec.key"
          >
            <text class="spec-name" :class="{ 'spec-name-selected': selectedSpec === spec.key }">
              {{ spec.name }}
            </text>
            <text class="spec-price" :class="{ 'spec-price-selected': selectedSpec === spec.key }">
              ¥{{ spec.subPrice }}
            </text>
          </view>
        </view>
      </view>

      <!-- 订阅周期选择 -->
      <view class="cycle-section">
        <view class="section-title-row">
          <text class="section-title">配送周期</text>
          <text class="section-hint">可随时调整</text>
        </view>
        <view class="cycle-options">
          <view
            v-for="cycle in cycles"
            :key="cycle.key"
            class="cycle-card"
            :class="{ selected: selectedCycle === cycle.key }"
            @click="selectedCycle = cycle.key"
          >
            <view class="cycle-radio">
              <view v-if="selectedCycle === cycle.key" class="cycle-radio-dot" />
            </view>
            <view class="cycle-info">
              <view class="cycle-name-row">
                <text class="cycle-name">{{ cycle.name }}</text>
                <text v-if="cycle.recommend" class="cycle-recommend">推荐</text>
              </view>
              <text class="cycle-desc">{{ cycle.desc }}</text>
            </view>
            <text class="cycle-save">省{{ cycle.savePercent }}%</text>
          </view>
        </view>
      </view>

      <!-- 首次配送信息 -->
      <view class="delivery-section">
        <text class="section-title">配送信息</text>
        <view class="delivery-card">
          <view class="delivery-row" @click="selectDate">
            <view class="delivery-left">
              <text class="delivery-icon">📅</text>
              <text class="delivery-label">首次配送</text>
            </view>
            <view class="delivery-right">
              <text class="delivery-value">{{ firstDeliveryDate }}</text>
              <text class="delivery-arrow">›</text>
            </view>
          </view>
          <view class="delivery-divider" />
          <view class="delivery-row" @click="selectAddress">
            <view class="delivery-left">
              <text class="delivery-icon">📍</text>
              <text class="delivery-label">配送地址</text>
            </view>
            <view class="delivery-right">
              <text class="delivery-value">{{ deliveryAddress }}</text>
              <text class="delivery-arrow">›</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 订阅权益 -->
      <view class="benefits-section">
        <text class="section-title">订阅权益</text>
        <view class="benefits-list">
          <view v-for="(b, i) in benefits" :key="i" class="benefit-item">
            <text class="benefit-check">✓</text>
            <text class="benefit-text">{{ b }}</text>
          </view>
        </view>
      </view>

      <!-- 底部占位 -->
      <view class="bottom-spacer" />
    </scroll-view>

    <!-- 底部固定 CTA -->
    <view class="bottom-bar">
      <view class="bottom-info">
        <view class="bottom-price-row">
          <text class="bottom-label">订阅价</text>
          <text class="bottom-price">¥{{ currentSubPrice }}</text>
        </view>
        <text class="bottom-save">较单次购买省 ¥{{ currentSaveAmount }}</text>
      </view>
      <button class="subscribe-btn" @click="onSubscribe">立即订阅</button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

// 商品信息 mock
const product = ref({
  name: 'MOYUYO 温和沐浴露',
  desc: '宠物专用温和清洁配方，呵护爱宠肌肤',
  price: '28.00',
  originalPrice: '32.00',
  subPrice: '25.20',
  savePercent: 10,
  image: '/static/images/product-shampoo.png',
})

// 规格选择
const selectedSpec = ref('500ml')
const specs = ref([
  { key: '500ml', name: '500ml', subPrice: '25.20' },
  { key: '1L', name: '1L', subPrice: '42.30' },
  { key: '2L', name: '2L', subPrice: '68.40' },
])

// 配送周期选择
const selectedCycle = ref('monthly')
const cycles = ref([
  { key: 'monthly', name: '每月配送', desc: '适合日常消耗', savePercent: 10, recommend: false },
  { key: 'bimonthly', name: '双月配送', desc: '适合中频使用', savePercent: 10, recommend: true },
  { key: 'quarterly', name: '季度配送', desc: '适合囤货党', savePercent: 15, recommend: false },
])

// 配送信息
const firstDeliveryDate = ref('2026-07-20')
const deliveryAddress = ref('默认地址')

// 订阅权益
const benefits = [
  '自动享 10% off 优惠',
  '满额免运费',
  '随时修改周期或暂停',
  '续订前 7 天提醒',
  '可随时取消，无违约金',
]

// 当前订阅价
const currentSubPrice = computed(() => {
  const spec = specs.value.find((s) => s.key === selectedSpec.value)
  return spec ? spec.subPrice : '25.20'
})

// 当前节省金额
const currentSaveAmount = computed(() => {
  const spec = specs.value.find((s) => s.key === selectedSpec.value)
  if (!spec) return '2.80'
  return (parseFloat(product.value.price) - parseFloat(spec.subPrice)).toFixed(2)
})

// 返回
const goBack = () => {
  uni.navigateBack()
}

// 选择配送日期
const selectDate = () => {
  uni.showToast({ title: '选择配送日期', icon: 'none' })
}

// 选择配送地址
const selectAddress = () => {
  uni.navigateTo({ url: '/pages/user/address' })
}

// 立即订阅
const onSubscribe = () => {
  uni.showModal({
    title: '确认订阅',
    content: `将以 ¥${currentSubPrice.value}/期 订阅此商品，可随时取消。`,
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '订阅成功！', icon: 'success' })
      }
    },
  })
}
</script>

<style lang="scss" scoped>
.product-subscribe {
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
  height: 280rpx;
}

/* 商品信息卡片 */
.product-card {
  display: flex;
  gap: 24rpx;
  margin-top: 32rpx;
  padding: 28rpx;
  border-radius: 24rpx;
  background: var(--color-card);
  border: 1rpx solid var(--color-border);
}

.product-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 20rpx;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.product-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 8rpx;
}

.product-price-row {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
  margin-top: 16rpx;
}

.product-price {
  font-size: 32rpx;
  font-weight: 700;
  color: var(--color-text);
}

.product-original-price {
  font-size: 24rpx;
  text-decoration: line-through;
  color: var(--color-text-secondary);
}

.sub-save-tag {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 16rpx;
  padding: 8rpx 16rpx;
  border-radius: 12rpx;
  background: rgba(219, 201, 138, 0.15);
  align-self: flex-start;
}

.sub-save-emoji {
  font-size: 24rpx;
}

.sub-save-text {
  font-size: 22rpx;
  font-weight: 600;
  color: var(--color-primary);
}

/* 区域标题 */
.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.section-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-hint {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

/* 规格选择 */
.spec-section {
  margin-top: 40rpx;
}

.spec-options {
  display: flex;
  gap: 16rpx;
  margin-top: 20rpx;
  flex-wrap: wrap;
}

.spec-option {
  padding: 16rpx 32rpx;
  border-radius: 16rpx;
  background: var(--color-card);
  border: 2rpx solid var(--color-border);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.spec-option.selected {
  border-color: var(--color-primary);
}

.spec-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text-secondary);
}

.spec-name-selected {
  color: var(--color-text);
}

.spec-price {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

.spec-price-selected {
  color: var(--color-primary);
  font-weight: 700;
}

/* 配送周期选择 */
.cycle-section {
  margin-top: 40rpx;
}

.cycle-options {
  margin-top: 20rpx;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.cycle-card {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 28rpx;
  border-radius: 20rpx;
  background: var(--color-card);
  border: 2rpx solid var(--color-border);
}

.cycle-card.selected {
  border-color: var(--color-primary);
}

.cycle-radio {
  width: 36rpx;
  height: 36rpx;
  border-radius: 999rpx;
  border: 3rpx solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.cycle-card.selected .cycle-radio {
  border-color: var(--color-primary);
}

.cycle-radio-dot {
  width: 18rpx;
  height: 18rpx;
  border-radius: 999rpx;
  background: var(--color-primary);
}

.cycle-info {
  flex: 1;
  min-width: 0;
}

.cycle-name-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.cycle-name {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.cycle-recommend {
  padding: 2rpx 12rpx;
  border-radius: 999rpx;
  background: var(--color-primary);
  color: #ffffff;
  font-size: 20rpx;
  font-weight: 600;
}

.cycle-desc {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  margin-top: 4rpx;
}

.cycle-save {
  font-size: 24rpx;
  font-weight: 700;
  color: var(--color-primary);
  flex-shrink: 0;
}

/* 配送信息 */
.delivery-section {
  margin-top: 40rpx;
}

.delivery-card {
  margin-top: 20rpx;
  padding: 32rpx;
  border-radius: 24rpx;
  background: var(--color-card);
  border: 1rpx solid var(--color-border);
}

.delivery-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.delivery-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.delivery-icon {
  font-size: 28rpx;
}

.delivery-label {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}

.delivery-right {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.delivery-value {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-text);
}

.delivery-arrow {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}

.delivery-divider {
  height: 1rpx;
  background: var(--color-border);
  margin: 24rpx 0;
}

/* 订阅权益 */
.benefits-section {
  margin-top: 40rpx;
}

.benefits-list {
  margin-top: 20rpx;
  padding: 28rpx;
  border-radius: 24rpx;
  background: var(--color-card);
  border: 1rpx solid var(--color-border);
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.benefit-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.benefit-check {
  font-size: 24rpx;
  color: var(--color-success);
  font-weight: 700;
}

.benefit-text {
  font-size: 26rpx;
  color: var(--color-text);
}

/* 底部固定栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 40;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 32rpx;
  padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px);
  border-top: 1rpx solid var(--color-border);
}

.bottom-info {
  display: flex;
  flex-direction: column;
}

.bottom-price-row {
  display: flex;
  align-items: baseline;
  gap: 12rpx;
}

.bottom-label {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

.bottom-price {
  font-size: 36rpx;
  font-weight: 700;
  color: var(--color-primary);
}

.bottom-save {
  font-size: 22rpx;
  color: var(--color-success);
  margin-top: 4rpx;
}

.subscribe-btn {
  height: 88rpx;
  padding: 0 64rpx;
  border-radius: 999rpx;
  background: var(--color-primary);
  color: #ffffff;
  font-size: 28rpx;
  font-weight: 700;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.subscribe-btn::after {
  border: none;
}
</style>
