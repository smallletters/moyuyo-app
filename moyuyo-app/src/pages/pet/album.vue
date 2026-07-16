<template>
  <view class="album">
    <!-- 顶部导航 -->
    <view class="header">
      <view class="header-inner">
        <view class="header-btn" @click="onBack">
          <text class="header-btn-icon">←</text>
        </view>
        <text class="header-title">宠物相册</text>
        <view class="header-btn" @click="onAddPhoto">
          <text class="header-btn-icon">+</text>
        </view>
      </view>
    </view>

    <!-- 存储空间提示 -->
    <view class="storage-card">
      <view class="storage-top">
        <view class="storage-label">
          <text class="storage-icon">📁</text>
          <text class="storage-text">存储空间</text>
        </view>
        <text class="storage-count">{{ storageUsed }} / {{ storageTotal }} 张</text>
      </view>
      <view class="storage-track">
        <view class="storage-fill" :style="{ width: storagePercent + '%' }" />
      </view>
      <text class="storage-hint">
        已使用 {{ storagePercent }}%，还可添加 {{ storageTotal - storageUsed }} 张照片
      </text>
    </view>

    <!-- 月份导航 -->
    <view class="month-nav">
      <view class="month-btn" @click="prevMonth">
        <text class="month-arrow">‹</text>
      </view>
      <text class="month-label">{{ currentMonthLabel }}</text>
      <view class="month-btn" @click="nextMonth">
        <text class="month-arrow">›</text>
      </view>
    </view>

    <!-- 照片网格 -->
    <view class="photo-section">
      <view v-for="(group, gIdx) in photoGroups" :key="gIdx">
        <!-- 日期标记 -->
        <view class="date-marker">
          <text class="date-text">{{ group.date }}</text>
        </view>
        <!-- 3列网格 -->
        <view class="photo-grid">
          <view
            v-for="(photo, pIdx) in group.photos"
            :key="pIdx"
            class="photo-item"
            :style="{ paddingTop: photo.ratio + '%' }"
            @click="onOpenPreview(group, pIdx)"
          >
            <view class="photo-placeholder" :style="{ background: photo.color }" />
            <view class="photo-date">
              <text class="photo-date-text">{{ photo.time }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="photoGroups.length === 0" class="empty-state">
      <view class="empty-icon">
        <text class="empty-emoji">📷</text>
      </view>
      <text class="empty-title">还没有照片</text>
      <text class="empty-desc">记录宠物的精彩瞬间</text>
      <view class="empty-btn" @click="onAddPhoto">
        <text class="empty-btn-text">添加第一张照片</text>
      </view>
    </view>

    <!-- FAB 添加按钮 -->
    <view class="fab" @click="onAddPhoto">
      <text class="fab-icon">+</text>
    </view>

    <!-- 图片预览遮罩 -->
    <view v-if="previewVisible" class="preview-overlay" @click="onClosePreview">
      <view class="preview-top">
        <view class="preview-actions-left">
          <view class="preview-action-btn" @click.stop="onClosePreview">
            <text class="preview-action-text">✕</text>
          </view>
        </view>
        <view class="preview-actions-right">
          <view class="preview-action-btn" @click.stop="onSharePhoto">
            <text class="preview-action-icon">↗</text>
          </view>
          <view class="preview-action-btn" @click.stop="onDeletePhoto">
            <text class="preview-action-icon">🗑</text>
          </view>
        </view>
      </view>
      <view class="preview-image-wrap" @click.stop>
        <view class="preview-placeholder" :style="{ background: currentPreviewColor }" />
      </view>
      <view class="preview-nav" @click.stop>
        <view class="preview-nav-btn" @click="onPrevPhoto">
          <text class="nav-arrow">‹</text>
        </view>
        <view class="preview-nav-btn" @click="onNextPhoto">
          <text class="nav-arrow">›</text>
        </view>
      </view>
      <view class="preview-counter">
        <text class="counter-text">{{ previewIndex + 1 }} / {{ previewTotal }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { petAlbumApi } from '@/api'

export default {
  data() {
    const now = new Date()
    return {
      petId: null,
      storageUsed: 0,
      storageTotal: 500,
      currentYear: now.getFullYear(),
      currentMonth: now.getMonth(),
      previewVisible: false,
      previewGroupIndex: 0,
      previewIndex: 0,
      photoGroups: [],
    }
  },

  computed: {
    storagePercent() {
      return Math.round((this.storageUsed / this.storageTotal) * 100)
    },
    currentMonthLabel() {
      return `${this.currentYear}年${this.currentMonth + 1}月`
    },
    currentPreviewColor() {
      const allPhotos = []
      for (const g of this.photoGroups) {
        for (const p of g.photos) {
          allPhotos.push(p)
        }
      }
      const flatIdx = (() => {
        let idx = 0
        for (let g = 0; g < this.photoGroups.length; g++) {
          if (g === this.previewGroupIndex) {
            idx += this.previewIndex
            break
          }
          idx += this.photoGroups[g].photos.length
        }
        return idx
      })()
      const gradients = [
        'linear-gradient(135deg, #d4a574, #c4956a, #b8845c)',
        'linear-gradient(160deg, #f5e6d0, #edd5b8, #e8cda8)',
        'linear-gradient(145deg, #f2d6d6, #ecc8c8, #e6baba)',
        'linear-gradient(130deg, #d6e4f2, #c8d8ec, #bacede)',
        'linear-gradient(140deg, #c4956a, #b8845c, #a8734d)',
        'linear-gradient(150deg, #edd5b8, #f2d6d6, #f5e0d0)',
        'linear-gradient(135deg, #e8cda8, #d4a574, #c9b896)',
        'linear-gradient(125deg, #d8e2ea, #ccd6de, #c2ccd6)',
        'linear-gradient(155deg, #e6baba, #d4a574, #f2d6d6)',
        'linear-gradient(140deg, #f5ece0, #edd5b8, #e8dac0)',
        'linear-gradient(130deg, #c9b896, #b8845c, #d4a574)',
        'linear-gradient(150deg, #ecc8c8, #d8e2ea, #f5e6d0)',
        'linear-gradient(135deg, #bacede, #a8c4d6, #c4956a)',
        'linear-gradient(145deg, #f2d6d6, #e8cda8, #edd5b8)',
        'linear-gradient(140deg, #d4a574, #e6baba, #c9b896)',
      ]
      return gradients[flatIdx % gradients.length]
    },
    previewTotal() {
      let total = 0
      for (const g of this.photoGroups) {
        total += g.photos.length
      }
      return total
    },
  },

  onLoad(query) {
    this.petId = query.petId || null
    this.loadData()
  },

  methods: {
    onBack() {
      uni.navigateBack()
    },

    async loadData() {
      if (!this.petId) return
      try {
        const data = await petAlbumApi.getPetAlbums(this.petId)
        this.photoGroups = data.groups || data || []
        this.storageUsed = data.used || 0
      } catch (e) {
        console.warn('[album] load failed', e)
        this.photoGroups = []
      }
    },

    async onAddPhoto() {
      if (!this.petId) return
      try {
        uni.chooseImage({
          count: 1,
          success: async (res) => {
            const tempPath = res.tempFilePaths[0]
            await petAlbumApi.createPetAlbum(this.petId, { image: tempPath })
            uni.showToast({ title: '上传成功', icon: 'success' })
            this.loadData()
          },
        })
      } catch (e) {
        console.warn('[album] upload failed', e)
        uni.showToast({ title: '上传失败', icon: 'none' })
      }
    },

    prevMonth() {
      if (this.currentMonth === 0) {
        this.currentMonth = 11
        this.currentYear -= 1
      } else {
        this.currentMonth -= 1
      }
    },

    nextMonth() {
      if (this.currentMonth === 11) {
        this.currentMonth = 0
        this.currentYear += 1
      } else {
        this.currentMonth += 1
      }
    },

    onOpenPreview(group, idx) {
      this.previewGroupIndex = this.photoGroups.indexOf(group)
      this.previewIndex = idx
      this.previewVisible = true
    },

    onClosePreview() {
      this.previewVisible = false
    },

    onPrevPhoto() {
      let total = 0
      for (let g = 0; g < this.photoGroups.length; g++) {
        if (g < this.previewGroupIndex) {
          total += this.photoGroups[g].photos.length
        }
      }
      total += this.previewIndex
      total = (total - 1 + this.previewTotal) % this.previewTotal
      let acc = 0
      for (let g = 0; g < this.photoGroups.length; g++) {
        if (total < acc + this.photoGroups[g].photos.length) {
          this.previewGroupIndex = g
          this.previewIndex = total - acc
          break
        }
        acc += this.photoGroups[g].photos.length
      }
    },

    onNextPhoto() {
      let total = 0
      for (let g = 0; g < this.photoGroups.length; g++) {
        if (g < this.previewGroupIndex) {
          total += this.photoGroups[g].photos.length
        }
      }
      total += this.previewIndex
      total = (total + 1) % this.previewTotal
      let acc = 0
      for (let g = 0; g < this.photoGroups.length; g++) {
        if (total < acc + this.photoGroups[g].photos.length) {
          this.previewGroupIndex = g
          this.previewIndex = total - acc
          break
        }
        acc += this.photoGroups[g].photos.length
      }
    },

    onSharePhoto() {
      uni.showToast({ title: '分享照片', icon: 'none' })
    },

    onDeletePhoto() {
      if (!this.petId) return
      const target = this.photoGroups[this.previewGroupIndex].photos[this.previewIndex]
      uni.showModal({
        title: '删除照片',
        content: '确定要删除这张照片吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await petAlbumApi.deletePetAlbum(this.petId, target.id)
              uni.showToast({ title: '已删除', icon: 'success' })
              this.previewVisible = false
              this.loadData()
            } catch (e) {
              console.warn('[album] delete failed', e)
              uni.showToast({ title: '删除失败', icon: 'none' })
            }
          }
        },
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.album {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 160rpx;
}

// 顶部导航
.header {
  position: sticky;
  top: 0;
  z-index: 30;
  background: var(--color-surface);
  border-bottom: 1rpx solid var(--color-divider);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 24rpx;
}

.header-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.header-btn {
  width: 72rpx;
  height: 72rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--color-surface);
}

.header-btn-icon {
  font-size: 40rpx;
  color: var(--color-primary);
  font-weight: var(--font-weight-bold);
}

// 存储卡片
.storage-card {
  margin: 24rpx 24rpx 0;
  padding: 28rpx;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
}

.storage-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.storage-label {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.storage-icon {
  font-size: 28rpx;
}

.storage-text {
  font-size: var(--font-size-sm);
  color: var(--color-text);
  font-weight: var(--font-weight-medium);
}

.storage-count {
  font-size: var(--font-size-sm);
  color: var(--color-primary);
  font-weight: var(--font-weight-semibold);
}

.storage-track {
  height: 8rpx;
  border-radius: 4rpx;
  background: var(--color-divider);
  overflow: hidden;
}

.storage-fill {
  height: 100%;
  border-radius: 4rpx;
  background: var(--color-primary);
  transition: width 0.4s ease;
}

.storage-hint {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  margin-top: 12rpx;
  display: block;
}

// 月份导航
.month-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32rpx 40rpx 16rpx;
}

.month-btn {
  width: 56rpx;
  height: 56rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--color-surface);
  box-shadow: var(--shadow-sm);
}

.month-arrow {
  font-size: 36rpx;
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-medium);
}

.month-label {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

// 照片网格
.photo-section {
  padding: 0 8rpx;
}

.date-marker {
  padding: 16rpx 16rpx 8rpx;
}

.date-text {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-tertiary);
}

.photo-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 4rpx;
}

.photo-item {
  position: relative;
  border-radius: 8rpx;
  overflow: hidden;
  cursor: pointer;
}

.photo-placeholder {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.photo-date {
  position: absolute;
  bottom: 8rpx;
  left: 8rpx;
  padding: 2rpx 10rpx;
  border-radius: 6rpx;
  background: rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(4px);
}

.photo-date-text {
  font-size: 18rpx;
  color: #ffffff;
  font-weight: var(--font-weight-medium);
}

// 空状态
.empty-state {
  margin: 48rpx 24rpx;
  padding: 64rpx 24rpx;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  text-align: center;
}

.empty-icon {
  width: 96rpx;
  height: 96rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--color-divider);
  margin: 0 auto 24rpx;
}

.empty-emoji {
  font-size: 48rpx;
}

.empty-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
  display: block;
}

.empty-desc {
  font-size: var(--font-size-sm);
  color: var(--color-text-tertiary);
  margin-top: 8rpx;
  display: block;
}

.empty-btn {
  display: inline-flex;
  align-items: center;
  gap: 12rpx;
  margin-top: 24rpx;
  padding: 16rpx 32rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary);
}

.empty-btn-text {
  font-size: var(--font-size-sm);
  color: #ffffff;
  font-weight: var(--font-weight-semibold);
}

// FAB
.fab {
  position: fixed;
  z-index: 20;
  bottom: calc(120rpx + env(safe-area-inset-bottom));
  left: 50%;
  transform: translateX(-50%);
  width: 112rpx;
  height: 112rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--color-primary);
  box-shadow: 0 8rpx 32rpx rgba(219, 201, 138, 0.4);
}

.fab-icon {
  font-size: 56rpx;
  color: #ffffff;
  font-weight: var(--font-weight-light);
  line-height: 1;
}

// 预览遮罩
.preview-overlay {
  position: fixed;
  inset: 0;
  z-index: 50;
  background: rgba(0, 0, 0, 0.95);
  display: flex;
  flex-direction: column;
}

.preview-top {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 80rpx 24rpx 24rpx;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.6) 0%, transparent 100%);
}

.preview-actions-left,
.preview-actions-right {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.preview-action-btn {
  width: 72rpx;
  height: 72rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
}

.preview-action-text {
  font-size: 36rpx;
  color: #ffffff;
}

.preview-action-icon {
  font-size: 32rpx;
  color: #ffffff;
}

.preview-image-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-placeholder {
  width: 100%;
  height: 100%;
}

// 导航箭头
.preview-nav {
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  transform: translateY(-50%);
  display: flex;
  justify-content: space-between;
  padding: 0 16rpx;
  pointer-events: none;
}

.preview-nav-btn {
  width: 80rpx;
  height: 80rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.4);
  pointer-events: auto;
}

.nav-arrow {
  font-size: 40rpx;
  color: #ffffff;
}

.preview-counter {
  position: absolute;
  bottom: 80rpx;
  left: 0;
  right: 0;
  text-align: center;
}

.counter-text {
  font-size: var(--font-size-sm);
  color: rgba(255, 255, 255, 0.6);
  font-weight: var(--font-weight-medium);
}
</style>
