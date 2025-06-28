import request from '@/utils/request'

export function addCaseEntry(data) {
  return request({
    url: '/elderly-profile/addc',
    method: 'post',
    data: data
  });
}


/**
 * 获取所有老人档案
 * @returns {Promise}
 */
export function getAllElderlyProfiles() {
  return request({
    url: '/elderly-profile/index',
    method: 'get'
  })
}

/**
 * 获取老人既往病史
 * @param {Number} elderlyId 老人ID
 * @returns {Promise}
 */
export function getChronicDiseasesByElderlyId(elderlyId) {
  return request({
    url: `/elderly-profile/chronic-diseases/${elderlyId}`,
    method: 'get'
  })
}

/**
 * 根据条件查询老人档案
 * @param {Object} params 查询条件
 * @returns {Promise}
 */
export function searchElderlyProfiles(params) {
  return request({
    url: '/elderly-profile/search',
    method: 'get',
    params
  })
}

/**
 * 更新老人档案
 * @param {Object} data 老人档案信息
 * @returns {Promise}
 */
export function updateElderlyProfile(data) {
  return request({
    url: '/elderly-profile/update',
    method: 'put',
    data:data
  })
}

export function addElderlyProfile(data) {
  return request({
    url: '/elderly-profile/add',
    method: 'post',
    data: data
  })
}

/**
 * 删除老人档案
 * @param {number} id 老人档案ID
 * @returns {Promise}
 */
export function deleteElderlyProfile(id) {
  return request({
    url: `/elderly-profile/delete/${id}`,
    method: 'delete'
  })
}