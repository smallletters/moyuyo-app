import { get, post } from '@/utils/request'

export function getPetOutfits(petId) {
  return get(`/api/v1/pets/${petId}/dresser`)
}

export function equipOutfit(petId, id) {
  return post(`/api/v1/pets/${petId}/dresser/${id}/equip`)
}

export default {
  getPetOutfits,
  equipOutfit,
}
