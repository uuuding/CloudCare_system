<template>
  <div class="help-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">使用帮助</h1>
      <p class="page-description">云护CloudCare系统使用指南</p>
    </div>

    <!-- 帮助内容 -->
    <div class="help-content">
      <!-- 系统概述 -->
      <el-row :gutter="20" class="section-row">
        <el-col :span="24">
          <el-card class="help-card overview-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Guide /></el-icon>
                <span>系统概述</span>
              </div>
            </template>
            <div class="help-section">
              <p class="overview-text">云护CloudCare是一个智能化的老年人健康管理系统，提供全方位的健康监护和养老服务，包括实时定位监控、健康预警、电子围栏、短信通知等核心功能。</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 功能模块 -->
      <el-row :gutter="20" class="section-row">
        <el-col :span="8">
          <el-card class="help-card feature-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><User /></el-icon>
                <span>档案与健康管理</span>
              </div>
            </template>
            <div class="help-section">
              <div class="feature-item">
                <h4>老人档案管理</h4>
                <p>• 基本信息管理</p>
                <p>• 健康档案记录</p>
                <p>• 紧急联系人设置</p>
                <p>• 多条件查询筛选</p>
              </div>
              <div class="feature-item">
                <h4>健康管理</h4>
                <p>• 生命体征监控</p>
                <p>• 健康预警设置</p>
                <p>• 健康报告生成</p>
                <p>• 预警事件处理</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="help-card feature-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Location /></el-icon>
                <span>定位与安全监控</span>
              </div>
            </template>
            <div class="help-section">
              <div class="feature-item">
                <h4>GPS定位服务</h4>
                <p>• 实时位置监控</p>
                <p>• 历史轨迹查看</p>
                <p>• 设备绑定管理</p>
                <p>• 位置数据分析</p>
              </div>
              <div class="feature-item">
                <h4>电子围栏</h4>
                <p>• 圆形/多边形围栏</p>
                <p>• 进出告警通知</p>
                <p>• 地图选点设置</p>
                <p>• 围栏事件记录</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="help-card feature-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Service /></el-icon>
                <span>服务与通知</span>
              </div>
            </template>
            <div class="help-section">
              <div class="feature-item">
                <h4>短信通知服务</h4>
                <p>• 自动告警通知</p>
                <p>• 短信模板管理</p>
                <p>• 发送记录查询</p>
                <p>• 余额管理</p>
              </div>
              <div class="feature-item">
                <h4>医疗养老服务</h4>
                <p>• 医疗预约管理</p>
                <p>• 就诊记录</p>
                <p>• 生活服务预约</p>
                <p>• 服务质量监控</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 常见问题 -->
      <el-row :gutter="20" class="section-row">
        <el-col :span="24">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><QuestionFilled /></el-icon>
                <span>常见问题</span>
              </div>
            </template>
            <div class="help-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-collapse v-model="activeNames">
                    <el-collapse-item title="如何添加老人档案？" name="1">
                      <p>1. 进入"老人档案管理"页面</p>
                      <p>2. 点击"新增老人"按钮</p>
                      <p>3. 填写老人基本信息、健康状况、紧急联系人等</p>
                      <p>4. 保存档案信息</p>
                    </el-collapse-item>
                    <el-collapse-item title="如何设置健康预警？" name="2">
                      <p>1. 进入"健康管理" > "健康预警"</p>
                      <p>2. 选择需要设置预警的老人</p>
                      <p>3. 配置血压、血糖、心率等健康指标的预警阈值</p>
                      <p>4. 启用预警功能，系统会自动监控并发送告警</p>
                    </el-collapse-item>
                    <el-collapse-item title="如何设置电子围栏？" name="3">
                      <p>1. 进入"电子围栏"页面，点击"创建围栏"</p>
                      <p>2. 选择围栏类型（圆形/多边形）</p>
                      <p>3. 使用地图选点功能设置围栏范围</p>
                      <p>4. 配置紧急联系人，启用围栏监控</p>
                    </el-collapse-item>
                  </el-collapse>
                </el-col>
                <el-col :span="12">
                  <el-collapse v-model="activeNames2">
                    <el-collapse-item title="如何绑定GPS设备？" name="4">
                      <p>1. 在"设备管理"页面，点击"设备绑定"</p>
                      <p>2. 输入设备IMEI号和对应的老人信息</p>
                      <p>3. 确认绑定即可开始位置监控</p>
                      <p>4. 在"实时定位"页面查看老人位置</p>
                    </el-collapse-item>
                    <el-collapse-item title="如何查看统计报表？" name="5">
                      <p>1. 进入"报表统计与分析"页面</p>
                      <p>2. 选择报表类型和时间范围</p>
                      <p>3. 点击"生成报表"按钮</p>
                      <p>4. 查看或导出报表数据</p>
                    </el-collapse-item>
                    <el-collapse-item title="短信通知如何配置？" name="6">
                      <p>1. 在"短信管理"模块中设置短信模板</p>
                      <p>2. 配置自动通知规则</p>
                      <p>3. 系统会根据预警事件自动发送短信通知</p>
                      <p>4. 可在"发送记录"中查看短信状态</p>
                    </el-collapse-item>
                  </el-collapse>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 操作指南 -->
      <el-row :gutter="20" class="section-row">
        <el-col :span="24">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Document /></el-icon>
                <span>操作指南</span>
              </div>
            </template>
            <div class="help-section">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="guide-section">
                    <h3>基础操作</h3>
                    <div class="operation-item">
                      <h4>系统登录与导航</h4>
                      <p>• 使用管理员提供的账号密码登录系统</p>
                      <p>• 首次登录建议修改默认密码</p>
                      <p>• 通过左侧菜单栏快速访问各功能模块</p>
                    </div>
                    
                    <div class="operation-item">
                      <h4>老人档案管理</h4>
                      <p>• 新增档案：点击"新增"→填写信息→保存</p>
                      <p>• 编辑档案：列表中点击"编辑"→修改→保存</p>
                      <p>• 查询档案：搜索框输入关键词快速查找</p>
                    </div>
                    
                    <div class="operation-item">
                      <h4>权限管理说明</h4>
                      <p>• 管理员：拥有所有功能的完整权限</p>
                      <p>• 护理员：可管理档案、查看健康数据</p>
                      <p>• 家属：仅可查看相关老人信息</p>
                    </div>
                  </div>
                </el-col>
                
                <el-col :span="12">
                  <div class="guide-section">
                    <h3>高级功能</h3>
                    <div class="operation-item">
                      <h4>电子围栏设置</h4>
                      <p>• 创建围栏：选择老人→选择围栏类型</p>
                      <p>• 地图选点：使用地图设置围栏范围</p>
                      <p>• 配置告警：设置紧急联系人和告警类型</p>
                    </div>
                    
                    <div class="operation-item">
                      <h4>GPS设备管理</h4>
                      <p>• 设备绑定：输入IMEI号→选择老人→确认</p>
                      <p>• 查看位置："实时定位"页面查看当前位置</p>
                      <p>• 历史轨迹：选择时间范围查看移动轨迹</p>
                    </div>
                    
                    <div class="operation-item">
                      <h4>数据管理</h4>
                      <p>• 支持数据的增删改查操作</p>
                      <p>• 可批量处理和导出数据</p>
                      <p>• 定期备份重要数据</p>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 技术支持与帮助 -->
      <el-row :gutter="20" class="section-row">
        <el-col :span="12">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Service /></el-icon>
                <span>技术支持</span>
              </div>
            </template>
            <div class="help-section">
              <div class="contact-info">
                <div class="contact-item">
                  <el-icon><Phone /></el-icon>
                  <span>技术热线：15983660989</span>
                </div>
                <div class="contact-item">
                  <el-icon><Message /></el-icon>
                  <span>邮箱：3296416743@qq.com</span>
                </div>
                <div class="contact-item">
                  <el-icon><Clock /></el-icon>
                  <span>服务时间：周一至周五 9:00-18:00</span>
                </div>
              </div>
              
              <div class="version-info">
                <h4>系统版本信息</h4>
                <p>当前版本：CloudCare v1.0.0</p>
                <p>更新时间：2025年7月</p>
                <p>兼容浏览器：Chrome 80+、Firefox 75+、Safari 13+、Edge 80+</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Link /></el-icon>
                <span>在线帮助</span>
              </div>
            </template>
            <div class="help-section">
              <div class="help-links">
                <el-button type="primary" @click="openDoc('api')" class="help-button">
                  <el-icon><Document /></el-icon>
                  API接口文档
                </el-button>
                <el-button type="success" @click="openDoc('video')" class="help-button">
                  <el-icon><VideoPlay /></el-icon>
                  视频教程
                </el-button>
                <el-button type="warning" @click="openDoc('gps')" class="help-button">
                  <el-icon><Location /></el-icon>
                  GPS围栏指南
                </el-button>
                <el-button type="info" @click="openDoc('sms')" class="help-button">
                  <el-icon><Message /></el-icon>
                  短信服务说明
                </el-button>
                <el-button type="danger" @click="openDoc('map')" class="help-button">
                  <el-icon><MapLocation /></el-icon>
                  地图选点指南
                </el-button>
                <el-button type="primary" @click="openDoc('support')" class="help-button">
                  <el-icon><ChatDotRound /></el-icon>
                  在线客服
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {
  Guide,
  QuestionFilled,
  Document,
  Service,
  Phone,
  Message,
  Clock,
  VideoPlay,
  Download,
  ChatDotRound,
  User,
  Location,
  MapLocation,
  Link
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 折叠面板激活项
const activeNames = ref(['1'])
const activeNames2 = ref([])

// 打开文档链接
const openDoc = (type) => {
  const docs = {
    api: '/docs/api-docs.md',
    video: '#',
    gps: '/docs/GPS_FENCE_SYSTEM_README.md',
    sms: '/docs/SMS_MODULE_DOCUMENTATION.md',
    map: '/docs/MAP_PICKER_GUIDE.md',
    support: '#'
  }
  
  if (docs[type] === '#') {
    ElMessage.info('该功能正在开发中，敬请期待！')
  } else {
    // 在新窗口打开文档
    window.open(docs[type], '_blank')
  }
}
</script>

<style scoped>
.help-container {
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: calc(100vh - 120px);
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 10px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-description {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.help-content {
  max-width: 1200px;
  margin: 0 auto;
}

.section-row {
  margin-bottom: 20px;
}

.help-card {
  height: 100%;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.help-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.overview-card {
  background: linear-gradient(135deg, rgba(52, 152, 219, 0.1) 0%, rgba(155, 89, 182, 0.1) 100%);
}

.feature-card {
  height: 100%;
}

.card-header {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.header-icon {
  margin-right: 8px;
  font-size: 20px;
  color: #3498db;
}

.help-section {
  padding: 10px 0;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.help-section h3 {
  color: #2c3e50;
  font-size: 16px;
  font-weight: 600;
  margin: 15px 0 10px 0;
  border-left: 3px solid #3498db;
  padding-left: 10px;
}

.help-section h4 {
  color: #34495e;
  font-size: 14px;
  font-weight: 600;
  margin: 10px 0 5px 0;
}

.help-section p {
  color: #5a6c7d;
  font-size: 14px;
  line-height: 1.6;
  margin: 5px 0;
}

.help-section ul {
  margin: 10px 0;
  padding-left: 20px;
}

.help-section li {
  color: #5a6c7d;
  font-size: 14px;
  line-height: 1.6;
  margin: 5px 0;
}

.overview-text {
  font-size: 16px;
  line-height: 1.8;
  color: #2c3e50;
  text-align: center;
  padding: 10px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  margin: 10px 0;
}

.feature-item {
  margin-bottom: 20px;
  padding: 15px;
  background: rgba(248, 249, 250, 0.8);
  border-radius: 8px;
  border-left: 4px solid #3498db;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(227, 242, 253, 0.9);
  transform: translateX(3px);
}

.feature-item h4 {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 15px;
}

.feature-item p {
  margin: 3px 0;
  font-size: 13px;
  color: #6c757d;
}

.guide-section {
  height: 100%;
}

.guide-section h3 {
  color: #2c3e50;
  margin-bottom: 15px;
  font-size: 16px;
  border-left: 4px solid #3498db;
  padding-left: 10px;
}

.operation-item {
  margin-bottom: 15px;
  padding: 15px;
  background: rgba(248, 249, 250, 0.8);
  border-radius: 8px;
  border-left: 4px solid #3498db;
  transition: all 0.3s ease;
}

.operation-item:hover {
  background: rgba(227, 242, 253, 0.9);
  transform: translateX(3px);
}

.operation-item h4 {
  margin: 0 0 10px 0;
  color: #2c3e50;
  font-size: 15px;
}

.operation-item p {
  margin: 3px 0;
  color: #6c757d;
  font-size: 13px;
}

.contact-info {
  margin: 15px 0;
}

.contact-item {
  display: flex;
  align-items: center;
  margin: 10px 0;
  color: #5a6c7d;
  font-size: 14px;
  padding: 8px;
  background: rgba(248, 249, 250, 0.6);
  border-radius: 6px;
  transition: all 0.3s ease;
}

.contact-item:hover {
  background: rgba(227, 242, 253, 0.8);
}

.contact-item .el-icon {
  margin-right: 8px;
  color: #3498db;
}

.version-info {
  margin-top: 20px;
  padding: 15px;
  background: rgba(232, 244, 253, 0.8);
  border-radius: 8px;
  border-left: 4px solid #3498db;
}

.version-info h4 {
  margin: 0 0 10px 0;
  color: #2c3e50;
  font-size: 15px;
}

.version-info p {
  margin: 5px 0;
  color: #6c757d;
  font-size: 13px;
}

.help-links {
  margin-top: 15px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px;
}

.help-button {
  width: 100%;
  justify-content: flex-start;
  padding: 12px 16px;
  height: auto;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.help-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.help-button .el-icon {
  margin-right: 8px;
}

/* 折叠面板样式 */
:deep(.el-collapse) {
  border: none;
}

:deep(.el-collapse-item__header) {
  background: rgba(248, 249, 250, 0.8);
  border: none;
  border-radius: 6px;
  margin-bottom: 5px;
  padding: 0 15px;
  font-weight: 500;
  color: #2c3e50;
  transition: all 0.3s ease;
}

:deep(.el-collapse-item__header:hover) {
  background: rgba(227, 242, 253, 0.9);
}

:deep(.el-collapse-item__content) {
  padding: 15px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 6px;
  margin-bottom: 10px;
  border: 1px solid rgba(233, 236, 239, 0.6);
}

:deep(.el-collapse-item__content p) {
  margin: 5px 0;
  color: #5a6c7d;
  font-size: 14px;
}

@media (max-width: 768px) {
  .help-container {
    padding: 10px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .help-links {
    grid-template-columns: 1fr;
  }
  
  .feature-item, .operation-item {
    padding: 10px;
  }
}
</style>