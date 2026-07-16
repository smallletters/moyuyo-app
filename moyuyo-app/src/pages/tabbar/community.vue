<template>
  <view class="community">
    <view class="header">
      <text class="title">Community</text>
      <view class="header-actions">
        <u-icon
          name="plus"
          color="#2E2B29"
          size="22"
          @click="goCreate" />
      </view>
    </view>

    <view class="tabs">
      <view
        v-for="t in tabs"
        :key="t.value"
        class="tab"
        :class="{ active: activeTab === t.value }"
        @click="onTabChange(t.value)"
      >
        {{ t.label }}
      </view>
    </view>

    <scroll-view scroll-y class="feed" @scrolltolower="onLoadMore">
      <view
        v-for="p in posts"
        :key="p.id"
        class="post-card"
        @click="goDetail(p.id)">
        <view class="post-header">
          <image :src="p.avatar || defaultAvatar" class="post-avatar" />
          <view class="post-user">
            <text class="post-username">{{ p.username || 'Pet Lover' }}</text>
            <text class="post-time">{{ formatTime(p.createTime) }}</text>
          </view>
        </view>
        <text class="post-content">{{ p.content }}</text>
        <image
          v-if="p.images && p.images[0]"
          :src="p.images[0]"
          class="post-image"
          mode="widthFix"
        />
        <view class="post-actions">
          <view class="post-action" @click.stop="onLike(p)">
            <text>{{ p.liked ? '♥' : '♡' }}</text>
            <text>{{ p.likes || 0 }}</text>
          </view>
          <view class="post-action" @click.stop>
            <text>💬</text>
            <text>{{ p.comments || 0 }}</text>
          </view>
        </view>
      </view>

      <view v-if="loading" class="status">Loading...</view>
      <view v-else-if="noMore && !posts.length" class="status">No posts yet</view>
      <view v-else-if="noMore" class="status">— No more —</view>
    </scroll-view>
  </view>
</template>

<script>
import { communityApi } from '@/api'

export default {
  data() {
    return {
      activeTab: 'recommend',
      tabs: [
        { value: 'recommend', label: '推荐' },
        { value: 'follow', label: '关注' },
      ],
      posts: [],
      loading: false,
      noMore: false,
      page: 1,
      defaultAvatar: 'https://i.pravatar.cc/100?img=1',
    }
  },

  onLoad() {
    this.loadPosts(true)
  },

  methods: {
    async loadPosts(reset = false) {
      if (reset) {
        this.page = 1
        this.noMore = false
        this.posts = []
      }
      this.loading = true
      try {
        const res = await communityApi.getCommunityPosts({ page: this.page, size: 20 })
        const list = res.records || res || []
        this.posts.push(...list)
        this.noMore = list.length < 20
        this.page += 1
      } catch (e) {
        console.error('[community] load error', e)
      } finally {
        this.loading = false
      }
    },

    onTabChange(value) {
      this.activeTab = value
      this.loadPosts(true)
    },

    onLoadMore() {
      if (this.loading || this.noMore) return
      this.loadPosts(false)
    },

    async onLike(p) {
      try {
        if (p.liked) {
          await communityApi.unlikePost(p.id)
          p.liked = false
          p.likes = Math.max(0, (p.likes || 1) - 1)
        } else {
          await communityApi.likePost(p.id)
          p.liked = true
          p.likes = (p.likes || 0) + 1
        }
      } catch (e) {
        /* ignore */
      }
    },

    goDetail(id) {
      uni.navigateTo({ url: `/pages/community/detail?id=${id}` })
    },

    goCreate() {
      uni.navigateTo({ url: '/pages/community/create' })
    },

    formatTime(time) {
      if (!time) return ''
      const d = new Date(time)
      const now = Date.now()
      const diff = now - d.getTime()
      if (diff < 3600000) return Math.floor(diff / 60000) + 'm ago'
      if (diff < 86400000) return Math.floor(diff / 3600000) + 'h ago'
      return d.toLocaleDateString()
    },
  },
}
</script>

<style lang="scss" scoped>
.community {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: var(--color-background);
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx 24rpx 16rpx;
  background: var(--color-surface);
  padding-top: calc(32rpx + env(safe-area-inset-top));
}
.header-actions {
  display: flex;
  gap: 16rpx;
}
.title {
  font-size: 36rpx;
  font-weight: var(--font-weight-bold);
  color: var(--color-text);
}
.tabs {
  display: flex;
  background: var(--color-surface);
  border-bottom: 1rpx solid var(--color-divider);
  padding: 0 24rpx;
}
.tab {
  padding: 20rpx 24rpx;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  position: relative;
}
.tab.active {
  color: var(--color-primary);
  font-weight: var(--font-weight-semibold);
}
.tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 40rpx;
  height: 4rpx;
  background: var(--color-primary);
  border-radius: 2rpx;
}
.feed {
  flex: 1;
  padding: 16rpx;
}
.post-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 24rpx;
  margin-bottom: 16rpx;
}
.post-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 12rpx;
}
.post-avatar {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
}
.post-user {
  flex: 1;
}
.post-username {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  display: block;
}
.post-time {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
}
.post-content {
  font-size: var(--font-size-base);
  line-height: 1.6;
  margin-bottom: 12rpx;
}
.post-image {
  width: 100%;
  border-radius: var(--radius-sm);
  margin-bottom: 12rpx;
}
.post-actions {
  display: flex;
  gap: 32rpx;
}
.post-action {
  display: flex;
  align-items: center;
  gap: 8rpx;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}
.status {
  text-align: center;
  padding: 32rpx;
  color: var(--color-text-tertiary);
  font-size: var(--font-size-sm);
}
</style>
