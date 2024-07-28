<!--面板-->
<template>
  <!--动态和历史-->
  <div v-if="panelType === 'trend' || panelType === 'history'" class="trend-panel">
    <!--动态头-->
    <div class="change-banner flex-center-container">
      <div class="change-banner-item" v-for="(item, index) in trendType" :key="index">
        <p @click.stop="changeType(trendType, item)" :class="{ 'trend-type-chosen': item.isChosen }">{{ item.name }}</p>
      </div>
    </div>
    <div class="horizontal-divided-line" style="margin-bottom: 0.5rem;"></div>
    <!--动态内容-->
    <div class="trend-content">
      <el-scrollbar style="height: 22rem;">
        <!--未读数据-->
        <!--没有未读数据-->
        <div v-if="unread.length === 0 && panelType === 'trend'" style="height: 3rem; display: flex; align-items:center;
              justify-content: center;color: #11457da9; ">没有未读动态
        </div>
        <!--有未读数据-->
        <div v-else-if="panelType === 'trend'">
          <div class="trend-history-title flex-center-container">未读动态</div>
        </div>
        <div v-for="(item, index) in unread" :key="index" class="trend-history-item flex-center-container">
          <smallVideoVue :videoRecord="item" @click="turnToVideoDetail(item.videoId, item.upId)"></smallVideoVue>
        </div>
        <!--中间线-->
        <div v-if="panelType === 'trend'" class="horizontal-divided-line small-video-line"></div>
        <!--已读数据-->
        <!--没有已读数据-->
        <div v-if="read.length === 0 && panelType === 'trend'" style="height: 3rem; display: flex; align-items:center;
              justify-content: center;color: #11457da9; ">没有已读动态
        </div>
        <!--有已读数据-->
        <div v-else-if="panelType === 'trend'">
          <div class="trend-history-title flex-center-container" style="margin-bottom: 0.1rem;">已读动态</div>
          <div v-for="(item, index) in read" :key="index" class="trend-history-item flex-center-container">
            <smallVideoVue :videoRecord="item" @click="turnToVideoDetail(item.videoId, item.upId)"></smallVideoVue>
          </div>
        </div>
      </el-scrollbar>
    </div>
  </div>
  <!-- 消息 -->
  <div v-else-if="panelType === 'message'" class="message-panel">
    <div style="margin-bottom: 10px;">
      <el-button type="text" size="large" link @click="pushMessage(1)">回复我的</el-button>
      <span v-if="props.dataTmp.commentCount > 0" class="bg_yuan">{{ props.dataTmp.commentCount }}</span>
    </div>

    <div style="margin-bottom: 10px;">
      <el-button type="text" size="large" link @click="pushMessage(2)">点赞我的</el-button>
      <span v-if="props.dataTmp.likeCount > 0" class="bg_yuan">{{ props.dataTmp.likeCount }}</span>
    </div>

    <div>
      <el-button type="text" size="large" link @click="pushMessage(0)">我的消息</el-button>
      <span v-if="props.dataTmp.chatCount > 0" class="bg_yuan">{{ props.dataTmp.chatCount }}</span>
    </div>


  </div>
  <!--收藏-->
  <div v-else class="collect-panel">
    <!--收藏左-->
    <div class="collect-folder-left">
      <!-- <div v-if="collectFolderData.length > 0">
        <div v-for="(folder, index) in collectFolderData" :key="index">
          <div @click.stop="currentCollectFolder = folder.id"
            :class="{ 'main-first-color collect-folder-chosen': folder.id === currentCollectFolder }">
            <p style="margin-top:0.3rem;">{{ folder.name }}</p>
          </div>
        </div>
      </div>
      <div v-else>
        <img src="@/assets/img/utils/noData.png" style="width: 12rem; height: 12rem;" />
      </div> -->
      <el-tabs tab-position="left" style="height: 100%" class="demo-tabs" @tab-change="handleChange"
        v-model="currentCollectFolder">
        <el-tab-pane :label="folder.name" v-for="(folder, index) in collectFolderData" :key="index"
          :name="folder.id"></el-tab-pane>
      </el-tabs>
    </div>
    <!--收藏右-->
    <div class="collect-content-right">
      <div v-if="collectVideoData.length > 0">
        <div v-for="(content, index) in collectVideoData" :key="index" class="collect-item"
          @click.stop="psuhVideoDetail(content)">
          <div class="left-content">
            <img :src="content.videoCover" alt="" style="width: 100%;height: 100%;">
          </div><!--TODO 没写完这里-->
          <div class="right-content">
            <p>{{ content.videoName }}</p>
            <div class="name">作者：{{ content.authorName }}</div>
          </div>
        </div>
      </div>
      <div v-else class="flex-center-container" style="height: 95%;">
        等待新的收藏数据~
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  ref, reactive, defineProps, watch, onMounted,
  onBeforeUnmount, defineAsyncComponent
} from 'vue'
import { getVideoSmall } from '@/api/video'
import { fetchCollection, getCollectGroupByid } from "@/api/like_and_collect"
import { useUserInfo } from "@/store/userInfo"
import { editTrendToRead } from "@/api/notice"
import { useRouter } from 'vue-router'
const router = useRouter()
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const props = defineProps({
  itemType: {
    type: String,
    required: true
  },
  dataTmp: {
    type: Object,
  }
})
const emit = defineEmits(['changeDYState'])
// 当前panel的类型
const panelType = ref(props.itemType)
const collectFolderData = ref([]) // 收藏夹数据
const currentCollectFolder = ref('') // 当前收藏夹id
const collectVideoData = ref([]) // 收藏夹中视频数据
const videoSmallData = ref([]) // 未整理数据
const unread = ref([]) // 已读
const read = ref([]) // 未读
// 调用组件
const smallVideoVue = defineAsyncComponent(() =>
  import("./SmallVideo.vue")
)

const handleChange = (val) => {
  console.log('val', val);
}
// 跳转到视频详情页
const turnToVideoDetail = (videoId, upId) => {
  const routeURL = router.resolve({
    name: 'videoDetail',
    params: {
      videoId: videoId,
    },
    query: {
      upId: upId
    }
  })
  window.open(routeURL.href, '_blank')
}
/**
 * 分类：动态和历史共用
 */
const trendType = reactive([{
  id: 1,
  name: "视频",
  isChosen: true
},
  // {
  //   id: 2,
  //   name: "直播",
  //   isChosen: false
  // },{
  //   id: 3,
  //   name: "专栏",
  //   isChosen: false
  // }
])
const changeType = (types, item) => {
  types.forEach((type) => {
    type.isChosen = false
  })
  item.isChosen = true
}
const getData = async () => {
  // 默认调用当前
  if (panelType.value === 'collect') {
    // 首先获取收藏夹
    collectFolderData.value = await fetchCollection(userId)
    if (collectFolderData.value.length > 0) {
      currentCollectFolder.value = collectFolderData.value[0].id
      //然后获取收藏夹中视频
      // collectVideoData.value = await getVideoSmall(panelType.value, userId, currentCollectFolder.value)
    }
  } else {
    videoSmallData.value = await getVideoSmall(panelType.value, userId, 0)
    videoSmallData.value.forEach((video) => {
      if (video.status === 1) { // 已读
        read.value.push(video)
      } else {
        unread.value.push(video)
      }
    })
  }
}
// 使用watch更新收藏夹数据
watch(currentCollectFolder, async (newValue, oldValue) => {
  console.log('收藏夹的id改变了', newValue);
  if (newValue != oldValue) {
    collectVideoData.value = await getCollectGroupByid(newValue)
  }
})
/**
 * 获取数据
 */
onMounted(async () => {
  getData()
  // 将动态已读
  if (panelType.value === 'trend') {
    await editTrendToRead(userId);
    emit('changeDYState')
  }
})
onBeforeUnmount(() => {
  changeType(trendType, trendType[0])
})


const psuhVideoDetail = (item) => {
  console.log('pushVideoDetail', item);
  router.push({
    path: `/videoDetail/${videoId}?upId=${item.authorId}`
  })
}

const pushMessage = (val) => {
  if (userId === 0) return router.push({ path: '/login' })
  // val ? router.push({
  //   path: '/message/Reply/1'
  // }) : router.push({
  //   path: '/message/MyChat/1'
  // })
  if (val == 0) {
    router.push({
      path: `/message/MyChat/${userId}`
    })
  } else if (val == 1) {
    router.push({
      path: `/message/Reply/${userId}`
    })
  } else if (val == 2) {
    router.push({
      path: `/message/ThumbsUpMe/${userId}`
    })
  }
}

</script>

<style lang="scss" scoped>
$trend-and-history-width: 23rem;

.trend-panel {
  width: $trend-and-history-width;
  height: 25rem;

  .trend-history-title {
    color: #365a7c;
    font-weight: 600;
    padding-bottom: 0.3rem;
  }

  .trend-history-item {
    width: $trend-and-history-width;
    height: 3.2rem;
    margin-bottom: 0.8rem;
  }
}

.change-banner {
  height: 2rem;
  width: $trend-and-history-width;
  font-weight: 700;
}

.change-banner .trend-type-chosen {
  color: #449cf4f2;
}

.change-banner .change-banner-item {
  width: 30%;
  margin-top: -1rem;
  cursor: pointer;
}

.small-video-line {
  margin: 0.5rem 0;
}

.change-banner>.change-banner-item:active {
  transform: scale(0.9, 0.9);
  -webkit-transform: scale(0.9, 0.9);
  -o-transform: scale(0.9, 0.9);
  -ms-transform: scale(0.9, 0.9);
  color: #449cf4f2;
}

.message-panel {
  border-radius: 20px;
  width: 8rem;
  height: 80px;
  // display: flex;
}

.collect-panel {
  border-radius: 20px;
  top: 5rem;
  // right: 3rem;
  width: 25rem;
  height: 25rem;
  display: flex;
}

.collect-panel .collect-folder-left {
  flex: 0.3;
  text-align: center;
}

.collect-panel .collect-content-right {
  width: 100%;
}

.collect-folder-left>div {
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}


.collect-panel .collect-folder-chosen {
  border-radius: 2rem;
  width: 5rem;
  /* margin-right: 1rem; */
  height: 100%;
  color: #fff;
}

.collect-item {
  width: 100%;
  height: 80px;
  display: flex;
  border-radius: 5px;
  margin-bottom: 10px;
  overflow: hidden;
  box-shadow: inset 0 0 0 1px rgba(0, 0, 0, 0.1);

  .left-content {
    width: 90px;
    height: 100%;
  }

  .right-content {
    flex: 1;
    text-align: left;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 10px;

    p {
      font-size: 16px;
      font-weight: 700;
    }
  }


}


.bg_yuan {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-color: rgb(241, 133, 133);
  border-radius: 50%;
  text-align: center;
  line-height: 20px;
  color: #fff;
}
</style>