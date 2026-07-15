import { get, post } from '@/utils/request'

export function getPointsLog(params = {}) {
  return get('/api/v1/points/log', params)
}

export function getPointsBalance() {
  return get('/api/v1/points/balance')
}

export function checkin() {
  return post('/api/v1/points/checkin')
}

export default {
  getPointsLog,
  getPointsBalance,
  checkin,
}
