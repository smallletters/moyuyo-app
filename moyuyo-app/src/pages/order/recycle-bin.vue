<template>
  <view class="recycle-bin">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="header-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="header-title">订单回收站</text>
      <view class="header-btn" @click="onToggleManage">
        <text class="manage-text">{{ isManage ? '完成' : '管理' }}</text>
      </view>
    </view>

    <!-- 说明卡片 -->
    <view class="info-card">
      <view class="info-card-header">
        <text class="info-icon">ℹ️</text>
        <text class="info-text">
          删除的订单将在回收站保留
          <text class="info-highlight">30 天</text>
          ，超期后将被彻底清除且无法恢复。
        </text>
      </view>
      <view class="stats-row">
        <view class="stats-item">
          <text class="stats-label">回收订单</text>
          <text class="stats-value">
            {{ orderList.length }}
            <text class="stats-unit">个</text>
          </text>
        </view>
        <view class="stats-divider" />
        <view class="stats-item">
          <text class="stats-label">最近可恢复</text>
          <text class="stats-value stats-value--warn">
            12
            <text class="stats-unit">天</text>
          </text>
        </view>
        <view class="stats-divider" />
        <view class="stats-item">
          <text class="stats-label">即将过期</text>
          <text class="stats-value stats-value--error">
            {{ expiringCount }}
            <text class="stats-unit">个</text>
          </text>
        </view>
      </view>
    </view>

    <!-- 订单列表 -->
    <view v-if="orderList.length > 0" class="order-list">
      <view
        v-for="order in orderList"
        :key="order.id"
        class="order-card"
        :class="{ selected: manageSelected.includes(order.id) }"
        :style="{ opacity: order.grayed ? 0.85 : 1 }"
      >
        <view class="order-header">
          <view
            v-if="isManage"
            class="order-checkbox"
            :class="{ checked: manageSelected.includes(order.id) }"
            @click="onToggleSelect(order.id)"
          />
          <view class="order-meta">
            <text class="order-no">{{ order.orderNo }}</text>
            <text class="order-delete-time">删除于 {{ order.deleteTime }}</text>
          </view>
          <view class="countdown-badge" :class="{ urgent: order.daysLeft <= 3 }">
            <text class="countdown-icon">⏰</text>
            <text>剩余 {{ order.daysLeft }} 天</text>
          </view>
        </view>

        <view class="order-body">
          <view class="order-products">
            <view class="product-thumb-list">
              <view
                v-for="(product, idx) in order.products.slice(0, 3)"
                :key="idx"
                class="product-thumb"
              >
                <text class="thumb-placeholder">{{ product.icon }}</text>
              </view>
              <view v-if="order.products.length > 3" class="product-thumb product-thumb-more">
                <text>+{{ order.products.length - 3 }}</text>
              </view>
            </view>
            <view class="product-info">
              <text class="product-name">
                {{ order.products[0].name }}{{ order.products.length > 1 ? ' 等' : '' }}
              </text>
              <text class="product-count">共 {{ order.products.length }} 件商品</text>
            </view>
          </view>

          <view class="order-footer">
            <text class="order-total">¥{{ order.total }}</text>
            <text class="order-status" :class="'status-' + order.status">
              {{ order.statusLabel }}
            </text>
          </view>

          <view v-if="!isManage" class="order-actions">
            <view class="action-btn action-btn--restore" @click="onRestore(order.id)">
              <text class="action-icon">↩️</text>
              <text>恢复订单</text>
            </view>
            <view class="action-btn action-btn--delete" @click="onPermanentDelete(order.id)">
              <text class="action-icon">🗑️</text>
              <text>永久删除</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-else class="empty-state">
      <view class="empty-icon-circle">
        <text class="empty-icon-text">🗑️</text>
      </view>
      <text class="empty-title">回收站为空</text>
      <text class="empty-desc">删除的订单会在这里保留 30 天，期间可随时恢复。</text>
      <view class="empty-action" @click="goOrderList">
        <text class="empty-action-icon">📦</text>
        <text>查看我的订单</text>
      </view>
    </view>

    <!-- 批量操作栏 -->
    <view v-if="isManage" class="batch-bar">
      <view class="batch-bar-content">
        <view
          class="order-checkbox"
          :class="{ checked: manageSelected.length === orderList.length && orderList.length > 0 }"
          @click="onToggleSelectAll"
        />
        <text class="batch-selected">已选 {{ manageSelected.length }} 个</text>
        <view class="batch-actions">
          <view class="batch-btn batch-btn--restore" @click="onBatchRestore">
            <text class="action-icon">↩️</text>
            <text>批量恢复</text>
          </view>
          <view class="batch-btn batch-btn--delete" @click="onBatchDelete">
            <text class="action-icon">🗑️</text>
            <text>批量删除</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { recycleBinApi } from '@/api'

export default {
  data() {
    return {
      isManage: false,
      manageSelected: [],
      orderList: [],
    }
  },

  computed: {
    expiringCount() {
      return this.orderList.filter((o) => o.daysLeft <= 3).length
    },
  },

  onLoad() {
    this.loadRecycleBinOrders()
  },

  methods: {
    goBack() {
      uni.navigateBack()
    },

    goOrderList() {
      uni.navigateTo({ url: '/pages/order/list' })
    },

    async loadRecycleBinOrders() {
      try {
        const res = await recycleBinApi.getRecycleBinOrders()
        this.orderList = res.data || []
      } catch (err) {
        uni.showToast({ title: '加载回收站订单失败', icon: 'none' })
      }
    },

    onToggleManage() {
      this.isManage = !this.isManage
      if (!this.isManage) {
        this.manageSelected = []
      }
    },

    onToggleSelect(orderId) {
      const idx = this.manageSelected.indexOf(orderId)
      if (idx > -1) {
        this.manageSelected.splice(idx, 1)
      } else {
        this.manageSelected.push(orderId)
      }
    },

    onToggleSelectAll() {
      if (this.manageSelected.length === this.orderList.length) {
        this.manageSelected = []
      } else {
        this.manageSelected = this.orderList.map((o) => o.id)
      }
    },

    onRestore(orderId) {
      const order = this.orderList.find((o) => o.id === orderId)
      uni.showModal({
        title: '恢复订单',
        content: `确定恢复订单 ${order.orderNo} 吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              await recycleBinApi.restoreOrder(orderId)
              this.orderList = this.orderList.filter((o) => o.id !== orderId)
              uni.showToast({ title: '已恢复', icon: 'success' })
            } catch (err) {
              uni.showToast({ title: '恢复失败', icon: 'none' })
            }
          }
        },
      })
    },

    onPermanentDelete(orderId) {
      const order = this.orderList.find((o) => o.id === orderId)
      uni.showModal({
        title: '永久删除',
        content: `确定永久删除订单 ${order.orderNo}？此操作不可撤销。`,
        success: async (res) => {
          if (res.confirm) {
            try {
              await recycleBinApi.deleteOrderPermanently(orderId)
              this.orderList = this.orderList.filter((o) => o.id !== orderId)
              uni.showToast({ title: '已永久删除', icon: 'success' })
            } catch (err) {
              uni.showToast({ title: '删除失败', icon: 'none' })
            }
          }
        },
      })
    },

    onBatchRestore() {
      if (this.manageSelected.length === 0) {
        uni.showToast({ title: '请选择订单', icon: 'none' })
        return
      }
      uni.showModal({
        title: '批量恢复',
        content: `确定恢复选中的 ${this.manageSelected.length} 个订单吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              await recycleBinApi.batchRestoreOrders(this.manageSelected)
              this.orderList = this.orderList.filter((o) => !this.manageSelected.includes(o.id))
              this.manageSelected = []
              uni.showToast({ title: '已批量恢复', icon: 'success' })
            } catch (err) {
              uni.showToast({ title: '批量恢复失败', icon: 'none' })
            }
          }
        },
      })
    },

    onBatchDelete() {
      if (this.manageSelected.length === 0) {
        uni.showToast({ title: '请选择订单', icon: 'none' })
        return
      }
      uni.showModal({
        title: '批量删除',
        content: `确定永久删除选中的 ${this.manageSelected.length} 个订单？此操作不可撤销。`,
        success: async (res) => {
          if (res.confirm) {
            try {
              await recycleBinApi.batchDeleteOrders(this.manageSelected)
              this.orderList = this.orderList.filter((o) => !this.manageSelected.includes(o.id))
              this.manageSelected = []
              uni.showToast({ title: '已批量删除', icon: 'success' })
            } catch (err) {
              uni.showToast({ title: '批量删除失败', icon: 'none' })
            }
          }
        },
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.recycle-bin {
  min-height: 100vh;
  background: var(--background-200);
  padding-bottom: 140rpx;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 30rpx;
  background: rgba(255, 255, 255, 0.88);
  border-bottom: 1rpx solid var(--border);
  position: sticky;
  top: 0;
  z-index: 30;
}

.header-btn {
  width: 72rpx;
  height: 72rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 44rpx;
  color: var(--icon-700);
  line-height: 1;
}

.header-title {
  font-size: 34rpx;
  font-weight: 600;
  color: var(--foreground);
}

.manage-text {
  font-size: 26rpx;
  font-weight: 500;
  color: var(--text-500);
  white-space: nowrap;
}

/* ===== 说明卡片 ===== */
.info-card {
  margin: 24rpx 30rpx 0;
  border-radius: var(--radius);
  background: var(--card);
  overflow: hidden;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}

.info-card-header {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 28rpx;
  background: linear-gradient(135deg, var(--brand-50), var(--background-100));
  border-bottom: 1rpx solid var(--border);
}

.info-icon {
  font-size: 32rpx;
  flex-shrink: 0;
}

.info-text {
  font-size: 24rpx;
  color: var(--text-600);
  line-height: 1.5;
}

.info-highlight {
  color: var(--brand-500);
  font-weight: 600;
}

.stats-row {
  display: flex;
  align-items: center;
  gap: 24rpx;
  padding: 24rpx 28rpx;
}

.stats-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.stats-divider {
  width: 2rpx;
  height: 56rpx;
  background: var(--border);
  flex-shrink: 0;
}

.stats-label {
  font-size: 20rpx;
  font-weight: 500;
  color: var(--text-400);
}

.stats-value {
  font-size: 36rpx;
  font-weight: 700;
  color: var(--foreground);
  line-height: 1.2;
}

.stats-value--warn {
  color: var(--state-warning);
}

.stats-value--error {
  color: var(--state-error);
}

.stats-unit {
  font-size: 24rpx;
  font-weight: 400;
  color: var(--text-400);
}

/* ===== 订单列表 ===== */
.order-list {
  margin: 24rpx 30rpx 0;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.order-card {
  border-radius: var(--radius);
  background: var(--card);
  overflow: hidden;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.08);
}

.order-card.selected {
  outline: 4rpx solid var(--primary);
  outline-offset: -4rpx;
}

.order-header {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 24rpx 28rpx 0;
}

.order-checkbox {
  width: 36rpx;
  height: 36rpx;
  border-radius: 50%;
  border: 4rpx solid var(--background-400);
  background: transparent;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.order-checkbox.checked {
  background: var(--primary);
  border-color: var(--primary);
}

.order-checkbox.checked::after {
  content: '';
  display: block;
  width: 12rpx;
  height: 20rpx;
  border: 4rpx solid var(--background-50);
  border-top: 0;
  border-left: 0;
  transform: rotate(45deg) translateY(-2rpx);
}

.order-meta {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.order-no {
  font-size: 24rpx;
  font-weight: 500;
  color: var(--foreground);
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-delete-time {
  font-size: 20rpx;
  color: var(--text-400);
  display: block;
}

.countdown-badge {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 8rpx 16rpx;
  border-radius: 12rpx;
  font-size: 22rpx;
  font-weight: 600;
  background: var(--background-200);
  color: var(--state-warning);
  flex-shrink: 0;
}

.countdown-badge.urgent {
  background: var(--state-error-surface);
  color: var(--state-error);
}

.countdown-icon {
  font-size: 24rpx;
}

.order-body {
  padding: 16rpx 28rpx 24rpx;
}

.order-products {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-top: 16rpx;
}

.product-thumb-list {
  display: flex;
  flex-shrink: 0;
}

.product-thumb {
  width: 100rpx;
  height: 100rpx;
  border-radius: 16rpx;
  background: linear-gradient(135deg, var(--secondary), var(--accent));
  border: 1rpx solid var(--border);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  font-size: 40rpx;
}

.product-thumb + .product-thumb {
  margin-left: -16rpx;
}

.product-thumb-more {
  background: var(--background-200);
  color: var(--text-500);
  font-size: 22rpx;
  font-weight: 600;
  border: 2rpx dashed var(--background-400);
}

.product-info {
  flex: 1;
  min-width: 0;
}

.product-name {
  font-size: 26rpx;
  font-weight: 500;
  color: var(--foreground);
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-count {
  font-size: 20rpx;
  color: var(--text-400);
  display: block;
  margin-top: 4rpx;
}

.order-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16rpx;
}

.order-total {
  font-size: 30rpx;
  font-weight: 700;
  color: var(--foreground);
}

.order-status {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  padding: 6rpx 16rpx;
  border-radius: 999rpx;
  font-size: 20rpx;
  font-weight: 600;
}

.status-completed {
  background: var(--state-success-surface);
  color: var(--state-success);
}

.status-cancelled {
  background: var(--background-200);
  color: var(--text-500);
}

.status-refunded {
  background: var(--state-error-surface);
  color: var(--state-error);
}

.order-actions {
  display: flex;
  gap: 16rpx;
  margin-top: 20rpx;
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10rpx;
  height: 64rpx;
  border-radius: 16rpx;
  font-size: 24rpx;
  font-weight: 600;
  border: none;
}

.action-btn--restore {
  background: var(--primary);
  color: var(--primary-foreground);
}

.action-btn--delete {
  background: var(--background-200);
  color: var(--state-error);
}

.action-icon {
  font-size: 24rpx;
}

/* ===== 空状态 ===== */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 60rpx 100rpx;
}

.empty-icon-circle {
  width: 200rpx;
  height: 200rpx;
  border-radius: 50%;
  background: var(--background-200);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40rpx;
}

.empty-icon-text {
  font-size: 80rpx;
}

.empty-title {
  font-size: 34rpx;
  font-weight: 700;
  color: var(--text-800);
  margin-bottom: 16rpx;
}

.empty-desc {
  font-size: 26rpx;
  color: var(--text-500);
  text-align: center;
  line-height: 1.5;
  max-width: 440rpx;
}

.empty-action {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  margin-top: 48rpx;
  height: 80rpx;
  padding: 0 56rpx;
  border-radius: 999rpx;
  background: var(--primary);
  color: var(--primary-foreground);
  font-size: 28rpx;
  font-weight: 600;
}

.empty-action-icon {
  font-size: 28rpx;
}

/* ===== 批量操作栏 ===== */
.batch-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 40;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px);
  border-top: 1rpx solid var(--border);
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

.batch-bar-content {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.batch-selected {
  font-size: 24rpx;
  font-weight: 500;
  color: var(--foreground);
  white-space: nowrap;
}

.batch-actions {
  display: flex;
  gap: 16rpx;
  margin-left: auto;
}

.batch-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  height: 64rpx;
  padding: 0 28rpx;
  border-radius: 16rpx;
  font-size: 24rpx;
  font-weight: 600;
  border: none;
}

.batch-btn--restore {
  background: var(--primary);
  color: var(--primary-foreground);
}

.batch-btn--delete {
  background: var(--state-error-surface);
  color: var(--state-error);
}
</style>
