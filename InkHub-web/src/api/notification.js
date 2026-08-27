import request from '@/utils/request'

// 我的通知（分页）
export function getNotifications(pageNum = 1, pageSize = 10) {
  return request.get('/notifications', { params: { pageNum, pageSize } })
}

// 未读数
export function getUnreadCount() {
  return request.get('/notifications/unread-count')
}

// 单条已读
export function readNotification(id) {
  return request.put(`/notifications/${id}/read`)
}

// 全部已读
export function readAllNotifications() {
  return request.put('/notifications/read-all')
}
