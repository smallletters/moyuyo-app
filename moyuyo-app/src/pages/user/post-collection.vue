<template>
  <view class="post-collection">
    <view class="nav-header dark">
      <view class="nav-back" @click="goBack">
        <text class="back-icon light">‹</text>
      </view>
      <text class="nav-title light">我的收藏</text>
      <view class="nav-placeholder" />
    </view>

    <scroll-view class="scroll" scroll-y>
      <view class="collection-section">
        <view v-if="collections.length === 0" class="empty-state">
          <text class="empty-icon">📑</text>
          <text class="empty-text">还没有收藏的笔记</text>
          <text class="empty-hint">去社区发现感兴趣的内容吧</text>
          <view class="empty-btn" @click="goCommunity">去社区逛逛</view>
        </view>

        <view v-for="(item, idx) in collections" :key="item.id" class="collection-card">
          <view class="card-user">
            <view class="user-avatar" :style="{ background: item.avatarBg }">
              <text class="avatar-text">{{ item.username[0] }}</text>
            </view>
            <view class="user-info">
              <text class="username">{{ item.username }}</text>
              <text class="post-time">{{ item.time }}</text>
            </view>
            <view class="card-remove" @click="removeCollection(idx)">
              <text class="remove-text">取消收藏</text>
            </view>
          </view>

          <view class="card-content" @click="goDetail(item)">
            <text class="content-text">{{ item.content }}</text>
          </view>

          <view v-if="item.image" class="card-image" :style="{ background: item.imageBg }">
            <text class="image-placeholder">{{ item.imageText }}</text>
          </view>

          <view class="card-footer">
            <view class="category-tag" :style="{ background: item.tagBg, color: item.tagColor }">
              <text>{{ item.category }}</text>
            </view>
            <view class="card-stats">
              <text class="stat-item">❤️ {{ item.likes }}</text>
              <text class="stat-item">💬 {{ item.comments }}</text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      collections: [
        {
          id: 1,
          username: '萌宠日记',
          avatarBg: 'linear-gradient(135deg, #fbc2eb, #a6c1ee)',
          time: '3小时前',
          content:
            '今天带我家柯基去公园，发现了一片超美的草坪！分享几个拍照小技巧，保证出片率100%~',
          image: true,
          imageBg: 'linear-gradient(135deg, #a8d8ea, #aa96da)',
          imageText: '📷 萌宠摄影',
          category: '养宠日常',
          tagBg: 'rgba(219, 201, 138, 0.15)',
          tagColor: 'var(--color-primary-dark)',
          likes: 128,
          comments: 36,
        },
        {
          id: 2,
          username: '喵星人研究所',
          avatarBg: 'linear-gradient(135deg, #ffecd2, #fcb69f)',
          time: '昨天',
          content: '猫咪洗澡的正确姿势🧼 很多铲屎官都洗错了！看完这篇保证你家的猫再也不怕洗澡了~',
          image: true,
          imageBg: 'linear-gradient(135deg, #c4b5fd, #818cf8)',
          imageText: '🐱 猫咪护理',
          category: '洗护知识',
          tagBg: 'rgba(171, 185, 173, 0.15)',
          tagColor: 'var(--color-success)',
          likes: 256,
          comments: 89,
        },
        {
          id: 3,
          username: '宠物穿搭达人',
          avatarBg: 'linear-gradient(135deg, #fca5a5, #f97316)',
          time: '2天前',
          content: '秋冬新款宠物毛衣上新啦🧣 保暖又时尚，让毛孩子在冬天也能做最靓的崽！',
          image: false,
          imageBg: '',
          imageText: '',
          category: '穿搭推荐',
          tagBg: 'rgba(179, 138, 90, 0.15)',
          tagColor: 'var(--color-accent)',
          likes: 64,
          comments: 18,
        },
        {
          id: 4,
          username: '宠物营养师说',
          avatarBg: 'linear-gradient(135deg, #86efac, #22c55e)',
          time: '3天前',
          content: '自制的宠物营养餐食谱来啦🍗 三文鱼+胡萝卜+西兰花的黄金搭配，狗狗超爱吃！',
          image: true,
          imageBg: 'linear-gradient(135deg, #fef3c7, #fde68a)',
          imageText: '🥗 健康食谱',
          category: '健康饮食',
          tagBg: 'rgba(219, 201, 138, 0.15)',
          tagColor: 'var(--color-primary-dark)',
          likes: 192,
          comments: 45,
        },
      ],
    }
  },

  methods: {
    goBack() {
      uni.navigateBack()
    },

    removeCollection(idx) {
      uni.showModal({
        title: '取消收藏',
        content: '确定要取消收藏这篇笔记吗？',
        success: (res) => {
          if (res.confirm) {
            this.collections.splice(idx, 1)
            uni.showToast({ title: '已取消收藏', icon: 'success' })
          }
        },
      })
    },

    goDetail(item) {
      uni.showToast({ title: item.content.slice(0, 10) + '...', icon: 'none' })
    },

    goCommunity() {
      uni.switchTab({ url: '/pages/tabbar/community' })
    },
  },
}
</script>

<style lang="scss" scoped>
.post-collection {
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

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 120rpx 24rpx;
}

.empty-icon {
  font-size: 96rpx;
  margin-bottom: 24rpx;
}

.empty-text {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.empty-hint {
  font-size: var(--font-size-sm);
  color: var(--color-text-tertiary);
  margin-top: 8rpx;
}

.empty-btn {
  margin-top: 32rpx;
  padding: 16rpx 48rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary);
  color: var(--color-text);
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
}

.collection-section {
  padding: 24rpx;
}

.collection-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 24rpx;
  margin-bottom: 16rpx;
  border: 1rpx solid var(--color-divider);
}

.card-user {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 16rpx;
}

.user-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-text {
  font-size: 28rpx;
  font-weight: var(--font-weight-semibold);
  color: #fff;
}

.user-info {
  flex: 1;
}

.username {
  display: block;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.post-time {
  display: block;
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  margin-top: 2rpx;
}

.card-remove {
  padding: 8rpx 16rpx;
  border-radius: var(--radius-pill);
  border: 1rpx solid var(--color-divider);
}

.remove-text {
  font-size: 20rpx;
  color: var(--color-text-tertiary);
}

.card-content {
  margin-bottom: 12rpx;
}

.content-text {
  font-size: var(--font-size-base);
  color: var(--color-text);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-image {
  width: 100%;
  height: 320rpx;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.image-placeholder {
  font-size: 32rpx;
  color: rgba(255, 255, 255, 0.7);
  font-weight: var(--font-weight-semibold);
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.category-tag {
  padding: 4rpx 16rpx;
  border-radius: var(--radius-pill);
  font-size: 20rpx;
  font-weight: var(--font-weight-medium);
}

.card-stats {
  display: flex;
  gap: 16rpx;
}

.stat-item {
  font-size: 20rpx;
  color: var(--color-text-tertiary);
}
</style>
