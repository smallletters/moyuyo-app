<template>
  <view v-if="order" class="order-detail">
    <view class="status-banner" :class="`status-${order.status}`">
      <text class="status-title">{{ statusTitle }}</text>
      <text class="status-desc">{{ statusDesc }}</text>
    </view>

    <view class="card address-card">
      <text class="card-title">Shipping Address</text>
      <text class="address-name">
        {{ order.receiverName }} · {{ order.receiverPhone }}
      </text>
      <text class="address-detail">
        {{ order.receiverAddress }}
      </text>
      <text v-if="order.receiverZip" class="address-zip">{{ order.receiverZip }}</text>
    </view>

    <view class="card items-card">
      <text class="card-title">Items</text>
      <view v-for="item in order.items" :key="item.id" class="item-row">
        <image :src="item.mainImage || ''" class="item-image" />
        <view class="item-info">
          <text class="item-name">{{ item.productName }}</text>
          <text v-if="item.skuSpec" class="item-spec">{{ item.skuSpec }}</text>
          <text class="item-qty">x {{ item.quantity }}</text>
        </view>
        <text class="item-price">${{ item.subtotal }}</text>
      </view>
    </view>

    <view class="card price-card">
      <view class="price-row">
        <text>Subtotal</text>
        <text>${{ order.goodsAmount }}</text>
      </view>
      <view class="price-row">
        <text>Shipping</text>
        <text>${{ order.freight }}</text>
      </view>
      <view v-if="order.taxAmount > 0" class="price-row">
        <text>Tax</text>
        <text>${{ order.taxAmount }}</text>
      </view>
      <view v-if="order.couponDiscount > 0" class="price-row">
        <text>Discount</text>
        <text class="discount">-${{ order.couponDiscount }}</text>
      </view>
      <view v-if="order.pointsDiscount > 0" class="price-row">
        <text>Points</text>
        <text class="discount">-${{ order.pointsDiscount }}</text>
      </view>
      <view class="price-row total">
        <text>Total</text>
        <text class="total-amount">${{ order.payAmount }}</text>
      </view>
    </view>

    <view class="card info-card">
      <view class="info-row">
        <text>Order Number</text>
        <text>#{{ order.orderNo }}</text>
      </view>
      <view class="info-row">
        <text>Payment Method</text>
        <text>{{ order.payChannel || '-' }}</text>
      </view>
      <view class="info-row">
        <text>Order Time</text>
        <text>{{ formatDate(order.createTime) }}</text>
      </view>
      <view v-if="order.paidAt" class="info-row">
        <text>Paid At</text>
        <text>{{ formatDate(order.paidAt) }}</text>
      </view>
      <view v-if="order.trackingNumber" class="info-row">
        <text>Tracking</text>
        <text>{{ order.trackingNumber }}</text>
      </view>
    </view>

    <view class="action-bar safe-area-bottom">
      <view v-if="order.status === 'PENDING_PAY'" class="btn btn-outline" @click="onCancel">
        Cancel Order
      </view>
      <view v-if="order.status === 'PENDING_PAY'" class="btn btn-primary" @click="onPay">Pay Now</view>
      <view
        v-if="order.status === 'PENDING_SHIP' || order.status === 'PENDING_RECEIVE'"
        class="btn btn-primary"
        @click="onTrack"
      >
        Track Order
      </view>
      <view v-if="order.status === 'PENDING_RECEIVE'" class="btn btn-outline" @click="onConfirm">
        Confirm Received
      </view>
      <view v-if="order.status === 'COMPLETED'" class="btn btn-primary" @click="onReview">
        Write Review
      </view>
      <view v-if="order.status === 'PAID' || order.status === 'RECEIVED'" class="btn btn-outline" @click="onRefund">
        Refund
      </view>
    </view>
  </view>
  <view v-else class="loading">Loading...</view>
</template>

<script>
import { orderApi } from '@/api'

export default {
  data() {
    return {
      orderId: null,
      order: null,
    }
  },

  computed: {
    statusTitle() {
      const map = {
        PENDING_PAY: 'Awaiting Payment',
        PENDING_SHIP: 'To Ship',
        PENDING_RECEIVE: 'To Receive',
        COMPLETED: 'Completed',
        CANCELLED: 'Cancelled',
        REFUNDING: 'Refunding',
        REFUNDED: 'Refunded',
      }
      return map[this.order?.status] || this.order?.status
    },
    statusDesc() {
      const map = {
        PENDING_PAY: 'Please complete payment before the order expires',
        PENDING_SHIP: 'Seller is preparing your order',
        PENDING_RECEIVE: 'Your order is on the way',
        COMPLETED: 'Thank you for your purchase',
        CANCELLED: 'This order has been cancelled',
      }
      return map[this.order?.status] || ''
    },
  },

  onLoad(query) {
    this.orderId = query.id
    this.loadOrder()
  },

  methods: {
    async loadOrder() {
      try {
        this.order = await orderApi.getOrderDetail(this.orderId)
      } catch (e) {
        uni.showToast({ title: 'Load failed', icon: 'none' })
      }
    },

    formatDate(dateStr) {
      if (!dateStr) return ''
      return new Date(dateStr).toLocaleString()
    },

    onPay() {
      uni.navigateTo({
        url: `/pages/order/pay?orderId=${this.orderId}&amount=${this.order.payAmount}`,
      })
    },

    onCancel() {
      uni.showModal({
        title: 'Cancel order?',
        success: async (res) => {
          if (res.confirm) {
            try {
              await orderApi.cancelOrder(this.orderId, '')
              this.loadOrder()
            } catch (e) {
              uni.showToast({ title: 'Cancel failed', icon: 'none' })
            }
          }
        },
      })
    },

    onConfirm() {
      uni.showModal({
        title: 'Confirm received?',
        success: async (res) => {
          if (res.confirm) {
            try {
              await orderApi.confirmReceived(this.orderId)
              this.loadOrder()
            } catch (e) {
              uni.showToast({ title: 'Failed', icon: 'none' })
            }
          }
        },
      })
    },

    onTrack() {
      uni.navigateTo({ url: `/pages/order/logistics?id=${this.orderId}` })
    },

    onReview() {
      uni.navigateTo({ url: `/pages/order/review?orderId=${this.orderId}` })
    },

    onRefund() {
      uni.navigateTo({ url: `/pages/order/refund?orderId=${this.orderId}` })
    },
  },
}
</script>

<style lang="scss" scoped>
.order-detail {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 120rpx;
}
</style>
