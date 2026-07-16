import { get, post } from '@/utils/request'

export function submitFeedback(data) {
  return post('/api/v1/feedback', data)
}

export function getMyFeedback(params = {}) {
  return get('/api/v1/feedback', params)
}

export default {
  submitFeedback,
  getMyFeedback,
}
