import request from '@/utils/request'

// 分类列表（带文章数）
export function getCategories() {
  return request.get('/categories')
}

// 标签列表
export function getTags() {
  return request.get('/tags')
}
