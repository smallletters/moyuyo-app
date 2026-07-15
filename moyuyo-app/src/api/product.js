import { get } from '@/utils/request'

export function getProductList(params = {}) {
  return get('/api/v1/products', params)
}

export function getProductDetail(id) {
  return get(`/api/v1/products/${id}`)
}

export function getCategoryList() {
  return get('/api/v1/categories')
}

export function searchProducts(keyword, params = {}) {
  return get('/api/v1/products', { keyword, ...params })
}

export function getCategoryChildren(parentId) {
  return get(`/api/v1/categories/${parentId}/children`)
}

export default {
  getProductList,
  getProductDetail,
  getCategoryList,
  searchProducts,
  getCategoryChildren,
}
