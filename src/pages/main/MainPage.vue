<template>
  <div class="main-page">
    <div class="first-level">
      <div class="recommendation">
      <span class="recommendation-item" @click="turnToRank()"><div class="img-bg" style="background-color: #49a9f8;"><img src="@/assets/img/rank.svg" /></div><p>排 名</p></span>
      <span class="recommendation-item" @click="turnToHot()"><div class="img-bg" style="background-color: rgb(205, 110, 252);"><img src="@/assets/img/fire.svg" /></div><p>热 门</p></span>
      </div> 
      <SearchPanel class="search-box"/>
    </div>
    <template class="video-box">
    <!--NOTE 设每行有4个元素-->
          <video-small-wrap v-for="(item, index) in videoView" :key="index" :class="{'not-last-in-row':(index+1)%2!=0}">
            <VideoCard :videoInfo="item" />
          </video-small-wrap>
        <!-- </DynamicScrollerItem> -->
      <!-- </template> -->
    </template>
    <footer style="margin-bottom: 5rem; color:#11457da9;">
      您已看完所有视频~
    </footer>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import VideoCard from '@/components/VideoCard.vue'
import VideoDetailList_test from '@/assets/data/videoViewList_test.json'
import SearchPanel from '@/components/search/SearchPanel.vue'
import { useRouter } from "vue-router"
import { getVideoBig } from "@/api/video"
const router = useRouter()
/**
 * 视频展示实现
 *  */
const videoView = ref([])  // NOTE 将传给子组件的
// 获取视频数据
onMounted(async()=>{
  const tmp = await getVideoBig() 
  videoView.value = tmp || VideoDetailList_test
})

/**
 * 滚动实现虚拟列表+懒加载
 *  */

/**
 * 跳转HOT页面
 */
const turnToHot = () => {
  router.push('/hot')
}

/**
 * 跳转Rank页面
 */
const turnToRank = () => {
  router.push('/rank')
}
</script>

<style scoped>
.main-page{
  display: flex;
  flex-direction: column;
}
@media screen and (min-width:0px){
  .first-level{
    width: 5vw;
  }
  .recommendation .recommendation-item:first-child{
  margin-left: 1.5rem;
  margin-right: 2.5rem;
  }
  .video-box > video-small-wrap{
    margin-top: 1rem;
    margin-left: 1.5rem;
    margin-right: 1.5rem;
  }
}
@media screen and (min-width:1020px){
  .first-level{
    width: 102rem;
    margin-right: 2rem;
  } 
  .video-box > video-small-wrap{
    margin-left: 2.6rem;
    margin-right: 2.6rem;
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
  display: flex;
  align-items: center;
  flex-flow:row nowrap;
  z-index: 5;
}
.video-box{
  margin: 10rem 2rem;
  min-height: 100%;
  display: flex;
  flex-wrap: wrap;
}
.recommendation > img{
  width: 2rem;
  height: 2rem;
  background: #000;
  border-radius: 50%;
}

/* 实现非最后一行的规则 */
.video-box > video-small-wrap:not(:last-child){
  margin-bottom: 2rem;
}
</style>