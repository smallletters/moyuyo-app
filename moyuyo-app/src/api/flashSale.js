import { get, post } from '@/utils/request'

export function getFlashSales(params = {}) {
  return get('/api/v1/flash-sales', params)
}

export function getFlashSaleDetail(id) {
  return get(`/api/v1/flash-sales/${id}`)
}

export function buyFlashSale(id, quantity = 1) {
  return post(`/api/v1/flash-sales/${id}/buy`, { quantity })
}

export default {
  getFlashSales,
  getFlashSaleDetail,
  buyFlashSale,
}
