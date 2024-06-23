<template>
  <!--回到顶端的按钮-->
  <div id="back-top-btn-id" class="back-top-btn detail-btn-chosen 
  common-btn-center based-box" @click="scrollToTop()">返回顶部</div>
  <div class="common-box">
    <!--TODO：待删-->
    <p>
      <el-button type="primary" @click="turnToBigModel()">{{ logInfo }}</el-button>
    </p>
  </div>
  <div class="main-page">
    <SearchPanel class="search-box flex-based-container" />
    <template id="video-box-id" class="video-box">
      <!--NOTE 设每行有4个元素-->
      <video-small-wrap v-for="(item, index) in videoView" :key="index">
        <VideoCard :videoInfo="item" />
      </video-small-wrap>
    </template>
    <footer style="margin-bottom: 5rem; color:#11457da9;">
      <div v-if="downNew && isRemain" style="height: 4rem;">
        <aLoadingVue />
      </div>
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
import { ElMessage } from 'element-plus'
let startLoc = 1 // 当前位置0
let probableData = [] // 可能放入的新数据
let scrollToTopButton = document.getElementById('back-top-btn-id')
const logInfo = '大模型文生文、文生图、智能PPT 大模型体验'
const isRemain = ref(true) // 是否仍然有新数据
const debounce = new Debounce() // 防抖
const userInfo = useUserInfo() // 保存登录信息
const userId = userInfo.getId()
const eachTimeVideos = 10 // 每次获取的视频数
const downNew = ref(false) // 触底加载
// const isUpdating = ref(false) // 是否在处理触底加载
const router = useRouter()
const aLoadingVue = defineAsyncComponent(() =>
  import("@/components/public/aLoading")
)
// 跳转至大模型
const turnToBigModel = () => {
  if (userId === 0) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  const routeURL = router.resolve({
    path: `/message/MyChat/${userId}`,
  })
  window.open(routeURL.href, '_blank')
}
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth' // 使用smooth滚动效果，如果浏览器不支持，会自动回退到不带smooth的滚动
  })
}
const scrollToTopByBtn = (elemDom) => {
  if (elemDom) { // NOTE 这里要用两层嵌套，1层的&&不够
    if (isTop()) { // 为兼容浏览器，使用pageYOffset而非scrollY
      elemDom.style.display = 'flex'
    } else {
      elemDom.style.display = 'none'
    }
  }
}
/**
 * 获取视频数据
 */
const getData = async () => {
  const tmp = await getVideoBig(startLoc)
  videoView.value = tmp || VideoDetailList_test
}
const updateData = async () => {
  startLoc += eachTimeVideos
  probableData = await getVideoBig(startLoc)
  downNew.value = false
  if (probableData.length === 0) {
    isRemain.value = false // 没有剩余数据了
    ElMessage.success("休息下吧！没有更多数据了QAQ")
    window.removeEventListener('scroll', loadWhenBottom)
    return
  }

  // 过滤掉已存在videoView中的视频
  let uniqueVideos = probableData.filter(video =>
    !videoView.value.some(existingVideo => existingVideo.id === video.id)
  )
  videoView.value = [...videoView.value, ...uniqueVideos] // NOTE 使用push会有浅复制问题
}
watch(downNew, async () => {
  await debounce.debounceEnd(7)
  updateData()
  window.addEventListener("updateWhenBottom", debounce)
})
// 页面是否下滑
const isTop = () => {
  if (window.pageYOffset > 100 || window.scrollY > 100 || document.documentElement.scrollTop > 100) {
    return true
  }
}
/**
 * 触底加载
 */
const loadWhenBottom = () => {
  if (document.documentElement.clientHeight + window.scrollY >= document.documentElement.scrollHeight - 10) {
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
const getUserDetail = async (userId) => {
  const loginUserInfo = await fetchUserInfo(userId, userId)
  // console.log("啊", loginUserInfo)
  userInfo.setAll(loginUserInfo.name, loginUserInfo.id, loginUserInfo.avatar)
}
// 获取数据
onMounted(() => {
  // 登录用户信息
  if (userId > 0) {
    getUserDetail(userId)
  }
  scrollToTopButton = document.getElementById('back-top-btn-id')
  // 视频
  getData()
  // 返回顶部
  window.addEventListener('scroll', () => scrollToTopByBtn(scrollToTopButton))
  window.addEventListener('scroll', loadWhenBottom)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', () => scrollToTopByBtn(scrollToTopButton))
  window.removeEventListener('scroll', loadWhenBottom)
})

</script>

<style scoped>
@import "@/assets/css/videoBox.scss";

.main-page {
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


.recommendation .recommendation-item:first-child {
  margin-left: 1.5rem;
  margin-right: 2.5rem;
}

@media screen and (min-width:1020px) {
  .first-level {
    width: 100%;
    margin-right: 2rem;
    box-sizing: border-box;
  }

  .recommendation .recommendation-item:first-child {
    margin-left: 2rem;
    margin-right: 4rem;
  }
}

.first-level {
  position: relative;
  margin: 2rem;
  min-width: 46.5rem;
  margin-right: 2rem;
  padding-left: 1rem;
  padding-right: 1rem;
  box-shadow: 0.2rem 0.2rem 20px 1px #79b1ec6e;
  border-radius: 10px;
  display: flex;
  /* flex-flow: row nowrap; */
  justify-self: center;
  align-items: center;
}

.recommendation {
  width: 15rem;
  margin-right: 1rem;
  display: flex;
  flex-flow: row nowrap;
}

.recommendation .recommendation-item {
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  justify-content: center;
  height: 3rem;
  cursor: pointer;
}

.recommendation-item>.img-bg {
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  padding: 0.5rem;
  background-color: #19449a;
}

.recommendation-item>p {
  font-size: 0.9rem;
  margin-top: 0.2rem;
  font-weight: 700;
  color: #11187da9;
  /*文字浅蓝*/
}

.search-box {
  min-width: 33rem;
  height: 6rem;
  z-index: 5;
}

.recommendation>img {
  width: 2rem;
  height: 2rem;
  background: #000;
  border-radius: 50%;
}
</style>