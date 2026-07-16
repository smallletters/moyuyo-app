import { get, post, del } from '@/utils/request'

export function getPetDiaries(petId, params = {}) {
  return get(`/api/v1/pets/${petId}/diaries`, params)
}

export function getPetDiaryDetail(petId, id) {
  return get(`/api/v1/pets/${petId}/diaries/${id}`)
}

export function createPetDiary(petId, data) {
  return post(`/api/v1/pets/${petId}/diaries`, data)
}

export function deletePetDiary(petId, id) {
  return del(`/api/v1/pets/${petId}/diaries/${id}`)
}

export default {
  getPetDiaries,
  getPetDiaryDetail,
  createPetDiary,
  deletePetDiary,
}
