import { get, post } from '@/utils/request'

export function testWooConnection() {
  return get('/api/v1/woocommerce/test')
}

export function syncProducts() {
  return post('/api/v1/woocommerce/sync/products')
}

export function syncOrders() {
  return post('/api/v1/woocommerce/sync/orders')
}

export function getWooProducts(page = 1, perPage = 20) {
  return get('/api/v1/woocommerce/products', { page, perPage })
}

export function getWooCategories() {
  return get('/api/v1/woocommerce/categories')
}

export function getWooOrders(page = 1, perPage = 20) {
  return get('/api/v1/woocommerce/orders', { page, perPage })
}

export default {
  testWooConnection,
  syncProducts,
  syncOrders,
  getWooProducts,
  getWooCategories,
  getWooOrders,
}
