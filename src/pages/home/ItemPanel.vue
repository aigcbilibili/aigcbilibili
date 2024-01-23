<!--面板-->
<template>
    <!--动态-->
    <div v-if="panelType==='trend'" class="trend-panel">
        <!--动态头-->
        <div class="change-banner">
            <div class="change-banner-item" v-for="(item,index) in trendType" :key="index">
                <p @click="changeType(trendType,item)" :class="{'trend-type-chosen': item.isChosen}">{{item.name}}</p>
            </div>
        </div>
        <div class="horizontal-divided-line"></div>
        <!--动态内容-->
        <div class="trend-content">
            <!--未读-->
            <p v-if="unread.length===0" style="height: 3rem; display: flex; align-items:center;justify-content: center;color: #11457da9; ">没有未读动态</p>
            <!--已读-->
            <div v-else>
                <div class="horizontal-divided-line small-video-line"></div>
            </div>
            
            
        </div>
    </div>
    <!--收藏-->
    <div v-else-if="panelType==='collect'" class="collect-panel">
        <!--收藏左-->
        <div class="collect-folder-left">
            <div v-for="(folder, index) in collectFolderData" :key="index">
            <div @click="changeType(collectFolderData, folder)" :class="{'main-first-color collect-folder-chosen': folder.isChosen}">
                <p style="margin-top:0.3rem;">{{folder.name}}</p>
            </div>
            </div>
        </div>
        <div class="vertical-divided-line"></div>
        <!--收藏右-->
        <div class="collect-content-right">
            <div v-for="(content, index) in collectData" :key="index">
            <div></div>
            <div>
                <p class="collect-content-title">{{content.name}}</p>
            </div>
            </div>
        </div>
    </div>
    <!--历史-->
    <div v-else class="history-panel">
        <div class="change-banner">
            <div class="change-banner-item" v-for="(item,index) in trendType" :key="index">
                <p @click="changeTrendType(trendType,item)" :class="{'trend-type-chosen': item.isChosen}">{{item.name}}</p>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, defineProps, onMounted, onBeforeUnmount } from 'vue'
import { getVideoSmall } from '@/api/video'
const props = defineProps({
    itemType:{
        type: String,
        required: true
    }
})
// 当前panel的类型
const panelType = ref(props.itemType)
/**
 * 分类：动态和历史共用
 */
const trendType = reactive([{
  id: 1,
  name: "视频动态",
  askURL: "",
  isChosen: true
},{
  id: 2,
  name: "直播",
  askURL: "",
  isChosen: false
},{
  id: 3,
  name: "专栏",
  askURL: "",
  isChosen: false
}])
const changeType = (types,item) => {
    types.forEach((type)=>{
        type.isChosen = false
    })
    item.isChosen = true
}
/**
 * 获取数据
 */
const videoSmallData = ref([]) // 未整理数据
const unread = ref([]) // 已读
const read = ref([]) // 未读
onMounted(()=>{
    // 默认调用当前
    if(panelType.value==='collect'){
        // 首先获取收藏夹
        
        // 然后获取收藏夹中视频
        const a  = 1
    }else{
        videoSmallData.value = getVideoSmall(panelType.value, 1)
    }
})
onBeforeUnmount(()=>{
    changeType(trendType, trendType[0])
})
</script>

<style lang="scss" scoped>
.trend-panel{
  width: 25rem;
  height: 25rem;
}
.change-banner{
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  height: 3rem;
  width: 20rem;
}
.change-banner .trend-type-chosen{
  color: #449cf4f2;
}
.change-banner > .change-banner-item{
  cursor: pointer;
}
.change-banner > .change-banner-item:not(:last-child){
  margin-right: 3rem;
  font-size: 1rem;
}
.change-banner > .change-banner-item:active{
  transform: scale(0.9, 0.9);
  -webkit-transform: scale(0.9, 0.9);
  -o-transform: scale(0.9, 0.9);    
  -ms-transform: scale(0.9, 0.9);
  color: #449cf4f2;
}
.collect-panel{
  border-radius: 20px;
  top: 5rem;
  right: 3rem;
  width: 25rem;
  height: 25rem;
  display: flex;
}
.collect-panel .collect-folder-left{
  flex-basis: 8rem;
  flex-grow: 0;
  flex-shrink: 1;
  text-align: center;
}
.collect-folder-left >div{
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.collect-content-right >div{
  height: 5rem;
  display: flex;
  flex-flow: row nowrap;
  align-items: center;
  justify-content: center;
}
.collect-panel .collect-folder-chosen{
  border-radius: 2rem;
  width: 5rem;
  /* margin-right: 1rem; */
  height: 100%;
  color: #fff;
}
.collect-panel .collect-content-right{
  flex-basis: 5rem;
  flex-grow: 1; 
  flex-shrink: 1;
}
</style>