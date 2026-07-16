import { get, post } from '@/utils/request'

export function getGiftCards(params = {}) {
  return get('/api/v1/gift-cards', params)
}

export function bindGiftCard(cardNo, pin) {
  return post('/api/v1/gift-cards/bind', { cardNo, pin })
}

export function purchaseGiftCard(amount, quantity) {
  return post('/api/v1/gift-cards/purchase', { amount, quantity })
}

export function getGiftCardTransactions(id) {
  return get(`/api/v1/gift-cards/${id}/transactions`)
}

export default {
  getGiftCards,
  bindGiftCard,
  purchaseGiftCard,
  getGiftCardTransactions,
}
