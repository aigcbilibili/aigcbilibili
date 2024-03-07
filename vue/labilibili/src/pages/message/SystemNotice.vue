<!--系统通知-->
<template>
    <div class="wrap">
        <p class="component main-title font-fifth-color">系统通知</p>
        <el-scrollbar style="height: 92%;">
        <div class="component content">
            <div v-if="noticeData.length!=0">
                <div v-for="(notice, index) in noticeData" :key="index">
                    <div class="item decoration-box">
                        <div class="notice-first-layer">
                            <p class="item-title notice-single-title">{{notice.title}}</p>
                            <img :src="notice.avatar" class="third-common-avatar"/>
                            <p class="item-author font-first-color">{{notice.senderName}}</p>
                            
                        </div>
                        <div class="notice-second-layer">
                            {{notice.content}}
                        </div>
                    </div>
                </div>
            </div>
            <div v-else class="flex-center-container">
                <img src="@/assets/img/utils/noData.png">
            </div>
        </div>
        </el-scrollbar>
    </div>     
</template>

<script setup>
import { onMounted } from "vue"
const noticeData = [{
    title: "2024年度赛季已开启",
    senderId: 0,
    avatar: require("@/assets/img/cover.jpeg"),
    senderName: "地球online专属PM",
    time: "2024-01-01 00:00:00",
    content: "在地球行驶到",
    turnUrl: "https://www.baidu.com"
},{
    title: "",
    avatar: require("@/assets/img/cover.jpeg"),
    time: "",
    content: "",
    turnUrl: ""
},{
    title: "",
    avatar: require("@/assets/img/cover.jpeg"),
    time: "",
    content: "",
    turnUrl: ""
},{
    title: "",
    avatar: require("@/assets/img/cover.jpeg"),
    time: "",
    content: "",
    turnUrl: ""
},{
    title: "",
    avatar: require("@/assets/img/cover.jpeg"),
    time: "",
    content: "",
    turnUrl: ""
}]
</script>

<style lang="scss" scoped>
@import "@/assets/css/messagePage.scss";
.notice-first-layer {
    margin-bottom: 0.3rem;
    display: flex;
    flex-flow: row nowrap;
    align-items: baseline;
}
.notice-single-title{
    font-size: 1.3rem;
}
.item-author{
    vertical-align: -0.4rem;
}
</style>