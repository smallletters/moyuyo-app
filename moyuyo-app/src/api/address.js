import { get, post, put, del } from '@/utils/request'

export function getAddressList() {
  return get('/api/v1/addresses')
}

export function getAddressDetail(id) {
  return get(`/api/v1/addresses/${id}`)
}

export function createAddress(data) {
  return post('/api/v1/addresses', data)
}

export function updateAddress(id, data) {
  return put(`/api/v1/addresses/${id}`, data)
}

export function deleteAddress(id) {
  return del(`/api/v1/addresses/${id}`)
}

export function setDefaultAddress(id) {
  return put(`/api/v1/addresses/${id}/default`)
}

export function validateAddress(id) {
  return get(`/api/v1/addresses/${id}/validate`)
}

export default {
  getAddressList,
  getAddressDetail,
  createAddress,
  updateAddress,
  deleteAddress,
  setDefaultAddress,
  validateAddress,
}
