import { get, post, put } from '@/utils/request'

export function getSubscribePlans() {
  return get('/api/v1/subscriptions/plans')
}

export function subscribe(planId) {
  return post('/api/v1/subscriptions/subscribe', { planId })
}

export function getMySubscription() {
  return get('/api/v1/subscriptions/my')
}

export function toggleAutoRenew() {
  return put('/api/v1/subscriptions/auto-renew')
}

export function getSubscribeHistory(params = {}) {
  return get('/api/v1/subscriptions/history', params)
}

export default {
  getSubscribePlans,
  subscribe,
  getMySubscription,
  toggleAutoRenew,
  getSubscribeHistory,
}
