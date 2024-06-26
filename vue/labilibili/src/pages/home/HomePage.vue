<template>
  <div class="main-container">
    <div class="header flex-between-container" @click="scollToTop()"><!--NOTE vue3不支持@on-click绑定-->
      <!--|最左边|-->
      <div class="left-align">
        <!--标题-->
        <div class="h1-new" @click.stop="handleNewPage(0)">FakeLi</div>
        <!--左侧链接-->
        <div id="home-icon" @click.stop="handleNewPage(0)">
          <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg"
            class="zhuzhan-icon">
            <path fill-rule="evenodd" clip-rule="evenodd"
              d="M3.73252 2.67094C3.33229 2.28484 3.33229 1.64373 3.73252 1.25764C4.11291 0.890684 4.71552 0.890684 5.09591 1.25764L7.21723 3.30403C7.27749 3.36218 7.32869 3.4261 7.37081 3.49407H10.5789C10.6211 3.4261 10.6723 3.36218 10.7325 3.30403L12.8538 1.25764C13.2342 0.890684 13.8368 0.890684 14.2172 1.25764C14.6175 1.64373 14.6175 2.28484 14.2172 2.67094L13.364 3.49407H14C16.2091 3.49407 18 5.28493 18 7.49407V12.9996C18 15.2087 16.2091 16.9996 14 16.9996H4C1.79086 16.9996 0 15.2087 0 12.9996V7.49406C0 5.28492 1.79086 3.49407 4 3.49407H4.58579L3.73252 2.67094ZM4 5.42343C2.89543 5.42343 2 6.31886 2 7.42343V13.0702C2 14.1748 2.89543 15.0702 4 15.0702H14C15.1046 15.0702 16 14.1748 16 13.0702V7.42343C16 6.31886 15.1046 5.42343 14 5.42343H4ZM5 9.31747C5 8.76519 5.44772 8.31747 6 8.31747C6.55228 8.31747 7 8.76519 7 9.31747V10.2115C7 10.7638 6.55228 11.2115 6 11.2115C5.44772 11.2115 5 10.7638 5 10.2115V9.31747ZM12 8.31747C11.4477 8.31747 11 8.76519 11 9.31747V10.2115C11 10.7638 11.4477 11.2115 12 11.2115C12.5523 11.2115 13 10.7638 13 10.2115V9.31747C13 8.76519 12.5523 8.31747 12 8.31747Z"
              fill="currentColor"></path>
          </svg>
        </div>
        <div class="title-link">
          <span v-for="(item, index) in titleFunc" :key="index" style="cursor: pointer;"
            @click.stop="handleNewPage(index)">
            <span style="margin-right: 2rem; color: #fff; font-weight: 600;font-size:1.2rem; overflow: hidden;">{{ item
              }}</span>
          </span>
        </div>
      </div>
      <!--|最右边|-->
      <div class="right-align">
        <!--右侧链接按钮-->
        <div class="title-notice">

          <span class="title-notice-single"
            style="display: flex; flex-flow: row nowrap; width: 5rem; justify-content: space-between;">
            <TrendAndCollect />
          </span>
        </div>
        <!--个人头像-->
        <div class="person-center">
          <div v-if="noticeNum.totalCount > 0" class=" notice-bubble-whole notice-bubble">
            <p v-if="noticeNum.totalCount < 99" style="color: #9ac9fb;">{{ noticeNum.totalCount }}</p>
            <p v-else>99+</p>
          </div>
          <!--点击头像后弹出-->
          <el-dropdown class="person-dropdown" id="person-dropdown" @command="handleCommand">
            <img class="person-avater common-avatar" id="person-dropdown-trigger" :src="userAvatar" />
            <template #dropdown>
              <el-dropdown-menu><!--NOTE html:  <DropdownMenu slot="list"> -->
                <el-dropdown-item v-if="isLogin" command="userCenter">个人中心</el-dropdown-item>
                <el-dropdown-item v-if="isLogin" command="message">我的消息</el-dropdown-item>
                <el-dropdown-item v-else>(oﾟvﾟ)ノ</el-dropdown-item> <!--v-else-->
                <el-dropdown-item command="logout" divided>
                  <p v-if="isLogin">退出</p>
                  <p v-else>登录</p>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--内容-->
    <div class="content">
      <!-- NOTE Vue的router-view的v-slot特殊语法，允许从路由视图获得组件Component并渲染-->
      <router-view v-slot="{ Component, route }" :key="$route.fullPath">
        <!--NOTE 使用 Vue 的过渡效果，通过 <transition> 组件包裹 <keep-alive> 和 <component> 元素，以实现路由切换时的动画效果-->
        <Transition name="move" mode="out-in">
          <!--XXX 涉及到频繁切换主页，试试-->
          <keep-alive>
            <component :is="Component" :key="route.path"></component>
          </keep-alive>
        </Transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router"
import { useRefreshToken } from '@/store/token'
import { useUserInfo } from "@/store/userInfo"
import { fetchNoticeNum } from "@/api/notice"
import request from "@/api/index"
import TrendAndCollect from '@/pages/home/TrendAndCollect'
const noticeNum = ref({ // 各类消息通知的数量
  totalCount: 20,
  chatCount: 10,
  commentCount: 3,
  likeCount: 7,
  dynamicVideoCount: 1
})
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const messageIcon = require('@/assets/img/message.svg')
console.log('userId', userId);
let user = {
  id: userId,
  name: userInfo.getName(),
  avatar: userInfo.getAvatar()
}
console.log('user', user);
const userAvatar = ref(require("@/assets/img/user/black_user.jpeg"))
const router = useRouter()
const isLogin = userId > 0 ? true : false
const titleFunc = ["首页"] // "论坛", "直播", "商城"
/**
 * 点击回到顶端
 */
const scollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth' // 使用smooth滚动效果，如果浏览器不支持，会自动回退到不带smooth的滚动
  })
}
/**
 * 论坛等跳转
 */
const handleNewPage = (index) => {
  let targetPath = ''
  switch (index) {
    case 0: {
      targetPath = '/main'
      break
    }
    case 1: {
      targetPath = '/forum'
      break
    }
    case 2: {
      targetPath = '/live'
      break
    }
    case 3: {
      targetPath = '/shop'
      break
    }
  }
  console.log('currentRoute', router.currentRoute.value.path)
  if (router.currentRoute.value.path != targetPath) {
    const routeURL = router.resolve({
      path: targetPath,
    })
    window.open(routeURL.href, '_blank')
  } else {
    location.reload() // 刷新页面
  }
}
/**
 * 下拉栏跳转
 * @param command string|number|string类型
 */
const handleCommand = (command) => {
  switch (command) {
    case 'userCenter': {
      const routeURL = router.resolve({
        path: `/userCenter/myItem/${user.id}`,
      })
      window.open(routeURL.href, '_blank')
      break;
    }
    case 'message':
      const routeURL = router.resolve({
        path: `/message/Reply/${user.id}`,
      })
      window.open(routeURL.href, '_blank')
      break;
    case 'logout':
      ElMessage.success('登出成功')
      userInfo.setAll("", 0, "")
      const refreshTokenStore = useRefreshToken()
      refreshTokenStore.shortToken = ''
      refreshTokenStore.isTokenPolling = false
      request.defaults.headers.common['shortauthorization'] = ''
      router.push('/login')
      break;
    default:
      break;
  }
}
onMounted(async () => {
  if (isLogin) {
    console.log('获取用户信息成功', user.avatar);
    noticeNum.value = await fetchNoticeNum(user.id)

    userAvatar.value = user.avatar

  }
})
</script>

<style lang="scss" scoped>
@import url(@/assets/css/dropdown.css);

.main-container {
  min-height: 100%;
  position: relative;
  display: flex;
  flex-flow: column nowrap;
}

.header {
  width: 100%;
  height: 7rem;
  margin: -4.3rem 0;
  position: fixed;
  /*回到顶端*/
  background: linear-gradient(50deg, rgba(2, 0, 36, 1) 0%, rgba(94, 73, 248, 1) 0%, rgb(119, 203, 242) 100%);
  z-index: 99;
  box-shadow: 0 0.5rem 20px 1px #79b1ec6e;
  cursor: pointer;
}

.header .left-align {
  display: flex;
  align-items: center;
}

.zhuzhan-icon {
  display: none;
}

.title-notice {
  display: none;
  margin-right: 15rem;
  min-width: 8rem;
}

.header .title-notice .title-notice-single {
  display: flex;
  flex-direction: column;
  margin-right: 4rem;
}

.title-notice-single>span:first-child {
  margin-right: 0.1rem;
}

.header .h1-new {
  /*标题*/
  font-weight: 900;
  font-size: 2.5rem;
  position: inherit;
  margin-left: 2rem;
  margin-right: 1rem;
  overflow: hidden;
  color: #ffffff;
}

.header .title-link {
  display: none;
}

@media screen and (min-width: 600px) {
  .title-notice {
    display: flex;
    flex-flow: row nowrap;
  }
}

@media screen and (min-width:1020px) {
  .zhuzhan-icon {
    margin-left: 3rem;
    vertical-align: -0.5rem;
    color: #fff;
    display: inline-block;
  }

  .header .title-link {
    margin-left: 0.5rem;
    display: flex;
  }
}

.header .person-center {
  position: absolute;
  right: 2rem;
  top: 1.5rem;
}

.header .person-avater {
  position: relative;
  width: 4rem;
  height: 4rem;
}

.content {
  position: absolute;
  top: 2.5rem;
  width: 100%;
  min-height: 100%;
}

.notice-bubble {
  width: 1.8rem;
  height: 1.8rem;
  right: -0.5rem;
}
</style>