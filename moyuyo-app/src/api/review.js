import { get, post, del } from '@/utils/request'

export function getProductReviews(productId, params = {}) {
  return get(`/api/v1/products/${productId}/reviews`, params)
}

export function createReview(data) {
  return post('/api/v1/reviews', data)
}

export function getMyReviews(params = {}) {
  return get('/api/v1/reviews/mine', params)
}

export function deleteReview(id) {
  return del(`/api/v1/reviews/${id}`)
}

export default {
  getProductReviews,
  createReview,
  getMyReviews,
  deleteReview,
}
