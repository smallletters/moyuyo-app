import { get, post, del } from '@/utils/request'

export function getRecycleBinOrders() {
  return get('/api/v1/recycle-bin')
}

export function restoreOrder(id) {
  return post(`/api/v1/recycle-bin/${id}/restore`)
}

export function deleteOrderPermanent(id) {
  return del(`/api/v1/recycle-bin/${id}`)
}

export function clearRecycleBin() {
  return del('/api/v1/recycle-bin/clear')
}

export default {
  getRecycleBinOrders,
  restoreOrder,
  deleteOrderPermanent,
  clearRecycleBin,
}
