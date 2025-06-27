<template>
  <div v-if="!item.hidden">
    <!-- 没有子菜单的情况 -->
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown': !isNest}">
          <el-icon v-if="onlyOneChild.meta && onlyOneChild.meta.icon">
            <component :is="onlyOneChild.meta.icon" />
          </el-icon>
          <template #title>
            <span>{{ onlyOneChild.meta.title }}</span>
          </template>
        </el-menu-item>
      </app-link>
    </template>
    
    <!-- 有子菜单的情况 -->
    <el-sub-menu v-else :index="resolvePath(item.path)" popper-append-to-body>
      <template #title>
        <el-icon v-if="item.meta && item.meta.icon">
          <component :is="item.meta.icon" />
        </el-icon>
        <span>{{ item.meta.title }}</span>
      </template>
      
      <!-- 递归渲染子菜单 -->
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :is-nest="true"
        :base-path="resolvePath(child.path)"
        :is-collapse="isCollapse"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { isExternal } from '@/utils/validate'
import AppLink from './Link.vue'
import path from 'path-browserify'

const props = defineProps({
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  },
  isCollapse: {
    type: Boolean,
    default: false
  }
})

// 唯一子菜单
const onlyOneChild = ref(null)

// 判断是否只有一个显示的子菜单
const hasOneShowingChild = (children = [], parent) => {
  const showingChildren = children.filter(item => {
    if (item.hidden) {
      return false
    } else {
      // 临时设置
      onlyOneChild.value = item
      return true
    }
  })
  
  // 当只有一个子路由，子路由默认显示
  if (showingChildren.length === 1) {
    return true
  }
  
  // 没有子路由则显示父路由
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }
  
  return false
}

// 解析路径
const resolvePath = (routePath) => {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  return path.resolve(props.basePath, routePath)
}
</script>

<style scoped>
/* 确保图标在折叠状态下居中显示 */
.el-menu-item .el-icon,
.el-sub-menu .el-icon,
.el-menu--collapse .el-menu-item .el-icon,
.el-menu--collapse .el-sub-menu .el-icon {
  margin-right: 10px;
  width: 24px;
  text-align: center;
  vertical-align: middle;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 确保文字垂直居中 */
.el-menu-item span,
.el-sub-menu__title span {
  vertical-align: middle;
}

/* 确保折叠状态下文字隐藏 */
.el-menu--collapse .el-menu-item span,
.el-menu--collapse .el-sub-menu__title span,
.el-menu--collapse .el-sub-menu .el-sub-menu__title span,
.el-menu--collapse .el-sub-menu .el-menu-item span {
  display: none !important;
  width: 0 !important;
  height: 0 !important;
  overflow: hidden !important;
  visibility: hidden !important;
}

/* 折叠时的提示框样式 */
.el-menu--popup {
  background-color: #1f2d3d !important;
}

.el-menu--popup .el-menu-item {
  background-color: #1f2d3d !important;
}

.el-menu--popup .el-menu-item:hover {
  background-color: #001528 !important;
}

/* 确保折叠时弹出的子菜单中的文字正确显示 */
.el-menu--popup .el-menu-item span {
  display: inline-block !important;
  visibility: visible !important;
}
</style>