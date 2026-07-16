import { get } from '@/utils/request'

export function getLiveRooms(params = {}) {
  return get('/api/v1/live-rooms', params)
}

export function getLiveRoomDetail(id) {
  return get(`/api/v1/live-rooms/${id}`)
}

export function getLiveRoomProducts(id) {
  return get(`/api/v1/live-rooms/${id}/products`)
}

export default {
  getLiveRooms,
  getLiveRoomDetail,
  getLiveRoomProducts,
}
