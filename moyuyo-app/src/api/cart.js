import { get, post, put, del } from '@/utils/request'

export function getCart() {
  return get('/api/v1/cart')
}

export function addItem(skuId, quantity = 1) {
  return post('/api/v1/cart', { skuId, quantity })
}

export function updateQuantity(skuId, quantity) {
  return put(`/api/v1/cart/${skuId}`, { quantity })
}

export function removeItem(skuId) {
  return del(`/api/v1/cart/${skuId}`)
}

export function toggleCheck(skuId, selected) {
  return put(`/api/v1/cart/check/${skuId}`, { selected })
}

export function toggleCheckAll(selected) {
  return put('/api/v1/cart/check-all', { selected })
}

export function clearCart() {
  return del('/api/v1/cart')
}

export function checkout(addressId, remark, couponId) {
  return post('/api/v1/cart/checkout', { addressId, remark, couponId })
}

export function getFavorites() {
  return get('/api/v1/favorites')
}

export function addFavorite(productId, skuId, groupId) {
  return post('/api/v1/favorites', { productId, skuId, groupId })
}

export function removeFavorite(productId, skuId) {
  return del('/api/v1/favorites', { productId, skuId })
}

export default {
  getCart,
  addItem,
  updateQuantity,
  removeItem,
  toggleCheck,
  toggleCheckAll,
  clearCart,
  checkout,
  getFavorites,
  addFavorite,
  removeFavorite,
}
