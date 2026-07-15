<template>
  <view class="pet-profile">
    <view class="header">
      <view class="avatar-section">
        <view class="avatar">{{ pet.type === 'CAT' ? '🐈' : '🐕' }}</view>
        <text class="name">{{ pet.name }}</text>
        <text class="species">{{ typeLabel }} · {{ pet.breed || '-' }}</text>
      </view>
    </view>

    <view class="info-card card">
      <view class="info-row">
        <text class="label">Birthday</text>
        <text class="value">{{ pet.birthday || 'Unknown' }}</text>
      </view>
      <view class="info-row">
        <text class="label">Weight</text>
        <text class="value">{{ pet.weightKg || '-' }} kg</text>
      </view>
      <view class="info-row">
        <text class="label">Gender</text>
        <text class="value">{{ pet.gender === 1 ? 'Male' : pet.gender === 2 ? 'Female' : '-' }}</text>
      </view>
    </view>

    <view class="card weight-card">
      <text class="card-title">Growth Timeline</text>
      <view v-for="t in growthRecords" :key="t.id" class="timeline-row">
        <text class="timeline-date">{{ t.recordTime }}</text>
        <text class="timeline-event">{{ t.title }}</text>
      </view>
      <view v-if="growthRecords.length === 0" class="empty-timeline">No records yet</view>
    </view>

    <view class="bottom-bar safe-area-bottom">
      <view class="btn btn-outline" @click="onEdit">Edit</view>
      <view class="btn btn-primary" @click="onAddRecord">+ Add Record</view>
    </view>
  </view>
</template>

<script>
import { petApi } from '@/api'

const TYPE_LABELS = { DOG: 'Dog', CAT: 'Cat', OTHER: 'Other' }

export default {
  data() {
    return {
      pet: {},
      growthRecords: [],
    }
  },

  computed: {
    typeLabel() {
      return TYPE_LABELS[this.pet.type] || this.pet.type || ''
    },
  },

  onLoad(query) {
    this.loadPet(query.id || null)
  },

  methods: {
    async loadPet(petId) {
      if (!petId) {
        try {
          const pets = await petApi.getPets()
          if (pets.length > 0) {
            this.pet = pets[0]
            this.loadRecords(this.pet.id)
          }
        } catch (e) {
          console.warn('[pet-profile] load failed', e)
        }
        return
      }
      try {
        this.pet = await petApi.getPetDetail(petId)
        this.loadRecords(petId)
      } catch (e) {
        console.warn('[pet-profile] load pet failed', e)
      }
    },

    async loadRecords(petId) {
      try {
        this.growthRecords = await petApi.getGrowthRecords(petId)
      } catch (e) {
        this.growthRecords = []
      }
    },

    onEdit() {
      uni.showToast({ title: 'Edit coming soon', icon: 'none' })
    },

    onAddRecord() {
      if (!this.pet?.id) return
      uni.showModal({
        title: 'New Record',
        content: 'Title:',
        editable: true,
        success: async (res) => {
          if (res.confirm && res.content) {
            try {
              await petApi.createGrowthRecord(this.pet.id, {
                petId: this.pet.id,
                type: 'EXAM',
                title: res.content,
                recordTime: new Date().toISOString().split('T')[0],
              })
              this.loadRecords(this.pet.id)
              uni.showToast({ title: 'Added', icon: 'success' })
            } catch (e) {
              uni.showToast({ title: 'Failed', icon: 'none' })
            }
          }
        },
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.pet-profile { min-height: 100vh; background: var(--color-background); padding-bottom: 120rpx; }
.header { background: var(--color-surface); padding: 48rpx 24rpx; padding-top: calc(48rpx + env(safe-area-inset-top)); text-align: center; }
.avatar { font-size: 120rpx; display: block; margin-bottom: 16rpx; }
.name { font-size: var(--font-size-xl); font-weight: var(--font-weight-bold); display: block; }
.species { font-size: var(--font-size-sm); color: var(--color-text-tertiary); margin-top: 4rpx; display: block; }
.card { margin: 16rpx; padding: 24rpx; background: var(--color-surface); border-radius: var(--radius-md); }
.info-row { display: flex; justify-content: space-between; padding: 12rpx 0; border-bottom: 1rpx solid var(--color-divider); }
.info-row:last-child { border-bottom: none; }
.label { font-size: var(--font-size-sm); color: var(--color-text-tertiary); }
.value { font-size: var(--font-size-sm); font-weight: var(--font-weight-medium); }
.card-title { font-size: var(--font-size-base); font-weight: var(--font-weight-semibold); margin-bottom: 16rpx; display: block; }
.timeline-row { display: flex; gap: 16rpx; padding: 10rpx 0; border-bottom: 1rpx solid var(--color-divider); }
.timeline-row:last-child { border-bottom: none; }
.timeline-date { font-size: var(--font-size-xs); color: var(--color-text-tertiary); min-width: 160rpx; }
.timeline-event { font-size: var(--font-size-sm); }
.empty-timeline { text-align: center; padding: 24rpx; color: var(--color-text-tertiary); font-size: var(--font-size-sm); }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; display: flex; gap: 16rpx; padding: 16rpx 24rpx; padding-bottom: calc(16rpx + env(safe-area-inset-bottom)); background: var(--color-surface); border-top: 1rpx solid var(--color-divider); }
.bottom-bar .btn { flex: 1; text-align: center; padding: 20rpx 0; }
</style>
