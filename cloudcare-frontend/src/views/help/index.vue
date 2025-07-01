<template>
  <div class="help-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">使用帮助</h1>
      <p class="page-description">云护CloudCare系统使用指南</p>
    </div>

    <!-- 帮助内容 -->
    <div class="help-content">
      <el-row :gutter="20">
        <!-- 快速入门 -->
        <el-col :span="12">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Guide /></el-icon>
                <span>快速入门</span>
              </div>
            </template>
            <div class="help-section">
              <h3>系统概述</h3>
              <p>云护CloudCare是一个智能化的老年人健康管理系统，提供全方位的健康监护和养老服务。</p>
              
              <h3>主要功能模块</h3>
              <ul>
                <li><strong>首页：</strong>系统概览和快捷操作</li>
                <li><strong>老人档案管理：</strong>管理老人基本信息和健康档案</li>
                <li><strong>健康管理：</strong>健康预警和评估功能</li>
                <li><strong>干预管理：</strong>制定和管理干预方案</li>
                <li><strong>医疗服务：</strong>在线预约和电子病历</li>
                <li><strong>养老服务：</strong>机构管理和服务调度</li>
              </ul>
            </div>
          </el-card>
        </el-col>

        <!-- 常见问题 -->
        <el-col :span="12">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><QuestionFilled /></el-icon>
                <span>常见问题</span>
              </div>
            </template>
            <div class="help-section">
              <el-collapse v-model="activeNames">
                <el-collapse-item title="如何添加老人档案？" name="1">
                  <p>1. 进入"老人档案管理"页面</p>
                  <p>2. 点击"新增老人"按钮</p>
                  <p>3. 填写老人基本信息</p>
                  <p>4. 保存档案信息</p>
                </el-collapse-item>
                <el-collapse-item title="如何设置健康预警？" name="2">
                  <p>1. 进入"健康管理" > "健康预警"</p>
                  <p>2. 选择需要设置预警的老人</p>
                  <p>3. 配置预警参数和阈值</p>
                  <p>4. 启用预警功能</p>
                </el-collapse-item>
                <el-collapse-item title="如何查看统计报表？" name="3">
                  <p>1. 进入"报表统计与分析"页面</p>
                  <p>2. 选择报表类型和时间范围</p>
                  <p>3. 点击"生成报表"按钮</p>
                  <p>4. 查看或导出报表数据</p>
                </el-collapse-item>
              </el-collapse>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 操作指南 -->
        <el-col :span="12">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Document /></el-icon>
                <span>操作指南</span>
              </div>
            </template>
            <div class="help-section">
              <h3>基本操作</h3>
              <div class="operation-item">
                <h4>1. 登录系统</h4>
                <p>使用管理员分配的账号和密码登录系统</p>
              </div>
              <div class="operation-item">
                <h4>2. 导航使用</h4>
                <p>通过左侧菜单栏快速访问各功能模块</p>
              </div>
              <div class="operation-item">
                <h4>3. 数据管理</h4>
                <p>支持数据的增删改查操作，可批量处理</p>
              </div>
              <div class="operation-item">
                <h4>4. 权限管理</h4>
                <p>不同角色用户具有不同的操作权限</p>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 联系支持 -->
        <el-col :span="12">
          <el-card class="help-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Service /></el-icon>
                <span>联系支持</span>
              </div>
            </template>
            <div class="help-section">
              <h3>技术支持</h3>
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
              
              <h3>在线帮助</h3>
              <div class="help-links">
                <el-button type="primary" link>
                  <el-icon><VideoPlay /></el-icon>
                  视频教程
                </el-button>
                <el-button type="primary" link>
                  <el-icon><Download /></el-icon>
                  用户手册
                </el-button>
                <el-button type="primary" link>
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
  ChatDotRound
} from '@element-plus/icons-vue'

// 折叠面板激活项
const activeNames = ref(['1'])
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

.help-card {
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

.operation-item {
  margin-bottom: 15px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 3px solid #3498db;
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
}

.contact-item .el-icon {
  margin-right: 8px;
  color: #3498db;
}

.help-links {
  margin-top: 15px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.help-links .el-button {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 折叠面板样式 */
:deep(.el-collapse) {
  border: none;
}

:deep(.el-collapse-item__header) {
  background: #f8f9fa;
  border: none;
  border-radius: 6px;
  margin-bottom: 5px;
  padding: 0 15px;
  font-weight: 500;
  color: #2c3e50;
}

:deep(.el-collapse-item__content) {
  padding: 15px;
  background: #ffffff;
  border-radius: 6px;
  margin-bottom: 10px;
  border: 1px solid #e9ecef;
}

:deep(.el-collapse-item__content p) {
  margin: 5px 0;
  color: #5a6c7d;
  font-size: 14px;
}
</style>