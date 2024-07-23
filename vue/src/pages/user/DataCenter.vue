<template>
    <!--数据中心-->
    <div style="margin:0.5rem;">
        <div class="work-title">创作数据</div>
        <Content class="flex-column-left-max-container">
            <el-scrollbar style="margin: 1rem; width:99%; height: 25rem;">
            <!--切换和概括数据-->
            <div class="user-content-slide-btn">
                <div class="data-change-item btn-based" :class="{'btn-chose': dataType=0}"
                @click="dataType=0" >视频数据</div>
                <p> | </p>
                <div class="data-change-item btn-based" :class="{'btn-chose': dataType=1}"
                @click="dataType=1">图文数据</div>
            </div>
            <!--第1项：概括的数据-->
            <div class="">
                <p>概括数据</p>
                <div class="works-data-wrap flex-center-container">
                    <div v-for="(item, index) in data[dataType].children" :key="index">
                        <div class="works-data-item flex-column-center-container">
                            <p style="font-size: 1.1rem;">{{item.name}}</p>
                            <p style="margin-top: 0.2rem">{{item.data}}</p>
                        </div>
                    </div>
                </div>
            </div>
            <!--第2项：粉丝增长量-->
            <div class="">
                <p>粉丝增长量</p>
                <div class="">
                    <div id="chart-container"></div>
                </div>
            </div>
            <!--第3项：粉丝特点-->
            <div class="">
                <p>粉丝增长量</p>
                <div class="">
                    <div id="chart-container"></div>
                </div>
            </div>
        </el-scrollbar>
        </Content>
    </div>
</template>

<script setup>
import {ref, onMounted } from "vue"
// import echarts from 'echarts'
const dataType = ref(0) // 0：视频数据，1：图文数据
const data = [{
    name: "视频数据",
    children: [{
        name: "新增粉丝",
        data: 30        
    },{
        name: "播放量",
        data: 27    
    },{
        name: "评论",
        data: 20    
    },{
        name: "弹幕",
        data: 22    
    },{
        name: "点赞",
        data: 20  
    },{
        name: "分享",
        data: 18  
    },{
        name: "收藏",
        data: 15 
    },{
        name: "投币",
        data: 11  
    }]
},{
    name: "图文数据",
    children: [{
        name: "浏览量",
        data: 5 
    },{
        name: "评论数",
        data: 11 
    },{
        name: "点赞数",
        data: 46 
    },{
        name: "收藏数",
        data: 37 
    },{
        name: "分享数",
        data: 29
    },{
        name: "投币数",
        data: 29
    }]
}]
// 图片数据
</script>

<style lang="scss" scoped>
@import "@/assets/css/userPage.scss";
.work-title {
    font-weight: 600;
    font-size: 1.5rem;
}
.data-change-item {
    font-size: 1rem;
    &:first-child {
        margin-right: 0.5rem;
    }
    &:last-child {
        margin-left: 0.5rem;
    }
}
.data-change-here {
    z-index: 1;
}
.works-data-wrap{
    margin-top: 1.5rem;
    flex-wrap: wrap !important;
    .works-data-item {
        width: 6rem;
        height: 4rem;
        background-color: #bfc4fc;
        color: #1e1581;
        margin: 1rem 1rem;
        border-radius: 10px;
    }
}
@media screen and (min-width: 1020px) {
    .works-data-wrap{
        .works-data-item {
            transform: scale(1.3, 1.3);
            margin: 2rem 2rem;
        }
    }
}
</style>