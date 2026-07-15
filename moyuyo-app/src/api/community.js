import { get, post, del } from '@/utils/request'

export function getCommunityPosts(params = {}) {
  return get('/api/v1/community/posts', params)
}

export function getPostDetail(id) {
  return get(`/api/v1/community/posts/${id}`)
}

export function createPost(content, images, topic) {
  const params = { content }
  if (images && images.length) params.images = images
  if (topic) params.topic = topic
  return post('/api/v1/community/posts', params)
}

export function getMyPosts(params = {}) {
  return get('/api/v1/community/posts/mine', params)
}

export function likePost(id) {
  return post(`/api/v1/community/posts/${id}/like`)
}

export function unlikePost(id) {
  return del(`/api/v1/community/posts/${id}/like`)
}

export function addComment(postId, content, parentId) {
  const params = { content }
  if (parentId) params.parentId = parentId
  return post(`/api/v1/community/posts/${postId}/comments`, params)
}

export default {
  getCommunityPosts,
  getPostDetail,
  createPost,
  getMyPosts,
  likePost,
  unlikePost,
  addComment,
}
