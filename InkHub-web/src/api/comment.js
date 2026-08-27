import request from '@/utils/request'

// 文章评论列表
export function getComments(articleId) {
  return request.get('/comments', { params: { articleId } })
}

// 发表评论 / 回复（parentId 可选）
// 后端 POST /api/comments 用 @RequestBody String 收纯文本，axios 传字符串本身
export function addComment(articleId, content, parentId = null) {
  return request.post('/comments', content, {
    params: { articleId, parentId },
    headers: { 'Content-Type': 'text/plain' },
  })
}

// 删除自己的评论
export function deleteComment(id) {
  return request.delete(`/comments/${id}`)
}
