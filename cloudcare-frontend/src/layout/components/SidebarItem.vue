<template>
  <div v-if="!item.hidden">
    <!-- 没有子菜单的情况 -->
    <template v-if="!hasOneShowingChild(item.children, item) || onlyOneChild.children">
      <el-menu-item 
        v-if="onlyOneChild.meta" 
        :index="resolvePath(onlyOneChild.path)"
      >
        <el-icon v-if="onlyOneChild.meta.icon">
          <component :is="onlyOneChild.meta.icon" />
        </el-icon>
        <template #title>
          <span>{{ onlyOneChild.meta.title }}</span>
        </template>
      </el-menu-item>
    </template>
    
    <!-- 有子菜单的情况 -->
    <el-sub-menu v-else :index="resolvePath(item.path)">
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
        :base-path="resolvePath(child.path)"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { isExternal } from '@/utils/validate'
import path from 'path-browserify'

// 定义props
const props = defineProps({
  item: {
    type: Object,
    required: true
  },
  basePath: {
    type: String,
    default: ''
  }
})

// 唯一子菜单
const onlyOneChild = ref(null)

/**
 * 判断是否只有一个显示的子菜单
 * @param {Array} children 子菜单数组
 * @param {Object} parent 父菜单
 * @returns {Boolean}
 */
const hasOneShowingChild = (children = [], parent) => {
  // 过滤出显示的子菜单
  const showingChildren = children.filter(item => {
    if (item.hidden) {
      return false
    } else {
      // 设置唯一子菜单
      onlyOneChild.value = item
      return true
    }
  })

  // 如果只有一个显示的子菜单
  if (showingChildren.length === 1) {
    return true
  }

  // 如果没有显示的子菜单，则显示父菜单
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
}

/**
 * 解析路径
 * @param {String} routePath 路由路径
 * @returns {String}
 */
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