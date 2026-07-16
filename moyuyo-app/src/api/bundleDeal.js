import { get } from '@/utils/request'

export function getBundleDeals(params = {}) {
  return get('/api/v1/bundle-deals', params)
}

export function getBundleDealDetail(id) {
  return get(`/api/v1/bundle-deals/${id}`)
}

export default {
  getBundleDeals,
  getBundleDealDetail,
}
