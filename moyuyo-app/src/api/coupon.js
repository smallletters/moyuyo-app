import { get, post } from '@/utils/request'

export function getAvailableCoupons(params = {}) {
  return get('/api/v1/coupons', params)
}

export function claimCoupon(id) {
  return post(`/api/v1/coupons/${id}/claim`)
}

export function getMyCoupons(status) {
  return get('/api/v1/coupons/mine', { status })
}

export function useCoupon(userCouponId, orderId) {
  return post(`/api/v1/coupons/${userCouponId}/use`, { orderId })
}

export function getCouponDetail(id) {
  return get(`/api/v1/coupons/${id}`)
}

export default {
  getAvailableCoupons,
  claimCoupon,
  getMyCoupons,
  useCoupon,
  getCouponDetail,
}
