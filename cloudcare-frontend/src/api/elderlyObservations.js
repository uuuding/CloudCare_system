import request from '@/utils/request'

// 查询所有体检记录
export function getAllObservations() {
  return request({
    url: '/elderly-observations/all',
    method: 'get'
  })
}

// 根据老人ID查询所有体检记录，按时间倒序
export function getObservationsByElderlyId(elderlyId) {
  return request({
    url: `/elderly-observations/by-elderly/${elderlyId}`,
    method: 'get'
  })
}

// 根据时间范围查询老人体检记录
export function getObservationsByTimeRange(elderlyId, startTime, endTime) {
  return request({
    url: `/elderly-observations/by-elderly/${elderlyId}/range`,
    method: 'get',
    params: { startTime, endTime }
  })
}

// 新增体检记录，包含观察地点
export function addObservation(data) {
  return request({
    url: '/elderly-observations/add',
    method: 'post',
    data
  })
}

// 更新体检记录，包含观察地点
export function updateObservation(data) {
  return request({
    url: '/elderly-observations/update',
    method: 'put',
    data
  })
}

// 删除体检记录
export function deleteObservation(id) {
  return request({
    url: `/elderly-observations/delete/${id}`,
    method: 'delete'
  })
}

// 批量导入健康记录
export function importObservations(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/elderly-observations/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 下载导入模板
export function downloadTemplate() {
  return request({
    url: '/elderly-observations/template',
    method: 'get',
    responseType: 'blob'
  })
}