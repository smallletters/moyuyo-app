import { get, post } from '@/utils/request'

export function getBargains(params = {}) {
  return get('/api/v1/bargains', params)
}

export function getBargainDetail(id) {
  return get(`/api/v1/bargains/${id}`)
}

export function helpBargain(id) {
  return post(`/api/v1/bargains/${id}/help`)
}

export function getBargainHelpers(id) {
  return get(`/api/v1/bargains/${id}/helpers`)
}

export default {
  getBargains,
  getBargainDetail,
  helpBargain,
  getBargainHelpers,
}
