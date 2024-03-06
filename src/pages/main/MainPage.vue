<template>
  <!--回到顶端的按钮-->
  <div id="back-top-btn-id" class="back-top-btn detail-btn-chosen common-btn-center based-box" @click="scrollToTop()">返回顶部</div>
  <div class="main-page">
    <div class="first-level">
      <div class="recommendation">
      <span class="recommendation-item" @click="turnToRank()"><div class="img-bg" style="background-color: #49a9f8;"><img src="@/assets/img/rank.svg" /></div><p>动 态</p></span>
      <span class="recommendation-item" @click="turnToHot()"><div class="img-bg" style="background-color: rgb(205, 110, 252);"><img src="@/assets/img/fire.svg" /></div><p>热 门</p></span>
      </div> 
      <SearchPanel class="search-box flex-based-container"/>
    </div>
    <template id="video-box-id" class="video-box">
    <!--NOTE 设每行有4个元素-->
      <video-small-wrap v-for="(item, index) in videoView" :key="index">
        <VideoCard :videoInfo="item" />
      </video-small-wrap>
    </template>
    <footer style="margin-bottom: 5rem; color:#11457da9;">
      <div v-if="downNew" style="height: 4rem;"><aLoadingVue /></div>
      <div v-else> 您已看完所有视频~ </div>
    </footer>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref, watch, defineAsyncComponent } from 'vue'
import VideoCard from '@/components/video/VideoCard.vue'
import SearchPanel from '@/components/search/SearchPanel.vue'
import VideoDetailList_test from '@/assets/data/videoViewList_test.json'
import { useRouter } from "vue-router"
import { getVideoBig } from "@/api/video"
import { fetchUserInfo } from "@/api/user"
import { useUserInfo } from '@/store/userInfo'
import Debounce from '@/static/debounce'
const debounce = new Debounce() // 防抖
const userInfo = useUserInfo() // 保存登录信息
const userId = userInfo.getId()
const startLoc = ref(1) // 当前位置0
const eachTimeVideos = 10 // 每次获取的视频数
const downNew = ref(false) // 触底加载 
const isUpdating = ref(false) // 是否在处理触底加载
const router = useRouter()
const aLoadingVue = defineAsyncComponent(()=>
  import ("@/components/public/aLoading")
)
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth' // 使用smooth滚动效果，如果浏览器不支持，会自动回退到不带smooth的滚动
  })
}
/**
 * 获取视频数据
 */
const getData = async() => {
  const tmp = await getVideoBig(startLoc.value) 
  videoView.value = tmp || VideoDetailList_test
}
const updateData = async() => {
  if(isUpdating) {
    return
  }
  isUpdating = true
  startLoc += eachTimeVideos
  const tmp = await getVideoBig(startLoc.value) 
  const probableData = tmp||VideoDetailList_test
  if (!videoView.value.includes(probableData)) {
    videoView.value.push(probableData)
  }
  downNew.value = false
  isUpdating.value = false
}
watch(downNew, async(newIndex, oldIndex) => {
  if(newIndex) {
    await debounce.debounceEnd(5)
    window.addEventListener("updateWhenBottom", debounce) 
  }
})
// 页面是否下滑
const isTop = () => {
  if(window.pageYOffset>100 || window.scrollY>100 || document.documentElement.scrollTop>100){
    return true
  }
}
/**
 * 触底加载
 */
const loadWhenBottom = () => {
  if(document.documentElement.clientHeight+window.scrollY>=document.documentElement.scrollHeight){
    downNew.value = true
  } else {
    downNew.value = false
  }
} 
/**
 * 视频展示实现
 *  */
const videoView = ref([])  // NOTE 将传给子组件的
/**
 * 跳转HOT页面
 */
const turnToHot = () => {
  router.push('/hot')
}
/**
 * 跳转Rank（改为动态）页面
 */
const turnToRank = () => {
  router.push('/trend')
}
/**
 * 已登录的用户数据
 */
const getUserDetail = async(userId) => {
  const loginUserInfo = await fetchUserInfo(userId, userId)
  // console.log("啊", loginUserInfo)
  userInfo.setAll(loginUserInfo.name, loginUserInfo.id, loginUserInfo.avatar )
}
// 获取数据
onMounted(() => {
  // 登录用户信息
  if (userId>0) {
    getUserDetail(userId)
  } 
  // 视频
  getData()
  const videoBoxElem = document.getElementById('video-box-id')
  // getVirtualList.observe(videoBoxElem)
  // 返回顶部
  let scrollToTopButton = undefined
  scrollToTopButton = document.getElementById('back-top-btn-id')
  const scrollToTopByBtn = () =>{
    if(scrollToTopButton){ // NOTE 这里要用两层嵌套，1层的&&不够
      if(isTop()){ // 为兼容浏览器，使用pageYOffset而非scrollY
      scrollToTopButton.style.display = 'flex'
      } else {
        scrollToTopButton.style.display = 'none'
      }
    }
  }
  window.addEventListener('scroll', scrollToTopByBtn)
  window.addEventListener('scroll', loadWhenBottom)
})

onBeforeUnmount(()=>{
  window.removeEventListener('scroll', scrollToTopByBtn)
})

</script>

<style scoped>
@import "@/assets/css/videoBox.scss";
.main-page{
  display: flex;
  flex-direction: column;
}
.back-top-btn {
  position: fixed;
  display: none;
  color: #6392c3;
  font-size: 1.2rem;
  font-weight: 600;
  width: 7rem;
  height: 3rem;
  top: 12rem;
  right: 3rem;
  z-index: 99;
}
.first-level{
  width: 5vw;
}
.recommendation .recommendation-item:first-child{
margin-left: 1.5rem;
margin-right: 2.5rem;
}
@media screen and (min-width:1020px){
  .first-level{
    width: 102rem;
    margin-right: 2rem;
  } 
  .recommendation .recommendation-item:first-child{
    margin-left: 2rem;
    margin-right: 4rem;
  }
}
.first-level{
  position: absolute;
  margin: 2rem 1rem;
  min-width: 46.5rem;
  margin-right: 2rem;
  padding-left: 1rem;
  padding-right: 1rem;
  box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
  border-radius: 10px;
  display: flex;
  flex-flow:row nowrap;
  align-items: center;
}
.recommendation{
  width: 15rem; 
  margin-right: 1rem;
  display: flex;
  flex-flow: row nowrap;
}
.recommendation .recommendation-item{
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  justify-content: center;
  height: 3rem;
  cursor: pointer;
}
.recommendation-item > .img-bg{
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  padding: 0.5rem;
  background-color: #19449a;
}
.recommendation-item > p{
  font-size: 0.9rem;
  margin-top: 0.2rem;
  font-weight:700;
  color: #11187da9; /*文字浅蓝*/
}
.search-box{
  min-width: 33rem;
  height: 6rem;
  z-index: 5;
}
.recommendation > img{
  width: 2rem;
  height: 2rem;
  background: #000;
  border-radius: 50%;
}
</style>