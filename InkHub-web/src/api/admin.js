import request from '@/utils/request'

// ===== 文章管理 =====
// 全量文章（可按状态过滤）
export function getAdminArticles(status) {
  return request.get('/admin/articles', { params: { status } })
}
// 下架
export function offArticle(id) {
  return request.put(`/admin/articles/${id}/off`)
}
// 恢复发布
export function onArticle(id) {
  return request.put(`/admin/articles/${id}/on`)
}
// 置顶 / 取消置顶
export function topArticle(id, top) {
  return request.put(`/admin/articles/${id}/top`, null, { params: { top } })
}
// 删除任意文章
export function deleteArticleByAdmin(id) {
  return request.delete(`/admin/articles/${id}`)
}

// ===== 评论管理 =====
// 全部评论（联表文章标题 + 昵称）
export function getAdminComments() {
  return request.get('/admin/comments')
}
// 删除任意评论
export function deleteCommentByAdmin(id) {
  return request.delete(`/admin/comments/${id}`)
}

// ===== 分类 / 标签维护 =====
export function addCategory(data) {
  return request.post('/admin/categories', data)
}
export function updateCategory(id, data) {
  return request.put(`/admin/categories/${id}`, data)
}
export function deleteCategory(id) {
  return request.delete(`/admin/categories/${id}`)
}
export function addTag(data) {
  return request.post('/admin/tags', data)
}
export function deleteTag(id) {
  return request.delete(`/admin/tags/${id}`)
}

// ===== 统计 =====
export function getStats() {
  return request.get('/admin/stats/overview')
}
