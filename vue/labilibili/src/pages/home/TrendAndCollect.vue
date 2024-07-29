<template>
  <!--具体项目-->
  <span v-for="(item, index) in sections" :key="index" style="position: relative;">
    <div @mouseleave="item.expanded = false">
      <!--消息通知数-->
      <div v-if="item.type === 'trend' && messageConter.data.dynamicVideoCount > 0">
        <div class="notice-bubble-whole notice-dynamic-bubble">
          <p v-if="messageConter.data.dynamicVideoCount < 99">{{ messageConter.data.dynamicVideoCount }}</p>
          <p v-else>99+</p>
        </div>
      </div>
      <div v-if="item.type === 'message' && messageConter.data.totalCount > 0">
        <div class="notice-bubble-whole notice-dynamic-bubble">
          <p v-if="messageConter.data.totalCount < 99">{{ messageConter.data.totalCount }}</p>
          <p v-else>99+</p>
        </div>
      </div>
      <!--图标-->
      <span class="notice-icon-wrap">
        <img :src="item.icon" :alt="item.name" class="notice-icon" @click.stop="turnPage(item.turnTo)"
          @mouseover="item.expanded = true" />
      </span>
      <!--待展开的列表-->
      <div v-if="item.expanded" class="right-panel">
        <Transition>
          <itemPanel :itemType="item.type" :dataTmp="messageConter.data" @changeDYState="changeDYState" />
        </Transition>
      </div>
    </div>
  </span>
</template>

<script setup>
import { ref, reactive, onMounted, defineAsyncComponent } from 'vue'
const itemPanel = defineAsyncComponent(() =>
  import('./ItemPanel.vue')
)
import { useRouter } from 'vue-router'
import { useUserInfo } from "@/store/userInfo"
import { fetchNoticeNum } from "@/api/notice"

import { usemessageConter } from "@/store/messageConter"
const messageConter = usemessageConter()

const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const dynamicNoticeNum = ref(0)
// import getVideoSmall from '@/api/video' // 动态的api
const router = useRouter()
// 打开其他页
const turnPage = (routerPath) => {
  const routeURL = router.resolve({
    path: routerPath,
  })
  window.open(routeURL.href, '_blank')
}
/**
 * 具体内容分类
 */
const sections = reactive([
  {
    type: 'trend',
    name: "动态",
    turnTo: "/trend",
    icon: require("@/assets/img/trend_icon.svg"),
    expanded: false,
  },
  {
    type: 'message',
    name: '消息',
    turnTo: `/message/Reply/${userId}`,
    icon: require("@/assets/img/message.svg"),
    expanded: false,
  },
  {
    type: 'collect',
    name: "收藏",
    turnTo: `/userCenter/myItem/${userId}`,
    icon: require("@/assets/img/collect_icon.svg"),
    expanded: false,
  }, {
    type: 'history',
    name: "历史",
    turnTo: "/history",
    icon: require("@/assets/img/history_icon.svg"),
    expanded: false,
  },

])

/**
 * 获得动态通知数
 */
const dataTmp = ref({

})
const changeDYState = () => {
  messageConter.data.dynamicNoticeNum = 0
}
const totalCount = ref(0)
// 获得数据
const getDNoticeNum = async () => {
  const data_tmp = await fetchNoticeNum(userId)
  console.log('data_tmp', data_tmp);
  messageConter.data = data_tmp

}
onMounted(() => {
  getDNoticeNum()
})
</script>

<style lang="scss" scoped>
.notice-icon {
  margin-right: 2rem;
  width: 2rem;
  height: 2rem;
  margin-top: 0.2rem;
}

.notice-icon:active {
  transition: 0.01s;
  transform: scale(0.9, 0.9);
  -webkit-transform: scale(0.9, 0.9);
  -o-transform: scale(0.9, 0.9);
  -ms-transform: scale(0.9, 0.9);
}

.right-panel {
  background: #ffffff;
  box-shadow: 0 0.5rem 20px 1px #79b1ec6e;
  position: absolute;
  padding: 1rem;
  z-index: 5;
  top: 2.5rem;
  transform: translateX(-50%);
  border-radius: 20px;
}

.notice-dynamic-bubble {
  width: 1.6rem;
  height: 1.6rem;
  right: 1.5rem;
  background-color: #c45656 !important;

  p {
    color: #70a5f4;
    font-size: 0.9rem;
  }
}
</style>