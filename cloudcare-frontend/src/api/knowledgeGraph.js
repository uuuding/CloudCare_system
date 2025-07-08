import request from '@/utils/request'

// 获取完整知识图谱数据
export function getFullGraph() {
  return request({
    url: '/knowledge-graph/full-graph',
    method: 'get'
  })
}

// 获取所有疾病
export function getAllDiseases() {
  return request({
    url: '/knowledge-graph/diseases',
    method: 'get'
  })
}


// 获取所有症状
export function getAllSymptoms() {
  return request({
    url: '/knowledge-graph/symptoms',
    method: 'get'
  })
}

// 获取所有药物
export function getAllMedicines() {
  return request({
    url: '/knowledge-graph/medicines',
    method: 'get'
  })
}

// 根据名称查找疾病
export function findDiseaseByName(name) {
  return request({
    url: `/knowledge-graph/disease/${name}`,
    method: 'get'
  })
}

// 根据名称查找药物
export function findMedicineByName(name) {
  return request({
    url: `/knowledge-graph/medicine/${name}`,
    method: 'get'
  })
}

// 根据症状查找疾病
export function findDiseasesBySymptom(symptomName) {
  return request({
    url: `/knowledge-graph/diseases/by-symptom/${symptomName}`,
    method: 'get'
  })
}

// 根据疾病查找药物
export function findMedicinesByDisease(diseaseName) {
  return request({
    url: `/knowledge-graph/medicines/by-disease/${diseaseName}`,
    method: 'get'
  })
}

// 添加疾病
export function addDisease(disease) {
  return request({
    url: '/knowledge-graph/disease',
    method: 'post',
    data: disease
  })
}

// 添加症状
export function addSymptom(symptom) {
  return request({
    url: '/knowledge-graph/symptom',
    method: 'post',
    data: symptom
  })
}

// 添加药物
export function addMedicine(medicine) {
  return request({
    url: '/knowledge-graph/medicine',
    method: 'post',
    data: medicine
  })
}

// 更新疾病
export function updateDisease(name, disease) {
  return request({
    url: `/knowledge-graph/disease/${name}`,
    method: 'put',
    data: disease
  })
}

// 更新症状
export function updateSymptom(name, symptom) {
  return request({
    url: `/knowledge-graph/symptom/${name}`,
    method: 'put',
    data: symptom
  })
}

// 更新药物
export function updateMedicine(name, medicine) {
  return request({
    url: `/knowledge-graph/medicine/${name}`,
    method: 'put',
    data: medicine
  })
}

// 删除疾病
export function deleteDisease(name) {
  return request({
    url: `/knowledge-graph/disease/${name}`,
    method: 'delete'
  })
}

// 删除症状
export function deleteSymptom(name) {
  return request({
    url: `/knowledge-graph/symptom/${name}`,
    method: 'delete'
  })
}

// 删除药物
export function deleteMedicine(name) {
  return request({
    url: `/knowledge-graph/medicine/${name}`,
    method: 'delete'
  })
}

// 添加疾病-症状关系
export function addSymptomToDisease(symptomName, diseaseName) {
  return request({
    url: '/knowledge-graph/disease-symptom',
    method: 'post',
    params: {
      symptomName,
      diseaseName
    }
  })
}

// 添加药物-疾病关系
export function addMedicineToDisease(medicineName, diseaseName) {
  return request({
    url: '/knowledge-graph/medicine-disease',
    method: 'post',
    params: {
      medicineName,
      diseaseName
    }
  })
}

// 删除疾病-症状关系
export function removeSymptomFromDisease(symptomName, diseaseName) {
  return request({
    url: '/knowledge-graph/disease-symptom',
    method: 'delete',
    params: {
      symptomName,
      diseaseName
    }
  })
}

// 删除药物-疾病关系
export function removeMedicineFromDisease(medicineName, diseaseName) {
  return request({
    url: '/knowledge-graph/medicine-disease',
    method: 'delete',
    params: {
      medicineName,
      diseaseName
    }
  })
}

// ==================== GCN 相关接口 ====================

// 执行GCN特征传播
export function performGCNPropagation(params = {}) {
  return request({
    url: '/knowledge-graph/gcn/propagate',
    method: 'post',
    params: {
      alpha: params.alpha || 0.6,
      iterations: params.iterations || 3
    }
  })
}

// 计算节点相似度
export function calculateNodeSimilarity(params) {
  return request({
    url: '/knowledge-graph/gcn/similarity',
    method: 'post',
    params: {
      nodeId1: params.nodeId1,
      nodeId2: params.nodeId2,
      alpha: params.alpha || 0.6,
      iterations: params.iterations || 3
    }
  })
}

// 基于症状推荐药物
export function recommendMedicinesBySymptom(params) {
  return request({
    url: '/knowledge-graph/gcn/recommend-medicines',
    method: 'post',
    params: {
      symptomId: params.symptomId,
      topK: params.topK || 5,
      alpha: params.alpha || 0.6,
      iterations: params.iterations || 3
    }
  })
}