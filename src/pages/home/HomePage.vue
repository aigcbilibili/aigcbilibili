<template>
  <div class="main-container">
    <div class="header" @click="scollToTop()"><!--NOTE vue3不支持@on-click绑定-->
      <!--|最左边|-->
      <div class="left-align">
        <!--标题-->
        <div class="h1-new">FakeLi</div>
        <!--左侧链接-->
        <div class="title-link">
          <span v-for="(item, index) in titleFunc" :key="index">
            <span style="margin-right: 2rem; color: #fff; font-weight: 600;font-size:1.2rem; overflow: hidden;"
            class="">{{item.title}}</span>
          </span>
        </div>
      </div>
       <!--|最右边|-->
      <div class="right-align">
      <!--右侧链接按钮-->
      <div class="title-notice">
        <span class="title-notice-single"  style="display: flex; flex-flow: row nowrap; width: 5rem;">
         <TrendAndCollect />
        </span>
      </div>
      <!--个人头像-->
      <div class="person-center">
        <!--点击头像后弹出-->
        <el-dropdown class="person-dropdown" id="person-dropdown" @command="handleCommand">
          <img class="person-avater" src="@/assets/img/avater.png" id="person-dropdown-trigger"/>
          <template #dropdown>
            <el-dropdown-menu><!--NOTE html:  <DropdownMenu slot="list"> -->
              <el-dropdown-item command="userCenter">个人中心</el-dropdown-item>
              <el-dropdown-item command="message">我的消息</el-dropdown-item>
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
      <router-view v-slot="{ Component }">
        <!--NOTE 使用 Vue 的过渡效果，通过 <transition> 组件包裹 <keep-alive> 和 <component> 元素，以实现路由切换时的动画效果-->
        <Transition name = "move" mode="out-in">
          <!--XXX 涉及到频繁切换主页，试试-->
          <keep-alive>
            <component :is="Component"></component>
          </keep-alive>
        </Transition>
      </router-view> 
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { onMounted, onBeforeMount } from 'vue'
import { useRouter } from "vue-router"
import { useUserInfo} from "@/store/userInfo"
import TrendAndCollect from '@/pages/home/TrendAndCollect'

const router = useRouter()

const isLogin = ()=>{
  const userInfo = useUserInfo()
  let {token} = userInfo
  if(typeof token=='string'&&token!='' ){
    return true
  }else{
    return false
  }
}

onBeforeMount(() =>{

})

/**
 * 点击回到顶端
 */
const scollToTop = (() =>{

})

/**
 * 下拉栏跳转
 * @param command string|number|string类型
 */
const handleCommand = (command) => {
  switch(command) {
    case 'userCenter':{
      let id = 1
      router.push({
        path: `/userCenter/${id}`,
        query: { 
          id: id,
          name: '',
          avatar: '',
          followingNum:100,
          fansNum: 100,
          intro: 100,
          isSame: 100
        }
      })
      break;
    }
    case'message':
      console.log('message')
      break;
    case 'logout':
      ElMessage.success('登出成功')
      router.push('/login')
      break;
    default:
      break;
  }
}

onMounted(()=>{
  // 长轮询获取个人和通知信息，若token过期则提示并返回登录页面
  const longPullData = setInterval(() =>{
    
  })
})

</script>

<style scoped >
@import url(@/assets/css/dropdown.css);
.main-container{
  min-height: 100%;
  position: relative;
  display: flex;
  flex-flow: column nowrap;
}
.header{
  width: 100%;
  height: 7rem;
  margin: -4.3rem 0;
  position: fixed; /*点击回到顶端*/
  background: linear-gradient(50deg, rgba(2,0,36,1) 0%, rgba(94,73,248,1) 0%, rgb(119, 203, 242) 100%);
  z-index: 99;
  box-shadow: 0 0.5rem 20px 1px #79b1ec6e;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.header .left-align{
  display: flex;
  align-items: center;
}
.title-notice{
  display: none;
  margin-right: 10rem;
  min-width: 8rem;
}
.header .title-notice .title-notice-single{
  display: flex;
  flex-direction: column;
  margin-right: 4rem;
}
.title-notice-single > span:first-child{
  margin-right: 0.1rem;
}
.header .h1-new{ /*标题*/
  font-weight: 900;
  font-size: 2.5rem;
  position: inherit;
  margin-left: 2rem;
  margin-right: 1rem;
  overflow: hidden;
  color: #ffffff;
}
.header .title-link{
  margin-left: 3rem;
}
@media screen and (min-width: 600px) {
  .title-notice{
    display: flex;
    flex-flow: row nowrap;
  }
}
@media screen and (min-width:1020px){
  .header .title-link{
    margin-left: 3rem;
    display: flex;
  }  
}
.header .person-center{
  position: absolute;
  right: 2rem;
  top: 1.5rem;
}
.header .person-avater{
  border-radius: 50%;
  position: relative;
  width: 4rem;
  height: 4rem;
}
.content{
  position: absolute;
  top: 2.5rem;
  width: 100%;
  min-height: 100%;
}
</style>