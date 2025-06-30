import request from '@/utils/request'

/**
 * 上传头像
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function uploadAvatar(formData) {
  return request({
    url: '/upload/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传文件
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function uploadFile(formData) {
  return request({
    url: '/upload/file',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除文件
 * @param {String} fileUrl 文件URL
 * @returns {Promise}
 */
export function deleteFile(fileUrl) {
  return request({
    url: '/upload/delete',
    method: 'delete',
    params: { fileUrl }
  })
}