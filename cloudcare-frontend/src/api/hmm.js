import request from '@/utils/request'

/**
 * 预测下一状态
 * @param {Object} data HMM请求数据
 * @param {Array} data.observation 身体指标数组 [体温, 血压, BMI, 心率, 睡眠] (0/1/2)
 * @param {Array} data.diseaseCount 三类疾病数量 [A类, B类, C类]
 * @param {String} data.currentState 当前状态 (健康/注意/异常)
 * @returns {Promise}
 */
export function predictNextState(data) {
  return request({
    url: '/api/hmm/predict',
    method: 'post',
    data
  })
}

/**
 * 获取状态评分
 * @param {Object} data HMM请求数据
 * @param {Array} data.observation 身体指标数组 [体温, 血压, BMI, 心率, 睡眠] (0/1/2)
 * @param {Array} data.diseaseCount 三类疾病数量 [A类, B类, C类]
 * @param {String} data.currentState 当前状态 (健康/注意/异常)
 * @returns {Promise}
 */
export function getStateScores(data) {
  return request({
    url: '/api/hmm/scores',
    method: 'post',
    data
  })
}