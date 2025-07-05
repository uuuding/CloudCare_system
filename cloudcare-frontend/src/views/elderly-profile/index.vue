<template>
  <div class="elderly-profile-container">
    <el-card class="profile-card" shadow="hover">
      <div class="header">
        <h2>老人档案管理</h2>
      </div>
      <div class="search-bar" style="margin-bottom: 20px; display: flex; gap: 10px; align-items: center;">
        <el-input v-model="searchQuery.name" placeholder="输入姓名" style="width: 200px;" clearable />
        <el-input v-model="searchQuery.age" placeholder="输入年龄" style="width: 200px;" clearable />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <!-- 导入按钮 -->
        <el-button type="success" @click="openImportDialog">导入老人档案</el-button>
        <!-- 批量新增按钮 -->
        <el-button type="warning" @click="openBatchAddDialog">批量新增</el-button>
        <!-- 新增按钮：录入病例 -->
        <el-button type="primary" @click="openCaseEntryDialog">录入病例</el-button>
      </div>
      <el-table :data="elderlyProfiles" style="width: 100%" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="180" />
        <el-table-column prop="age" label="年龄" width="100" />
        <el-table-column prop="gender" label="性别" width="100" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button @click="openEditDialog(scope.row)" type="primary" size="small" style="margin-right: 6px;">编辑</el-button>
            <el-button @click="handleDelete(scope.row.id)" type="danger" size="small" style="margin-right: 6px;">删除</el-button>
            <el-button @click="openChronicDialog(scope.row)" type="info" size="small" style="background-color: #606266; border-color: #606266; color: #fff;">查看既往病史</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" title="编辑老人档案" width="600px" :before-close="closeEditDialog">
      <el-form :model="editProfile" :rules="formRules" label-width="100px" ref="formRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editProfile.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="editProfile.age" type="number" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="editProfile.gender" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="既往病史" prop="chronicDiseases">
          <el-input
              type="textarea"
              v-model="editProfile.chronicDiseases"
              placeholder="请输入既往病史，多个用分号分隔"
              rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeEditDialog">取消</el-button>
          <el-button type="primary" @click="handleEditSave">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 既往病史弹窗 -->
    <el-dialog v-model="chronicDialogVisible" title="老人既往病史" width="600px" :before-close="closeChronicDialog">
      <el-table :data="chronicDiseases" style="width: 100%" border stripe>
        <el-table-column prop="diseaseName" label="疾病名称" />
        <el-table-column prop="diagnosisDate" label="诊断日期" />
        <el-table-column prop="notes" label="备注" />
      </el-table>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeChronicDialog">关闭</el-button>
      </span>
      </template>
    </el-dialog>

    <!-- 导入弹窗 -->
    <el-dialog v-model="importDialogVisible" title="导入老人档案" width="600px">
      <el-form :model="importProfile" :rules="importFormRules" label-width="100px" ref="importFormRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="importProfile.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="importProfile.age" type="number" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="importProfile.gender" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="既往病史" prop="chronicDiseases">
          <el-input
              type="textarea"
              v-model="importProfile.chronicDiseases"
              placeholder="请输入既往病史，多个用分号分隔"
              rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeImportDialog">取消</el-button>
          <el-button type="primary" @click="handleImportSave">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="caseEntryDialogVisible" title="录入病例" width="600px">
      <el-form :model="caseEntry" :rules="caseEntryFormRules" label-width="100px" ref="caseEntryFormRef">

        <!-- 选择老人 -->
        <el-form-item label="选择老人" prop="elderlyId">
          <el-select v-model="caseEntry.elderlyId" placeholder="请选择老人" filterable>
            <el-option 
              v-for="elderly in elderlyProfiles" 
              :key="elderly.id" 
              :label="`${elderly.name} (ID: ${elderly.id})`" 
              :value="elderly.id"
            />
          </el-select>
        </el-form-item>

        <!-- 疾病名称 -->
        <el-form-item label="疾病名称" prop="diseaseName">
          <el-input v-model="caseEntry.diseaseName" placeholder="请输入疾病名称" />
        </el-form-item>

        <!-- 疾病类别 -->
        <el-form-item label="疾病类别" prop="diseaseCategory">
          <el-select v-model="caseEntry.diseaseCategory" placeholder="请选择疾病类别">
            <el-option label="A" value="A" />
            <el-option label="B" value="B" />
            <el-option label="C" value="C" />
          </el-select>
        </el-form-item>

        <!-- 诊断日期 -->
        <el-form-item label="诊断日期" prop="diagnosisDate">
          <el-date-picker v-model="caseEntry.diagnosisDate" type="date" placeholder="选择日期" />
        </el-form-item>

      </el-form>

      <template #footer>
    <span class="dialog-footer">
      <el-button @click="closeCaseEntryDialog">取消</el-button>
      <el-button type="primary" @click="handleCaseEntrySave">确定</el-button>
    </span>
      </template>
    </el-dialog>

    <!-- 批量新增弹窗 -->
    <el-dialog v-model="batchAddDialogVisible" title="批量新增老人档案（含病例信息）" width="1400px" :before-close="closeBatchAddDialog">
      <div class="batch-add-container">
        <div class="batch-add-header">
          <el-button type="primary" @click="addBatchRow">添加一行</el-button>
          <el-button type="danger" @click="clearAllRows">清空所有</el-button>
          <span class="batch-tip">提示：最多可同时添加20条记录，可选择性添加病例信息</span>
        </div>
        
        <el-table :data="batchProfiles" style="width: 100%; margin-top: 20px;" border stripe max-height="500">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column label="姓名" width="150">
            <template #default="{ row, $index }">
              <el-input v-model="row.name" placeholder="请输入姓名" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="年龄" width="100">
            <template #default="{ row, $index }">
              <el-input-number v-model="row.age" :min="1" :max="150" placeholder="年龄" size="small" style="width: 100%;" />
            </template>
          </el-table-column>
          <el-table-column label="性别" width="120">
            <template #default="{ row, $index }">
              <el-select v-model="row.gender" placeholder="选择性别" size="small" style="width: 100%;">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
                <el-option label="其他" value="其他" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="病例信息" width="600">
            <template #default="{ row, $index }">
              <div class="disease-container">
                <div v-if="row.chronicDiseases.length === 0" class="no-disease">
                  <span style="color: #999; font-size: 12px;">暂无病例</span>
                  <el-button type="primary" size="small" @click="addDiseaseToProfile($index)" style="margin-left: 10px;">添加病例</el-button>
                </div>
                <div v-else>
                  <div v-for="(disease, diseaseIndex) in row.chronicDiseases" :key="diseaseIndex" class="disease-row" style="display: flex; align-items: center; margin-bottom: 8px;">
                    <el-input v-model="disease.diseaseName" placeholder="疾病名称" size="small" style="width: 120px; margin-right: 8px;" />
                    <el-select v-model="disease.diseaseCategory" placeholder="类别" size="small" style="width: 80px; margin-right: 8px;">
                      <el-option label="A类" value="A" />
                      <el-option label="B类" value="B" />
                      <el-option label="C类" value="C" />
                    </el-select>
                    <el-date-picker v-model="disease.diagnosisDate" type="date" placeholder="诊断日期" size="small" style="width: 120px; margin-right: 8px;" />
                    <el-button type="danger" size="small" @click="removeDiseaseFromProfile($index, diseaseIndex)" style="margin-right: 4px;">删除</el-button>
                  </div>
                  <el-button type="primary" size="small" @click="addDiseaseToProfile($index)" style="margin-top: 4px;">+ 添加病例</el-button>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ row, $index }">
              <el-button type="danger" size="small" @click="removeBatchRow($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeBatchAddDialog">取消</el-button>
          <el-button type="primary" @click="handleBatchSave" :loading="batchSaving">确定提交</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElMessageBox, ElDialog, ElButton, ElInput, ElSelect, ElOption, ElForm, ElFormItem } from "element-plus"
import {
  getAllElderlyProfiles,
  searchElderlyProfiles,
  updateElderlyProfile,
  deleteElderlyProfile,
  addElderlyProfile,
  getChronicDiseasesByElderlyId,
  addCaseEntry,
  batchAddElderlyProfiles
} from "@/api/elderlyProfile"

const elderlyProfiles = ref([])
const searchQuery = reactive({ name: "", age: "" })
const editProfile = reactive({ id: null, name: "", age: "", gender: "", chronicDiseases: "" })
const importProfile = reactive({ name: "", age: "", gender: "", chronicDiseases: "" }) // 用于导入数据的对象
const dialogVisible = ref(false)
const importDialogVisible = ref(false) // 控制导入弹窗的显示
const caseEntryDialogVisible = ref(false); // 录入病例弹窗

// 批量新增相关数据
const batchAddDialogVisible = ref(false) // 控制批量新增弹窗的显示
const batchProfiles = ref([]) // 批量新增的老人档案数据
const batchSaving = ref(false) // 批量保存状态
// 录入病例数据
const caseEntry = reactive({
  elderlyId: null,  // 老人ID
  diseaseName: "",  // 疾病名称
  diseaseCategory: "A",  // 疾病类别，默认A
  diagnosisDate: ""  // 诊断日期
});

// 新增既往病史弹窗相关状态
const chronicDialogVisible = ref(false)
const chronicDiseases = ref([])

const formRules = {
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  age: [{ required: true, message: "请输入年龄", trigger: "blur" }],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  chronicDiseases: []
}

const importFormRules = {
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  age: [{ required: true, message: "请输入年龄", trigger: "blur" }],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  chronicDiseases: []
}

const caseEntryFormRules = {
  elderlyId: [{ required: true, message: "请输入老人ID", trigger: "blur" }],
  diseaseName: [{ required: true, message: "请输入疾病名称", trigger: "blur" }],
  diseaseCategory: [{ required: true, message: "请选择疾病类别", trigger: "change" }],
  diagnosisDate: [{ required: true, message: "请选择诊断日期", trigger: "change" }]
};

const loadElderlyProfiles = async () => {
  try {
    const response = await getAllElderlyProfiles()
    elderlyProfiles.value = response.data || []
  } catch (error) {
    ElMessage.error("加载老人档案失败")
  }
}

const handleSearch = async () => {
  try {
    const response = await searchElderlyProfiles(searchQuery)
    elderlyProfiles.value = response.data || []
  } catch (error) {
    ElMessage.error("搜索失败")
  }
}

const resetSearch = () => {
  searchQuery.name = ""
  searchQuery.age = ""
  loadElderlyProfiles()
}

const openCaseEntryDialog = () => {
  caseEntryDialogVisible.value = true;
};

const openEditDialog = (profile) => {
  editProfile.id = profile.id
  editProfile.name = profile.name
  editProfile.age = profile.age
  editProfile.gender = profile.gender
  // 处理既往病史数组转字符串显示
  editProfile.chronicDiseases = profile.chronicDiseases ? profile.chronicDiseases.join('; ') : ''
  dialogVisible.value = true
}

const closeEditDialog = () => {
  dialogVisible.value = false
}

const handleEditSave = async () => {
  try {
    // 处理既往病史字符串，转换为数组或后端需要的格式
    const chronicList = editProfile.chronicDiseases
      ? editProfile.chronicDiseases.split(';').map(item => item.trim()).filter(item => item)
      : []
    const payload = { ...editProfile, chronicDiseases: chronicList }
    await updateElderlyProfile(payload)
    ElMessage.success("更新成功")
    closeEditDialog()
    await loadElderlyProfiles()
  } catch (error) {
    ElMessage.error("更新失败")
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm(
      '此操作将永久删除该老人档案，是否继续？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    const response = await deleteElderlyProfile(id)
    if (response.success && response.data) {
      ElMessage.success(response.message || "删除成功")
      await loadElderlyProfiles()
    } else {
      ElMessage.error(response.message || "删除失败")
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error("删除失败: " + (error.message || error))
    }
  }
}

// 关闭录入病例弹窗
const closeCaseEntryDialog = () => {
  caseEntryDialogVisible.value = false;
};

const handleCaseEntrySave = async () => {
  if (caseEntry.elderlyId && caseEntry.diseaseName && caseEntry.diseaseCategory && caseEntry.diagnosisDate) {

    if (caseEntry.diagnosisDate instanceof Date) {
      caseEntry.diagnosisDate = caseEntry.diagnosisDate.toISOString().split("T")[0];  // 转换为 'YYYY-MM-DD' 格式
      console.log(caseEntry.diagnosisDate)
    }

    try {
      await addCaseEntry(caseEntry);  // 提交到后端
      ElMessage.success("病例录入成功");
      closeCaseEntryDialog();
    } catch (error) {
      ElMessage.error("病例录入失败");
    }
  } else {
    ElMessage.warning("请填写所有必填项");
  }
};


// 导入功能相关
const openImportDialog = () => {
  importDialogVisible.value = true
}

const closeImportDialog = () => {
  importDialogVisible.value = false
}

const handleImportSave = async () => {
  try {
    // 处理既往病史字符串，转换为数组或后端需要的格式
    const chronicList = importProfile.chronicDiseases
      ? importProfile.chronicDiseases.split(';').map(item => item.trim()).filter(item => item)
      : []
    const payload = { ...importProfile, chronicDiseases: chronicList }
    await addElderlyProfile(payload) // 调用API保存导入的数据
    ElMessage.success("导入成功")
    closeImportDialog()
    await loadElderlyProfiles()
  } catch (error) {
    ElMessage.error("导入失败")
  }
}

// 新增查看既往病史相关方法
const openChronicDialog = async (profile) => {
  try {
    const response = await getChronicDiseasesByElderlyId(profile.id)
    chronicDiseases.value = response.data || []
    chronicDialogVisible.value = true
  } catch (error) {
    ElMessage.error("获取既往病史失败")
  }
}

const closeChronicDialog = () => {
  chronicDialogVisible.value = false
}

// 批量新增相关方法
const openBatchAddDialog = () => {
  batchProfiles.value = [createEmptyProfile()] // 初始化一行空数据
  batchAddDialogVisible.value = true
}

const closeBatchAddDialog = () => {
  batchAddDialogVisible.value = false
  batchProfiles.value = []
}

const createEmptyProfile = () => {
  return {
    name: '',
    age: null,
    gender: '',
    // 病例信息数组
    chronicDiseases: []
  }
}

// 创建空病例记录
const createEmptyDisease = () => {
  return {
    diseaseName: '',
    diseaseCategory: 'A',
    diagnosisDate: ''
  }
}

const addBatchRow = () => {
  if (batchProfiles.value.length >= 20) {
    ElMessage.warning('最多只能添加20条记录')
    return
  }
  batchProfiles.value.push(createEmptyProfile())
}

const removeBatchRow = (index) => {
  if (batchProfiles.value.length <= 1) {
    ElMessage.warning('至少保留一行数据')
    return
  }
  batchProfiles.value.splice(index, 1)
}

const clearAllRows = () => {
  batchProfiles.value = [createEmptyProfile()]
}

// 为指定老人添加病例记录
const addDiseaseToProfile = (profileIndex) => {
  batchProfiles.value[profileIndex].chronicDiseases.push(createEmptyDisease())
}

// 从指定老人删除病例记录
const removeDiseaseFromProfile = (profileIndex, diseaseIndex) => {
  batchProfiles.value[profileIndex].chronicDiseases.splice(diseaseIndex, 1)
}

const handleBatchSave = async () => {
  // 数据验证
  const validProfiles = []
  const invalidRows = []
  
  batchProfiles.value.forEach((profile, index) => {
    // 基本信息验证
    if (!profile.name || !profile.name.trim()) {
      invalidRows.push(`第${index + 1}行：姓名不能为空`)
      return
    }
    if (!profile.age || profile.age <= 0 || profile.age > 150) {
      invalidRows.push(`第${index + 1}行：年龄必须在1-150之间`)
      return
    }
    if (!profile.gender || !profile.gender.trim()) {
      invalidRows.push(`第${index + 1}行：性别不能为空`)
      return
    }
    
    // 病例信息验证
    const validDiseases = []
    if (profile.chronicDiseases && profile.chronicDiseases.length > 0) {
      profile.chronicDiseases.forEach((disease, diseaseIndex) => {
        // 检查病例信息是否完整
        if (disease.diseaseName && disease.diseaseName.trim()) {
          if (!disease.diseaseCategory) {
            invalidRows.push(`第${index + 1}行第${diseaseIndex + 1}个病例：疾病类别不能为空`)
            return
          }
          if (!disease.diagnosisDate) {
            invalidRows.push(`第${index + 1}行第${diseaseIndex + 1}个病例：诊断日期不能为空`)
            return
          }
          validDiseases.push({
            diseaseName: disease.diseaseName.trim(),
            diseaseCategory: disease.diseaseCategory,
            diagnosisDate: disease.diagnosisDate
          })
        }
      })
    }
    
    const validProfile = {
      name: profile.name.trim(),
      age: profile.age,
      gender: profile.gender
    }
    
    // 如果有有效的病例信息，包含病例数据
    if (validDiseases.length > 0) {
      validProfile.chronicDiseases = validDiseases
    }
    
    validProfiles.push(validProfile)
  })
  
  if (invalidRows.length > 0) {
    ElMessage.error(`数据验证失败：\n${invalidRows.join('\n')}`)
    return
  }
  
  if (validProfiles.length === 0) {
    ElMessage.warning('没有有效的数据可以提交')
    return
  }
  
  try {
    batchSaving.value = true
    const response = await batchAddElderlyProfiles(validProfiles)
    
    if (response.success) {
      ElMessage.success(response.data || '批量新增成功')
      closeBatchAddDialog()
      await loadElderlyProfiles()
    } else {
      ElMessage.error(response.message || '批量新增失败')
    }
  } catch (error) {
    ElMessage.error('批量新增失败: ' + (error.message || error))
  } finally {
    batchSaving.value = false
  }
}

onMounted(() => {
  loadElderlyProfiles()
})
</script>

<style>
/* 样式：可以根据需要自定义导入弹窗的样式 */
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}

.dialog-footer button {
  margin-left: 10px;
}
</style>




