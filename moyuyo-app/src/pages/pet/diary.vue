<template>
  <view class="diary">
    <!-- 顶部导航 -->
    <view class="header">
      <view class="header-inner">
        <view class="header-btn" @click="onBack">
          <text class="header-btn-icon">←</text>
        </view>
        <text class="header-title">成长日记</text>
        <view class="header-btn header-btn-primary" @click="onWriteDiary">
          <text class="header-btn-icon header-btn-icon-white">✎</text>
        </view>
      </view>
    </view>

    <!-- 宠物信息摘要 -->
    <view class="pet-card" @click="onPetDetail">
      <view class="pet-avatar">
        <text class="pet-avatar-emoji">🐕</text>
      </view>
      <view class="pet-info">
        <view class="pet-name-row">
          <text class="pet-name">团子</text>
          <text class="pet-breed">金毛</text>
        </view>
        <view class="pet-meta">
          <text class="pet-age">2岁3个月</text>
          <text class="pet-divider">|</text>
          <text class="pet-weight">28.5 kg</text>
        </view>
      </view>
      <text class="pet-arrow">›</text>
    </view>

    <!-- 日记时间线 -->
    <view class="timeline">
      <view v-for="(monthGroup, mgIdx) in diaryData" :key="mgIdx" class="month-group">
        <!-- 月份标题 -->
        <view class="month-header">
          <text class="month-title">{{ monthGroup.month }}</text>
          <text class="month-count">{{ monthGroup.posts.length }}篇</text>
        </view>

        <view class="timeline-inner">
          <!-- 时间线竖线 -->
          <view class="timeline-line" />

          <view
            v-for="(post, pIdx) in monthGroup.posts"
            :key="pIdx"
            class="timeline-post"
            :class="{ 'timeline-post-last': pIdx === monthGroup.posts.length - 1 }"
          >
            <!-- 时间线圆点 -->
            <view class="timeline-dot" :style="{ background: post.dotColor }" />

            <!-- 日记卡片 -->
            <view class="post-card">
              <!-- 头部 -->
              <view class="post-header">
                <view class="post-author">
                  <view class="post-avatar">
                    <text class="post-avatar-emoji">🐾</text>
                  </view>
                  <text class="post-date">{{ post.date }}</text>
                </view>
                <view class="post-more" @click="onPostMore(post)">
                  <text class="post-more-icon">⋮</text>
                </view>
              </view>

              <!-- 内容 -->
              <text class="post-content">{{ post.content }}</text>

              <!-- 图片区域 -->
              <view
                v-if="post.images && post.images.length > 0"
                class="post-images"
                :class="'post-images-' + Math.min(post.images.length, 3)"
              >
                <view
                  v-for="(img, iIdx) in post.images.slice(0, 3)"
                  :key="iIdx"
                  class="post-image-item"
                  :style="{ background: img.color }"
                />
              </view>

              <!-- 体重记录卡片 -->
              <view v-if="post.weightRecord" class="weight-card">
                <view class="weight-card-icon">
                  <text class="weight-icon-text">⚖</text>
                </view>
                <view class="weight-card-info">
                  <text class="weight-card-label">体重记录</text>
                  <view class="weight-card-value">
                    <text class="weight-number">{{ post.weightRecord.value }}</text>
                    <view
                      class="weight-trend"
                      :class="
                        post.weightRecord.trend === 'up' ? 'weight-trend-up' : 'weight-trend-down'
                      "
                    >
                      <text class="weight-trend-icon">
                        {{ post.weightRecord.trend === 'up' ? '↑' : '↓' }}
                      </text>
                      <text class="weight-trend-text">{{ post.weightRecord.diff }}</text>
                    </view>
                  </view>
                </view>
                <text class="weight-compare">较上次</text>
              </view>

              <!-- 标签 -->
              <view v-if="post.tags && post.tags.length > 0" class="post-tags">
                <view
                  v-for="(tag, tIdx) in post.tags"
                  :key="tIdx"
                  class="post-tag"
                  :class="'tag-' + tag.type"
                >
                  <text class="tag-text">{{ tag.label }}</text>
                </view>
              </view>

              <!-- 互动 -->
              <view class="post-actions">
                <view class="post-action" @click="onLike(post)">
                  <text class="action-icon">{{ post.liked ? '👍' : '👍' }}</text>
                  <text class="action-count" :class="{ 'action-count-active': post.liked }">
                    {{ post.likes }}
                  </text>
                </view>
                <view class="post-action" @click="onComment(post)">
                  <text class="action-icon">💬</text>
                  <text class="action-count">{{ post.comments }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="diaryData.length === 0" class="empty-state">
      <view class="empty-icon">
        <text class="empty-emoji">📖</text>
      </view>
      <text class="empty-title">记录你和宠物的每一天</text>
      <text class="empty-desc">写下与宠物相处的美好时光</text>
      <view class="empty-btn" @click="onWriteDiary">
        <text class="empty-btn-text">写下第一篇日记</text>
      </view>
    </view>

    <!-- FAB 新建按钮 -->
    <view class="fab" @click="onWriteDiary">
      <text class="fab-icon">✎</text>
    </view>

    <!-- 写日记弹窗 -->
    <view v-if="showWriteModal" class="modal-overlay" @click="onCloseModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-cancel" @click="onCloseModal">取消</text>
          <text class="modal-title">写日记</text>
          <text class="modal-submit" @click="onSubmitDiary">发布</text>
        </view>

        <view class="modal-body">
          <input
            v-model="diaryForm.title"
            class="modal-input"
            placeholder="给日记起个标题（选填）"
          >
          <textarea
            v-model="diaryForm.content"
            class="modal-textarea"
            placeholder="记录你和宠物的今天..."
            maxlength="500"
          />
          <text class="modal-count">{{ diaryForm.content.length }} / 500</text>

          <!-- 心情选择 -->
          <view class="mood-section">
            <text class="mood-label">心情</text>
            <view class="mood-list">
              <view
                v-for="mood in moodOptions"
                :key="mood.value"
                class="mood-item"
                :class="{ 'mood-active': diaryForm.mood === mood.value }"
                @click="diaryForm.mood = mood.value"
              >
                <text class="mood-emoji">{{ mood.emoji }}</text>
                <text class="mood-text">{{ mood.label }}</text>
              </view>
            </view>
          </view>

          <!-- 标签选择 -->
          <view class="tag-section">
            <text class="mood-label">标签</text>
            <view class="tag-list">
              <view
                v-for="tag in tagOptions"
                :key="tag.value"
                class="tag-select-item"
                :class="{ 'tag-select-active': diaryForm.tags.includes(tag.value) }"
                @click="onToggleTag(tag.value)"
              >
                <text class="tag-select-text">{{ tag.label }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { petDiaryApi } from '@/api'

export default {
  data() {
    return {
      petId: null,
      showWriteModal: false,
      diaryForm: {
        title: '',
        content: '',
        mood: '',
        tags: [],
      },
      moodOptions: [
        { value: 'happy', emoji: '😊', label: '开心' },
        { value: 'love', emoji: '❤️', label: '有爱' },
        { value: 'funny', emoji: '😂', label: '搞笑' },
        { value: 'sad', emoji: '😢', label: '心疼' },
        { value: 'amazing', emoji: '😮', label: '惊喜' },
      ],
      tagOptions: [
        { value: 'daily', label: '#日常' },
        { value: 'outdoor', label: '#户外' },
        { value: 'health', label: '#健康' },
        { value: 'food', label: '#饮食' },
        { value: 'toy', label: '#玩具' },
        { value: 'grooming', label: '#护理' },
      ],
      diaryData: [],
    }
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
        const data = await petDiaryApi.getPetDiaries(this.petId)
        this.diaryData = data || []
      } catch (e) {
        console.warn('[diary] load failed', e)
        this.diaryData = []
      }
    },

    onPetDetail() {
      uni.showToast({ title: '查看宠物详情', icon: 'none' })
    },

    onWriteDiary() {
      this.showWriteModal = true
    },

    onCloseModal() {
      this.showWriteModal = false
      this.diaryForm = { title: '', content: '', mood: '', tags: [] }
    },

    async onSubmitDiary() {
      if (!this.diaryForm.content.trim()) {
        uni.showToast({ title: '请输入日记内容', icon: 'none' })
        return
      }
      try {
        await petDiaryApi.createPetDiary(this.petId, this.diaryForm)
        uni.showToast({ title: '发布成功', icon: 'success' })
        this.onCloseModal()
        this.loadData()
      } catch (e) {
        console.warn('[diary] submit failed', e)
        uni.showToast({ title: '发布失败', icon: 'none' })
      }
    },

    onPostMore(post) {
      const petId = this.petId
      uni.showActionSheet({
        itemList: ['编辑', '删除'],
        success: (res) => {
          if (res.tapIndex === 1) {
            uni.showModal({
              title: '删除日记',
              content: '确定要删除这篇日记吗？',
              success: async (confirmRes) => {
                if (confirmRes.confirm) {
                  try {
                    await petDiaryApi.deletePetDiary(petId, post.id)
                    uni.showToast({ title: '已删除', icon: 'success' })
                    this.loadData()
                  } catch (e) {
                    console.warn('[diary] delete failed', e)
                    uni.showToast({ title: '删除失败', icon: 'none' })
                  }
                }
              },
            })
          }
        },
      })
    },

    async onLike(post) {
      post.liked = !post.liked
      post.likes += post.liked ? 1 : -1
    },

    onComment(post) {
      uni.showToast({ title: '评论功能开发中', icon: 'none' })
    },

    onToggleTag(tag) {
      const idx = this.diaryForm.tags.indexOf(tag)
      if (idx > -1) {
        this.diaryForm.tags.splice(idx, 1)
      } else {
        this.diaryForm.tags.push(tag)
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.diary {
  min-height: 100vh;
  background: var(--color-background);
  padding-bottom: 200rpx;
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

.header-btn-primary {
  background: var(--color-primary);
}

.header-btn-icon {
  font-size: 36rpx;
  color: var(--color-primary);
  font-weight: var(--font-weight-bold);
}

.header-btn-icon-white {
  color: #ffffff;
}

// 宠物卡片
.pet-card {
  margin: 24rpx 24rpx 0;
  padding: 28rpx;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  gap: 20rpx;
  box-shadow: var(--shadow-sm);
}

.pet-avatar {
  width: 112rpx;
  height: 112rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-divider));
  border: 2rpx solid var(--color-primary);
  flex-shrink: 0;
}

.pet-avatar-emoji {
  font-size: 56rpx;
}

.pet-info {
  flex: 1;
  min-width: 0;
}

.pet-name-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.pet-name {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.pet-breed {
  font-size: var(--font-size-xs);
  padding: 4rpx 12rpx;
  border-radius: var(--radius-pill);
  background: var(--color-primary-light);
  color: var(--color-primary-dark);
  font-weight: var(--font-weight-semibold);
}

.pet-meta {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-top: 8rpx;
}

.pet-age,
.pet-weight {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
}

.pet-divider {
  font-size: var(--font-size-xs);
  color: var(--color-divider);
}

.pet-arrow {
  font-size: 36rpx;
  color: var(--color-text-tertiary);
  flex-shrink: 0;
}

// 时间线
.timeline {
  padding: 24rpx 24rpx 0;
}

.month-group {
  margin-bottom: 40rpx;
}

.month-header {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.month-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.month-count {
  font-size: var(--font-size-xs);
  padding: 4rpx 16rpx;
  border-radius: var(--radius-pill);
  background: var(--color-divider);
  color: var(--color-text-tertiary);
  font-weight: var(--font-weight-medium);
}

.timeline-inner {
  position: relative;
  padding-left: 56rpx;
}

.timeline-line {
  position: absolute;
  left: 20rpx;
  top: 0;
  bottom: 0;
  width: 2rpx;
  background: var(--color-divider);
}

.timeline-post {
  position: relative;
  padding-bottom: 32rpx;
}

.timeline-post-last {
  padding-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: 10rpx;
  top: 8rpx;
  width: 22rpx;
  height: 22rpx;
  border-radius: 50%;
  border: 4rpx solid var(--color-surface);
  z-index: 1;
}

// 日记卡片
.post-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: 28rpx;
  box-shadow: var(--shadow-sm);
  border: 1rpx solid var(--color-divider);
}

.post-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.post-author {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.post-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--color-primary-light), var(--color-divider));
  flex-shrink: 0;
}

.post-avatar-emoji {
  font-size: 32rpx;
}

.post-date {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
}

.post-more {
  width: 56rpx;
  height: 56rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--color-divider);
}

.post-more-icon {
  font-size: 32rpx;
  color: var(--color-text-tertiary);
  font-weight: var(--font-weight-bold);
}

.post-content {
  font-size: var(--font-size-sm);
  line-height: 1.6;
  color: var(--color-text-secondary);
  display: block;
  margin-bottom: 20rpx;
}

// 图片区域
.post-images {
  display: flex;
  gap: 8rpx;
  margin-bottom: 20rpx;
}

.post-images-1 .post-image-item {
  flex: 1;
}

.post-images-2 .post-image-item {
  flex: 1;
}

.post-images-3 .post-image-item {
  flex: 1;
}

.post-image-item {
  height: 192rpx;
  border-radius: calc(var(--radius-md) - 4rpx);
}

// 体重记录卡片
.weight-card {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 20rpx;
  background: var(--color-background);
  border-radius: var(--radius-md);
  margin-bottom: 20rpx;
}

.weight-card-icon {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--color-success);
  opacity: 0.15;
  flex-shrink: 0;
}

.weight-icon-text {
  font-size: 32rpx;
  opacity: 1;
}

.weight-card-info {
  flex: 1;
}

.weight-card-label {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  display: block;
}

.weight-card-value {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-top: 4rpx;
}

.weight-number {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.weight-trend {
  display: flex;
  align-items: center;
  gap: 4rpx;
  padding: 4rpx 12rpx;
  border-radius: var(--radius-pill);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
}

.weight-trend-up {
  background: rgba(230, 185, 122, 0.15);
  color: var(--color-warning);
}

.weight-trend-down {
  background: var(--color-success);
  color: #ffffff;
  opacity: 0.85;
}

.weight-trend-icon {
  font-size: 20rpx;
}

.weight-trend-text {
  font-size: 20rpx;
}

.weight-compare {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  flex-shrink: 0;
}

// 标签
.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8rpx;
  margin-bottom: 20rpx;
}

.post-tag {
  padding: 6rpx 16rpx;
  border-radius: var(--radius-pill);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
}

.tag-daily {
  background: var(--color-primary-light);
  color: var(--color-primary-dark);
}

.tag-outdoor {
  background: var(--color-success);
  color: #ffffff;
  opacity: 0.85;
}

.tag-health {
  background: var(--color-warning);
  color: #ffffff;
  opacity: 0.85;
}

.tag-food {
  background: #f0e6d0;
  color: #8a7a5a;
}

.tag-toy {
  background: var(--color-warm);
  color: #ffffff;
  opacity: 0.85;
}

.tag-grooming {
  background: var(--color-info);
  color: #ffffff;
  opacity: 0.85;
}

.tag-text {
  font-size: 20rpx;
}

// 互动
.post-actions {
  display: flex;
  align-items: center;
  gap: 32rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid var(--color-divider);
}

.post-action {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.action-icon {
  font-size: 28rpx;
}

.action-count {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
}

.action-count-active {
  color: var(--color-primary);
}

// 空状态
.empty-state {
  margin: 80rpx 24rpx;
  padding: 80rpx 24rpx;
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
  bottom: calc(160rpx + env(safe-area-inset-bottom));
  right: 40rpx;
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
  font-size: 48rpx;
  color: #ffffff;
  font-weight: var(--font-weight-light);
}

// 弹窗遮罩
.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 60;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: flex-end;
}

.modal-content {
  width: 100%;
  max-height: 80vh;
  background: var(--color-surface);
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 32rpx;
  border-bottom: 1rpx solid var(--color-divider);
  flex-shrink: 0;
}

.modal-cancel {
  font-size: var(--font-size-sm);
  color: var(--color-text-tertiary);
}

.modal-title {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text);
}

.modal-submit {
  font-size: var(--font-size-sm);
  color: var(--color-primary);
  font-weight: var(--font-weight-semibold);
}

.modal-body {
  padding: 24rpx 32rpx;
  overflow-y: auto;
  flex: 1;
}

.modal-input {
  width: 100%;
  height: 80rpx;
  border: none;
  border-bottom: 1rpx solid var(--color-divider);
  font-size: var(--font-size-base);
  color: var(--color-text);
  background: transparent;
  margin-bottom: 24rpx;
}

.modal-textarea {
  width: 100%;
  height: 240rpx;
  border: none;
  font-size: var(--font-size-sm);
  color: var(--color-text);
  background: transparent;
  line-height: 1.6;
  resize: none;
}

.modal-count {
  font-size: var(--font-size-xs);
  color: var(--color-text-tertiary);
  display: block;
  text-align: right;
}

// 心情选择
.mood-section {
  margin-top: 32rpx;
}

.mood-label {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--color-text);
  display: block;
  margin-bottom: 16rpx;
}

.mood-list {
  display: flex;
  gap: 20rpx;
}

.mood-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
  padding: 16rpx 20rpx;
  border-radius: var(--radius-md);
  border: 2rpx solid var(--color-divider);
  background: var(--color-surface);
}

.mood-active {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}

.mood-emoji {
  font-size: 40rpx;
}

.mood-text {
  font-size: 20rpx;
  color: var(--color-text-tertiary);
}

// 标签选择
.tag-section {
  margin-top: 32rpx;
  margin-bottom: 48rpx;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.tag-select-item {
  padding: 12rpx 24rpx;
  border-radius: var(--radius-pill);
  border: 1rpx solid var(--color-divider);
  background: var(--color-surface);
}

.tag-select-active {
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}

.tag-select-text {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-medium);
}
</style>
