import { get, post, del } from '@/utils/request'

export function createOrder(data) {
  return post('/api/v1/orders', data)
}

export function getOrderList(params = {}) {
  return get('/api/v1/orders', params)
}

export function getOrderDetail(id) {
  return get(`/api/v1/orders/${id}`)
}

export function cancelOrder(id, reason) {
  return post(`/api/v1/orders/${id}/cancel`, { reason })
}

export function confirmReceived(id) {
  return post(`/api/v1/orders/${id}/confirm`)
}

export function deleteOrder(id) {
  return del(`/api/v1/orders/${id}`)
}

export function createPayment(data) {
  return post('/api/v1/payments/create', data)
}

export function getLogistics(orderId) {
  return get(`/api/v1/logistics/order/${orderId}`)
}

export function confirmReceivedLogistics(orderId) {
  return post(`/api/v1/logistics/${orderId}/confirm`)
}

export function applyRefund(data) {
  return post('/api/v1/refunds', data)
}

export function getRefundDetail(id) {
  return get(`/api/v1/refunds/${id}`)
}

export function getMyRefunds(params = {}) {
  return get('/api/v1/refunds/mine', params)
}

export function getShippingRate(country, weight) {
  return get('/api/v1/shipping/estimate', { country, weight })
}

export default {
  createOrder,
  getOrderList,
  getOrderDetail,
  cancelOrder,
  confirmReceived,
  deleteOrder,
  createPayment,
  getLogistics,
  confirmReceivedLogistics,
  applyRefund,
  getRefundDetail,
  getMyRefunds,
  getShippingRate,
}
