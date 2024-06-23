<template>
  <div class="user-panel flex-column-container">
    <SidebarPanel class="sidebar" :id="upId" :menuConfig="menuConfig" :isCollapseVal="true" />
    <UserInfo class="user-wrap" :isBgShow="true" :upId="upId" />
    <user-content :style="{ 'border': isHistoryAndTrend ? 'none' : '' }">
      <router-view v-slot="{ Component }">
        <transition name="move" mode="out-in">
          <component :is="Component"></component>
        </transition>
      </router-view>
    </user-content>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { fetchPermiss } from "@/api/permiss"
import { useUserInfo } from '@/store/userInfo'
import { useRouter } from "vue-router"
import SidebarPanel from '@/components/Sidebar'
import UserInfo from "@/components/user/UserInfo"
const userInfo = useUserInfo() // 保存登录信息
const userId = userInfo.getId()
const currentURL = window.location.href
const parts = currentURL.split('/') // 当前url的分割数组
const upId = parseInt(parts[parts.length - 1]) // 最后一位upId
const isHistoryAndTrend = ref(false) // 是否为动态或历史页
const permissData = ref() // 权限结果
const router = useRouter()
const menuConfig = reactive([
  {
    icon: "Odometer",
    index: `/userCenter/myItem/${upId}`,
    title: "主页",
    permiss: true,

  },
  // {
  //   icon: "MapLocation",
  //   index: `/userCenter/trends/${upId}`,
  //   title: "动态",
  //   permiss: true 
  // },
  {
    icon: "Camera",
    index: `/userCenter/uploadVideo/${upId}`,
    title: "视频投稿",
    permiss: upId == userId
  },
  {
    icon: "Compass",
    index: `/userCenter/permission/${upId}`,
    title: "设置权限",
    permiss: upId == userId
  },
])
// },
// {
//   icon: "DataAnalysis",
//   index: `/userCenter/charts/${upId}`,
//   title: "数据中心",
//   permiss: true 
// },
onMounted(async () => {
  // 获取权限
  // if (userId !== upId) {
  //   permissData.value = await fetchPermiss(userId, upId)
  //   if (permissData.value['collectGroup']) { // 为1代表false
  //     menuConfig[0].subs[2].permiss = false
  //   }
  //   if (permissData.value['remotelyLike']) {
  //     menuConfig[0].subs[3].permiss = false
  //   }
  // }
  // 使用router实现侧栏跳转
  router
  // 判断当前是否为动态和历史记录页面
  const currentURL = window.location.href;
  // 检查当前页面 URL 是否包含 '/trends' 字符串
  if (currentURL.includes('/trends') || currentURL.includes('/history')) {
    isHistoryAndTrend.value = true
  } else {
    isHistoryAndTrend.value = false
  }
})
</script>

<style lang="scss" scoped>
$user-box-first-color: rgb(161, 150, 235);

@mixin user-box {
  box-shadow: -0.3rem -0.2rem 25px 1px $user-box-first-color;
  border-radius: 20px;
  width: calc(79vw);
  margin: 2rem 2rem 2rem 8rem;
}

.user-panel {
  position: relative;
  width: 100%;
  height: 100%;
}

.sidebar {
  position: fixed;
  top: 6.8rem;
  left: 0;
  width: auto;
  height: calc(97vh);
}

.user-info {
  @include user-box;
  margin-bottom: 0.5rem !important;
  height: calc(20vh);
}

user-content {
  /* background-color: #3c7561; */
  @include user-box;
  min-height: calc(60vh);
  height: auto;
}

@media screen and (min-width: 1020px) {

  .user-info,
  user-content {
    width: calc(89vw);
  }
}
</style>