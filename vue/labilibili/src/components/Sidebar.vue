<template>
  <el-aside>
    <el-menu :default-active="onRoutes" :collapse="collapse" background-color="rgba(94,73,238,1)" text-color="#ffffff"
      active-text-color="#f7da94" unique-opened :router=true>
      <template v-for="(item, index) in items" :key="index">
        <template v-if="item.subs">
          <el-sub-menu v-if="item.permiss" :index="item.index" popper-class="el-popuper">
            <template #title>
              <el-icon class="main-icon">
                <component :is="item.icon"></component>
              </el-icon>
              <span v-if="!collapse">{{ item.title }}</span>
            </template>
            <el-menu-item v-for="(subItem, index_) in item.subs" :key="index_" :index="subItem.index">
              <div v-if="subItem.permiss" class="main-title">{{ subItem.title }}</div>
            </el-menu-item>
          </el-sub-menu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" v-if="item.permiss">
            <el-icon>
              <component :is="item.icon"></component>
            </el-icon>
            <p v-if="!collapse">{{ item.title }}</p>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed, ref, defineProps } from "vue"
import { useRoute } from "vue-router"
const props = defineProps({
  id: { // 用户id
    type: Number,
    required: true,
    default: 1
  },
  menuConfig: { // 菜单项的设置
    type: Object,
    required: true,
    default: undefined
  },
  isCollapseVal: {
    type: Boolean,
    required: false,
    default: true
  }
})
const route = useRoute()
const items = props.menuConfig
const onRoutes = computed(() => {
  return route.path;
});
const collapse = computed(() => {
  return props.isCollapseVal
})
</script>

<style lang="scss">
@import url(@/assets/css/menu.css);
</style>