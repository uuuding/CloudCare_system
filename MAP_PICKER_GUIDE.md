# 地图选点功能使用指南

## 功能概述

地图选点功能为围栏管理系统提供了可视化的坐标选择方式，用户可以通过地图界面直观地选择围栏中心位置，无需手动输入经纬度坐标。

## 功能特性

### 🗺️ 地图选点
- 点击地图任意位置选择坐标
- 拖拽标记点调整位置
- 实时显示选中坐标

### 🔍 地点搜索
- 支持地点名称搜索
- 快速定位到目标位置
- 自动添加标记点

### 📱 多种输入方式
- 地图可视化选点
- GPS当前位置获取
- 手动输入坐标

### 📐 坐标验证
- 自动验证坐标格式
- 经纬度范围检查
- 错误提示和处理

## 配置说明

### 1. 高德地图API密钥配置

在使用地图选点功能前，需要配置高德地图API密钥：

1. 访问 [高德开放平台](https://lbs.amap.com/) 注册账号
2. 创建应用并获取Web服务API密钥
3. 修改配置文件 `src/config/map.js`：

```javascript
export const MAP_CONFIG = {
  // 替换为您的实际API密钥
  AMAP_KEY: 'your_actual_amap_key_here',
  // ... 其他配置
}
```

### 2. 地图配置参数

在 `src/config/map.js` 中配置地图相关参数：

```javascript
export const MAP_CONFIG = {
  // 高德地图API密钥 - 请替换为您申请的实际密钥
  AMAP_KEY: 'c24b0e3fe1334b5acf3fddba1fd9974c', // 请替换为您申请的高德地图API密钥
  AMAP_VERSION: '2.0',           // API版本
  DEFAULT_CENTER: [116.397428, 39.90923], // 默认中心点（北京天安门）
  DEFAULT_ZOOM: 13,             // 默认缩放级别
  MAP_STYLE: 'amap://styles/normal', // 地图样式
  // ...
}
```

**重要提示**：
- 请将上述示例密钥替换为您从高德开放平台申请的真实API密钥
- 如果使用示例密钥或未正确配置，系统会提示"地图API密钥未配置，请联系管理员"
- 确保您的API密钥已开通Web服务API权限

## 使用方法

### 1. 在围栏编辑中使用

1. 打开围栏管理页面
2. 点击"创建围栏"或编辑现有围栏
3. 选择"圆形围栏"类型
4. 在"中心坐标"字段，点击"地图选点"按钮
5. 在弹出的地图对话框中选择位置
6. 确认选择后坐标自动填入表单

### 2. 地图操作说明

#### 选择坐标
- **点击地图**：在地图上点击任意位置设置坐标点
- **拖拽标记**：拖拽红色标记点调整位置
- **搜索定位**：在搜索框输入地点名称，按回车或点击搜索按钮

#### 搜索功能
- 支持中文地点名称搜索
- 支持详细地址搜索
- 示例："北京天安门"、"上海外滩"、"深圳市南山区"

### 3. 坐标格式

系统使用的坐标格式为：`经度,纬度`

- **经度范围**：-180 到 180
- **纬度范围**：-90 到 90
- **精度**：保留6位小数
- **示例**：`116.397428,39.909230`

## 组件集成

### MapPicker组件使用

```vue
<template>
  <MapPicker 
    v-model="coordinates"
    button-text="选择位置"
    @change="handleCoordinateChange"
  />
</template>

<script setup>
import MapPicker from '@/components/MapPicker.vue'
import { ref } from 'vue'

const coordinates = ref('')

const handleCoordinateChange = (newCoordinates) => {
  console.log('新坐标:', newCoordinates)
}
</script>
```

### 组件属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `modelValue` | String | '' | 坐标值（双向绑定） |
| `buttonText` | String | '地图选点' | 按钮文本 |
| `disabled` | Boolean | false | 是否禁用 |
| `defaultCenter` | Array | [116.397428, 39.90923] | 默认中心点 |

### 组件事件

| 事件 | 参数 | 说明 |
|------|------|------|
| `update:modelValue` | coordinates | 坐标值变化 |
| `change` | coordinates | 坐标选择完成 |

## 故障排除

### 常见问题

1. **"地图API密钥未配置，请联系管理员"**
   **原因**：API密钥配置错误或使用了示例密钥
   **解决方案**：
   - 检查 `src/config/map.js` 中的 `AMAP_KEY` 配置
   - 确保已将示例密钥 `c24b0e3fe1334b5acf3fddba1fd9974c` 替换为您申请的真实密钥
   - 验证密钥格式是否正确（32位字符串）
   - 确认密钥已开通Web服务API权限

2. **地图无法加载**
   - 检查网络连接
   - 确认API密钥是否正确配置
   - 检查API密钥是否有效且未过期
   - 查看浏览器控制台错误信息
   - 检查高德地图API服务状态

3. **搜索功能不可用**
   - 确认API密钥包含地理编码服务权限
   - 检查搜索关键词是否正确
   - 验证搜索关键词格式

4. **定位功能异常**
   - 确认浏览器已允许位置访问
   - 检查HTTPS环境（定位功能需要安全环境）
   - 检查设备GPS功能是否正常

5. **坐标格式错误**
   - 确保坐标格式为 `经度,纬度`
   - 检查经纬度数值范围

### 错误信息

- `地图API密钥未配置`：需要在配置文件中设置正确的API密钥
- `地图加载失败`：网络连接问题或API服务异常
- `坐标格式不正确`：输入的坐标不符合格式要求

## 技术实现

### 核心技术
- **高德地图API 2.0**：提供地图显示和交互功能
- **Vue 3 Composition API**：组件开发框架
- **Element Plus**：UI组件库

### 文件结构
```
src/
├── components/
│   └── MapPicker.vue          # 地图选点组件
├── config/
│   └── map.js                 # 地图配置文件
└── views/elderly-service/geo-fence/
    └── index.vue              # 围栏管理页面
```

### 工具函数
- `loadAMapAPI()`：异步加载地图API
- `validateCoordinates()`：坐标格式验证
- `parseCoordinates()`：坐标字符串解析
- `formatCoordinates()`：坐标格式化

## 更新日志

### v1.0.0 (2024-01-XX)
- ✨ 新增地图选点功能
- ✨ 支持地点搜索定位
- ✨ 集成到围栏编辑表单
- ✨ 添加坐标验证和错误处理
- 📱 支持响应式布局
- 🎨 优化用户界面和交互体验

## 许可证

本功能基于高德地图API开发，使用时请遵守高德地图服务条款。