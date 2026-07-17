<template>
  <view class="flash-sale-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
      <view class="nav-btn" @click="goBack">
        <text class="nav-back">‹</text>
      </view>
      <text class="nav-title">限时抢购</text>
      <view class="nav-btn" />
    </view>

    <!-- 限时抢购状态横幅 -->
    <view class="flash-header">
      <view class="flash-banner">
        <text class="flash-icon">⚡</text>
        <text class="flash-label">限时抢购进行中</text>
        <view class="countdown">
          <view class="countdown-block">
            <text class="countdown-num">{{ hours }}</text>
          </view>
          <text class="countdown-sep">:</text>
          <view class="countdown-block">
            <text class="countdown-num">{{ minutes }}</text>
          </view>
          <text class="countdown-sep">:</text>
          <view class="countdown-block">
            <text class="countdown-num">{{ seconds }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 商品图片轮播 -->
    <swiper
      class="product-swiper"
      :indicator-dots="true"
      :autoplay="false"
      indicator-color="rgba(0,0,0,0.2)"
      indicator-active-color="var(--color-primary)"
    >
      <swiper-item v-for="(img, index) in productImages" :key="index">
        <view class="swiper-img" :style="{ background: img.bg }" />
      </swiper-item>
    </swiper>

    <!-- 商品信息 -->
    <view class="product-info">
      <view class="price-row">
        <view class="sale-price">
          <text class="price-unit">$</text>
          <text class="price-value">29</text>
          <text class="price-decimal">.90</text>
        </view>
        <view class="original-price">
          <text class="original-value">$45.00</text>
          <view class="discount-badge">
            <text class="discount-text">3.4折</text>
          </view>
        </view>
      </view>
      <text class="product-name">MOYUYO 轻奢宠物窝 · 冬季加厚保暖款</text>
      <text class="product-desc">高密度回弹海绵填充 · 可拆洗外套 · 防滑底垫 · 三色可选</text>
      <!-- 标签 -->
      <view class="tag-row">
        <view class="tag-item">
          <text class="tag-text">⚡ 限时</text>
        </view>
        <view class="tag-item">
          <text class="tag-text">🏷️ 折扣</text>
        </view>
        <view class="tag-item">
          <text class="tag-text">🚚 包邮</text>
        </view>
      </view>
    </view>

    <!-- 销售进度 -->
    <view class="progress-card">
      <view class="progress-header">
        <text class="progress-label">🔥 已抢 78%</text>
        <text class="progress-stock">仅剩 22 件</text>
      </view>
      <view class="progress-track">
        <view class="progress-fill" :style="{ width: '78%' }" />
      </view>
    </view>

    <!-- 规格选择 -->
    <view class="spec-section" @click="showSpecPicker = true">
      <view class="spec-left">
        <text class="spec-label">规格</text>
        <text class="spec-value">请选择 颜色 · 尺寸</text>
      </view>
      <text class="spec-arrow">›</text>
    </view>

    <!-- 服务保障 -->
    <view class="service-section">
      <view class="service-row">
        <view v-for="item in services" :key="item.label" class="service-item">
          <text class="service-icon">{{ item.icon }}</text>
          <text class="service-label">{{ item.label }}</text>
        </view>
      </view>
    </view>

    <!-- 限量提示 -->
    <view class="limit-hint">
      <text class="limit-icon">⚠️</text>
      <text class="limit-text">每人限购 2 件，会员可享额外 1 件</text>
    </view>

    <!-- 商品详情 -->
    <view class="detail-section">
      <text class="detail-title">商品详情</text>
      <view class="detail-content">
        <view
          class="detail-img"
          :style="{ background: 'linear-gradient(135deg, #e9f9ee, #f7f7fa)' }"
        />
        <view
          class="detail-img"
          :style="{ background: 'linear-gradient(135deg, #f7f7fa, #e8f2ff)' }"
        />
      </view>
    </view>

    <!-- 规格选择弹窗 -->
    <view v-if="showSpecPicker" class="spec-overlay" @click="showSpecPicker = false">
      <view class="spec-sheet" @click.stop>
        <view class="sheet-header">
          <text class="sheet-title">选择规格</text>
          <view class="sheet-close" @click="showSpecPicker = false">
            <text class="close-text">✕</text>
          </view>
        </view>
        <!-- 颜色选择 -->
        <view class="spec-group">
          <text class="spec-group-title">颜色</text>
          <view class="spec-options">
            <view
              v-for="color in colors"
              :key="color"
              class="spec-option"
              :class="{ active: selectedColor === color }"
              @click="selectedColor = color"
            >
              <text class="spec-option-text">{{ color }}</text>
            </view>
          </view>
        </view>
        <!-- 尺寸选择 -->
        <view class="spec-group">
          <text class="spec-group-title">尺寸</text>
          <view class="spec-options">
            <view
              v-for="size in sizes"
              :key="size"
              class="spec-option"
              :class="{ active: selectedSize === size }"
              @click="selectedSize = size"
            >
              <text class="spec-option-text">{{ size }}</text>
            </view>
          </view>
        </view>
        <!-- 购买数量 -->
        <view class="quantity-row">
          <text class="quantity-label">购买数量</text>
          <view class="quantity-control">
            <view class="qty-btn" @click="decreaseQty">
              <text class="qty-btn-text">-</text>
            </view>
            <text class="qty-value">{{ quantity }}</text>
            <view class="qty-btn" @click="increaseQty">
              <text class="qty-btn-text">+</text>
            </view>
          </view>
        </view>
        <!-- 确认按钮 -->
        <view class="sheet-confirm-btn" @click="confirmSpec">
          <text class="sheet-confirm-text">确定</text>
        </view>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <view class="bar-left">
        <view class="bar-icon-btn" @click="toggleFav">
          <text class="bar-icon">{{ isFav ? '❤️' : '🤍' }}</text>
          <text class="bar-icon-label">收藏</text>
        </view>
        <view class="bar-icon-btn" @click="goCart">
          <text class="bar-icon">🛒</text>
          <text class="bar-icon-label">购物车</text>
        </view>
      </view>
      <view class="bar-right">
        <view class="add-cart-btn" @click="addToCart">
          <text class="add-cart-text">加入购物车</text>
        </view>
        <view class="buy-now-btn" @click="buyNow">
          <text class="buy-now-text">⚡ 立即抢购</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

// 倒计时
const hours = ref('02')
const minutes = ref('34')
const seconds = ref('56')
let timer = null

// 商品图片
const productImages = ref([
  { bg: 'linear-gradient(135deg, #f2f2f7, #e8f2ff)' },
  { bg: 'linear-gradient(135deg, #e9f9ee, #f2f2f7)' },
  { bg: 'linear-gradient(135deg, #fff4e5, #f7f7fa)' },
])

// 服务列表
const services = ref([
  { icon: '✅', label: '正品保障' },
  { icon: '🔄', label: '7天退换' },
  { icon: '🚚', label: '极速发货' },
  { icon: '🛡️', label: '运费险' },
])

// 规格选择
const showSpecPicker = ref(false)
const selectedColor = ref('')
const selectedSize = ref('')
const colors = ref(['奶油白', '燕麦灰', '焦糖棕'])
const sizes = ref(['S', 'M', 'L'])
const quantity = ref(1)

// 收藏
const isFav = ref(false)

// 启动倒计时
const startCountdown = () => {
  let totalSeconds = 2 * 3600 + 34 * 60 + 56
  timer = setInterval(() => {
    if (totalSeconds <= 0) {
      clearInterval(timer)
      return
    }
    totalSeconds--
    const h = Math.floor(totalSeconds / 3600)
    const m = Math.floor((totalSeconds % 3600) / 60)
    const s = totalSeconds % 60
    hours.value = String(h).padStart(2, '0')
    minutes.value = String(m).padStart(2, '0')
    seconds.value = String(s).padStart(2, '0')
  }, 1000)
}

onMounted(() => {
  startCountdown()
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})

// 返回
const goBack = () => {
  uni.navigateBack()
}

// 数量操作
const decreaseQty = () => {
  if (quantity.value > 1) quantity.value--
}
const increaseQty = () => {
  if (quantity.value < 2) quantity.value++
}

// 确认规格
const confirmSpec = () => {
  showSpecPicker.value = false
}

// 收藏
const toggleFav = () => {
  isFav.value = !isFav.value
  uni.showToast({ title: isFav.value ? '已收藏' : '已取消收藏', icon: 'none' })
}

// 购物车
const goCart = () => {
  uni.navigateTo({ url: '/pages/user/cart' })
}

// 加入购物车
const addToCart = () => {
  uni.showToast({ title: '已加入购物车', icon: 'success' })
}

// 立即购买
const buyNow = () => {
  uni.navigateTo({ url: '/pages/goods/checkout' })
}
</script>

<style lang="scss" scoped>
.flash-sale-page {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 140rpx;
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

// 限时横幅
.flash-header {
  padding: 24rpx 24rpx 0;
}
.flash-banner {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 20rpx 28rpx;
  border-radius: 20rpx;
  background: var(--color-error);
}
.flash-icon {
  font-size: 32rpx;
}
.flash-label {
  font-size: 28rpx;
  font-weight: 600;
  color: #fff;
}
.countdown {
  display: flex;
  align-items: center;
  gap: 6rpx;
  margin-left: auto;
}
.countdown-block {
  min-width: 44rpx;
  height: 44rpx;
  border-radius: 8rpx;
  background: rgba(255, 255, 255, 0.25);
  display: flex;
  align-items: center;
  justify-content: center;
}
.countdown-num {
  font-size: 26rpx;
  font-weight: 700;
  color: #fff;
  font-family: monospace;
}
.countdown-sep {
  font-size: 26rpx;
  font-weight: 700;
  color: #fff;
}

// 商品轮播
.product-swiper {
  height: 600rpx;
  margin: 0 24rpx;
  border-radius: 24rpx;
  overflow: hidden;
}
.swiper-img {
  width: 100%;
  height: 100%;
}

// 商品信息
.product-info {
  padding: 28rpx 24rpx 0;
}
.price-row {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
}
.sale-price {
  display: flex;
  align-items: baseline;
}
.price-unit {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-error);
}
.price-value {
  font-size: 56rpx;
  font-weight: 800;
  color: var(--color-error);
  line-height: 1;
}
.price-decimal {
  font-size: 28rpx;
  font-weight: 600;
  color: var(--color-error);
}
.original-price {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.original-value {
  font-size: 28rpx;
  text-decoration: line-through;
  color: var(--color-text-secondary);
}
.discount-badge {
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  background: #ffecea;
}
.discount-text {
  font-size: 22rpx;
  font-weight: 700;
  color: var(--color-error);
}
.product-name {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
  margin-top: 16rpx;
}
.product-desc {
  font-size: 26rpx;
  color: var(--color-text-secondary);
  margin-top: 8rpx;
}
.tag-row {
  display: flex;
  gap: 12rpx;
  margin-top: 16rpx;
}
.tag-item {
  padding: 6rpx 16rpx;
  border-radius: 999rpx;
  background: #fff4e5;
}
.tag-text {
  font-size: 22rpx;
  font-weight: 500;
  color: #e08600;
}

// 销售进度
.progress-card {
  margin: 24rpx 24rpx 0;
  padding: 20rpx 24rpx;
  border-radius: 16rpx;
  background: var(--color-card);
  border: 1rpx solid var(--color-border);
}
.progress-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}
.progress-label {
  font-size: 26rpx;
  font-weight: 500;
  color: var(--color-error);
}
.progress-stock {
  font-size: 26rpx;
  color: var(--color-text-secondary);
}
.progress-track {
  height: 12rpx;
  border-radius: 999rpx;
  background: var(--color-background);
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  border-radius: 999rpx;
  background: linear-gradient(90deg, #ff6b6b, var(--color-error));
  transition: width 0.4s ease;
}

// 规格选择
.spec-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 24rpx 24rpx 0;
  padding: 24rpx;
  background: var(--color-card);
  border-radius: 16rpx;
  border: 1rpx solid var(--color-border);
}
.spec-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.spec-label {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}
.spec-value {
  font-size: 26rpx;
  color: var(--color-text-secondary);
}
.spec-arrow {
  font-size: 32rpx;
  color: var(--color-text-secondary);
}

// 服务保障
.service-section {
  margin: 24rpx 24rpx 0;
  padding: 20rpx 24rpx;
  background: var(--color-card);
  border-radius: 16rpx;
  border: 1rpx solid var(--color-border);
}
.service-row {
  display: flex;
  justify-content: space-around;
}
.service-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}
.service-icon {
  font-size: 24rpx;
}
.service-label {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

// 限量提示
.limit-hint {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 24rpx 24rpx 0;
}
.limit-icon {
  font-size: 26rpx;
}
.limit-text {
  font-size: 24rpx;
  color: #e08600;
}

// 商品详情
.detail-section {
  padding: 32rpx 24rpx;
}
.detail-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 20rpx;
}
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}
.detail-img {
  width: 100%;
  aspect-ratio: 16/9;
  border-radius: 16rpx;
}

// 规格选择弹窗
.spec-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 50;
  display: flex;
  align-items: flex-end;
}
.spec-sheet {
  width: 100%;
  background: var(--color-card);
  border-radius: 32rpx 32rpx 0 0;
  padding: 32rpx;
  padding-bottom: calc(32rpx + env(safe-area-inset-bottom));
}
.sheet-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32rpx;
}
.sheet-title {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
}
.sheet-close {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
  background: var(--color-background);
  display: flex;
  align-items: center;
  justify-content: center;
}
.close-text {
  font-size: 28rpx;
  color: var(--color-text-secondary);
}
.spec-group {
  margin-bottom: 28rpx;
}
.spec-group-title {
  font-size: 26rpx;
  font-weight: 500;
  color: var(--color-text-secondary);
  margin-bottom: 16rpx;
}
.spec-options {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}
.spec-option {
  padding: 12rpx 28rpx;
  border-radius: 999rpx;
  border: 2rpx solid var(--color-border);
  background: var(--color-card);
}
.spec-option.active {
  border-color: var(--color-primary);
  background: #e8f2ff;
}
.spec-option-text {
  font-size: 26rpx;
  color: var(--color-text);
}
.spec-option.active .spec-option-text {
  color: var(--color-primary);
  font-weight: 600;
}

// 购买数量
.quantity-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32rpx;
}
.quantity-label {
  font-size: 28rpx;
  font-weight: 500;
  color: var(--color-text);
}
.quantity-control {
  display: flex;
  align-items: center;
  gap: 24rpx;
}
.qty-btn {
  width: 56rpx;
  height: 56rpx;
  border-radius: 16rpx;
  background: var(--color-background);
  display: flex;
  align-items: center;
  justify-content: center;
}
.qty-btn-text {
  font-size: 32rpx;
  color: var(--color-text);
}
.qty-value {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--color-text);
  min-width: 40rpx;
  text-align: center;
}

// 确认按钮
.sheet-confirm-btn {
  height: 88rpx;
  border-radius: 24rpx;
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}
.sheet-confirm-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #fff;
}

// 底部操作栏
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  padding-bottom: calc(16rpx + env(safe-area-inset-bottom));
  background: var(--color-card);
  border-top: 1rpx solid var(--color-border);
  gap: 16rpx;
}
.bar-left {
  display: flex;
  gap: 24rpx;
}
.bar-icon-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4rpx;
}
.bar-icon {
  font-size: 36rpx;
}
.bar-icon-label {
  font-size: 20rpx;
  color: var(--color-text-secondary);
}
.bar-right {
  flex: 1;
  display: flex;
  gap: 12rpx;
}
.add-cart-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 20rpx;
  background: var(--color-card);
  border: 2rpx solid var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
}
.add-cart-text {
  font-size: 26rpx;
  font-weight: 600;
  color: var(--color-primary);
}
.buy-now-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 20rpx;
  background: var(--color-error);
  display: flex;
  align-items: center;
  justify-content: center;
}
.buy-now-text {
  font-size: 26rpx;
  font-weight: 600;
  color: #fff;
}
</style>
