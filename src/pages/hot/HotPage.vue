<template>
    <!--热点页面-->
    <div class="left-right-panel hot-page">
        <!--左侧内容-->
        <section class="left-content">
            <!--分类显示-->
            <!--第一层分类-->
            <div class="second-category flex-based-container">
                <div v-for="item in typeList" :key="item.id"  @click="changeChosenItem(typeList, item)"
                class="detail-btn-chosen common-based-btn second-category-item hot-first-category-item"
                :class="{'tags-and-labels':item.isChosen}">
                    {{ item.name }}
                </div>
            </div>
            <!--第二层分类-->
            <div v-if="currentSecondList.length!=0" class="hot-second-category first-category flex-based-container">
                <div v-for="subItem in currentSecondList" :key="subItem.id"
                class="first-category-item change-color-btn" @click="changeChosenItem(currentSecondList, subItem)">
                    {{ subItem.name }}
                </div>
            </div>
            <!--推荐语-->
            <div class="flex-left-container">
                <img :src="currentFirst.icon" style="margin: 1.5rem 1rem; width:2rem;height: 2rem;"/>
                <div class="based-box fade-father-box recommend-bg" style="color: {{ currentFirst.recommendationColor }}">
                    <p v-for="(char, index) in currentFirst.recommendation" :key="index" class="fade-son-box"
                    :style="{ animationDelay: `${index * 0.15}s` }">
                        {{char}} </p>
                </div>
            </div>
            <!--数据显示-->
            <div style="margin-top: 2rem;">
                <!--XXX 设每行有2个元素-->
                <el-row :gutter="8" v-for="(item, index) in dataList" :key="index" style="margin-bottom:2rem;"><!--NOTE gutter是两列的间隔-->
                    <el-col :span="12"><!--span一个栅格所占的列数-->
                        <VideoCard :videoInfo="item" :boderShadow="false" />
                    </el-col>
                    <el-col :span="12"><!--span一个栅格所占的列数-->
                        <VideoCard :videoInfo="item" :boderShadow="false" />
                    </el-col>
                </el-row>
            </div>
        </section>
        <!--右侧内容-->
        <section class="right-content">
            <!--排行内容-->
            啊
        </section>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import VideoCard from '@/components/video/VideoCard.vue'
import VideoDetailList_test from '@/assets/data/videoViewList_test.json'
const typeList = [{
	id: 1,
	name: "综合热门",
    icon: require("@/assets/img/hotPage/hot.png"),
    recommendation: "各个领域中新奇好玩的优质内容都在这里~",
    recommendationColor: "#4d2494",
    isChosen: true,
	children: []
},{
    id: 2,
	name: "入站必刷",
    icon: require("@/assets/img/hotPage/classic.png"),
    recommendation: "",
    isChosen: false,
	children: [{
		id: 1,
        name: "你好",
        isChosen: true,
    },{
        id: 2,
        name: "最近一周",
        isChosen: false,
    },{
        id: 3,
        name: "最近一周",
        isChosen: false,
    },{
        id: 4,
        name: "最近一周",
        isChosen: false,
    }]
},{
    id: 3,
	name: "排行榜",
    icon: require("@/assets/img/hotPage/rank.png"),
    isChosen: false,
    recommendation: "",
	children: [{
		id: 1,
        name: "最近一周",
    }]
}]
const dataList = ref([])
const currentFirst = ref(typeList[0])
const currentSecondList = ref([])
// 修改当前分类
const changeChosenItem = async(array, val) => {
    array.forEach(category => 
        category.isChosen = false
    )
    val.isChosen = true
    if(val === typeList){ // 如果是一级分类，进入以下
        currentFirst.value = val
        currentSecondList.value = val.children
        // 刷新获取新数据
    }else{
        currentSecondList.value = val
        // 刷新获取新数据
    }
}

onMounted(()=>{
    dataList.value = VideoDetailList_test
    currentSecondList.value = currentFirst.value.children //  currentFirst.children 在组件初始化时尚未被赋值；html相关部分会报错
})
</script>

<style scoped>
@import "@/assets/css/category.scss";
.hot-page {
    padding: 1rem;
}
.hot-first-category-item {
    font-weight: 600;
}
.hot-second-category {
    margin-top: 2rem;
}
.recommend-bg {
    width: auto;
    padding: 0.5rem;
}
</style>