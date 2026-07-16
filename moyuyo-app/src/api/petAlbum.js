import { get, post, del } from '@/utils/request'

export function getPetAlbums(petId) {
  return get(`/api/v1/pets/${petId}/albums`)
}

export function createPetAlbum(petId, data) {
  return post(`/api/v1/pets/${petId}/albums`, data)
}

export function deletePetAlbum(petId, id) {
  return del(`/api/v1/pets/${petId}/albums/${id}`)
}

export default {
  getPetAlbums,
  createPetAlbum,
  deletePetAlbum,
}
