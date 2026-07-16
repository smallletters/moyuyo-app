import { get, post } from '@/utils/request'

export function getLotteries() {
  return get('/api/v1/lotteries')
}

export function spin(id) {
  return post(`/api/v1/lotteries/${id}/spin`)
}

export function getLotteryHistory() {
  return get('/api/v1/lotteries/history')
}

export function getLotteryStats() {
  return get('/api/v1/lotteries/stats')
}

export default {
  getLotteries,
  spin,
  getLotteryHistory,
  getLotteryStats,
}
