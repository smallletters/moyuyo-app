import { get, post } from '@/utils/request'

export function getPetWeights(petId) {
  return get(`/api/v1/pets/${petId}/weights`)
}

export function getPetWeightChart(petId) {
  return get(`/api/v1/pets/${petId}/weights/chart`)
}

export function createPetWeight(petId, data) {
  return post(`/api/v1/pets/${petId}/weights`, data)
}

export default {
  getPetWeights,
  getPetWeightChart,
  createPetWeight,
}
