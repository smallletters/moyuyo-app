import { get, post } from '@/utils/request'

export function getInvoices(params = {}) {
  return get('/api/v1/invoices', params)
}

export function applyInvoice(data) {
  return post('/api/v1/invoices', data)
}

export function getInvoiceDetail(id) {
  return get(`/api/v1/invoices/${id}`)
}

export default {
  getInvoices,
  applyInvoice,
  getInvoiceDetail,
}
