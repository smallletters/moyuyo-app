import { get, post } from '@/utils/request'

export function getMissions(params = {}) {
  return get('/api/v1/missions', params)
}

export function claimMission(id) {
  return post(`/api/v1/missions/${id}/claim`)
}

export function getMissionStats() {
  return get('/api/v1/missions/stats')
}

export default {
  getMissions,
  claimMission,
  getMissionStats,
}
