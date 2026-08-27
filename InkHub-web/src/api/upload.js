import request from '@/utils/request'

// 上传图片，返回 { url } 或直接返回 url 字符串（后端返回 R<String>，拦截器已解包成字符串）
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
