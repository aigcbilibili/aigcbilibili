<template>
  <!--具体项目-->
  <span v-for="(item, index) in sections" :key="index">
    <div @mouseleave="item.expanded=false">
      <!--图标-->
      <span class="notice-icon-wrap">   
          <img :src="item.icon" :alt="item.name" class="notice-icon" @click="item.expanded=true" @mouseover="item.expanded=true"/>
      </span>
      <!--待展开的列表-->
      <div v-if="item.expanded" class="right-panel">
          <Transition>
            <itemPanel :itemType="item.type"/>
          </Transition>
      </div>
    </div>
  </span>

</template>

<script setup>
import { ref, reactive, onMounted, defineAsyncComponent } from 'vue'
// import getVideoSmall from '@/api/video' // 动态的api
const itemPanel = defineAsyncComponent(()=>
  import ('./ItemPanel.vue')
)

/**
 * 具体内容分类
 */
const sections = reactive([{
  type: 'trend',
  name: "动态",
  icon: require("@/assets/img/trend_icon.svg"),
  expanded: false,
},{
  type: 'collect',
  name: "收藏",
  icon: require("@/assets/img/collect_icon.svg"),
  expanded: false,
},{
  type: 'history',
  name: "历史",
  icon: require("@/assets/img/history_icon.svg"),
  expanded: false,
}])

</script>

<style lang="scss" scoped>
.notice-icon{
  margin-right: 2rem; 
  width: 2rem; 
  height: 2rem; 
  margin-top: 0.2rem;
}
.notice-icon:active{
  transition: 0.01s;
  transform: scale(0.9, 0.9);
  -webkit-transform: scale(0.9, 0.9);
  -o-transform: scale(0.9, 0.9);
  -ms-transform: scale(0.9, 0.9);
}
.right-panel{
  background: #ffffff;
  box-shadow: 0 0.5rem 20px 1px #79b1ec6e;
  position: absolute;
  padding: 1rem;
  z-index: 5;
  top: 5rem;
  right: 3rem;
  border-radius: 20px;
}

</style>