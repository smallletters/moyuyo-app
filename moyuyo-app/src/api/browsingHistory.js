import { get, post, del } from '@/utils/request'

export function getBrowsingHistory(params = {}) {
  return get('/api/v1/browsing-history', params)
}

export function addBrowsingHistory(productId) {
  return post('/api/v1/browsing-history', { productId })
}

export function deleteBrowsingHistory(id) {
  return del(`/api/v1/browsing-history/${id}`)
}

export function clearBrowsingHistory() {
  return del('/api/v1/browsing-history')
}

export default {
  getBrowsingHistory,
  addBrowsingHistory,
  deleteBrowsingHistory,
  clearBrowsingHistory,
}
