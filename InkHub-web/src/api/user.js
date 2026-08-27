import request from '@/utils/request'

// 我的信息
export function getMe() {
  return request.get('/user/me')
}

// 修改资料：传 { nickname } 或 { avatar } 或都传
export function updateProfile(data) {
  return request.put('/user/profile', data)
}
