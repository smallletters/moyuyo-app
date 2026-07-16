import { get, post } from '@/utils/request'

export function getAfterSales(params = {}) {
  return get('/api/v1/after-sales', params)
}

export function getAfterSaleDetail(id) {
  return get(`/api/v1/after-sales/${id}`)
}

export function createAfterSale(data) {
  return post('/api/v1/after-sales', data)
}

export default {
  getAfterSales,
  getAfterSaleDetail,
  createAfterSale,
}
