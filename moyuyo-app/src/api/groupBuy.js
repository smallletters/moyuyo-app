import { get, post } from '@/utils/request'

export function getGroupBuys(params = {}) {
  return get('/api/v1/group-buys', params)
}

export function getGroupBuyDetail(id) {
  return get(`/api/v1/group-buys/${id}`)
}

export function joinGroupBuy(id) {
  return post(`/api/v1/group-buys/${id}/join`)
}

export function getGroupBuyMembers(id) {
  return get(`/api/v1/group-buys/${id}/members`)
}

export default {
  getGroupBuys,
  getGroupBuyDetail,
  joinGroupBuy,
  getGroupBuyMembers,
}
