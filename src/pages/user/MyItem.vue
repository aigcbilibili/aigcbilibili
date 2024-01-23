<template>
    <div class="my-item">
        <!--我的视频-->
        <div class="my-item-single" v-for="(videoPanel,index) in personVideoData" :key="index">
            <div class="my-single-title">{{videoPanel.title}}</div>
            <div class="my-single-content">
                <div v-if="videoPanel.isExist==='false'">本栏目没有内容</div>
            </div>
        </div>
    </div>
</template>

<script setup>
const personVideoData = require([{
    "isExist": false,
    "title": "我的投稿",
    "id": 1
},{
    "isExist": false,
    "title": "我的收藏",
    "id": 1
},{
    "isExist": false,
    "title": "最近点赞",
    "id": 1
}])
/**
 * 对视频的管理
 */
</script>

<style scoped>
.my-item{
    padding: 2rem;
}
.my-item-single{
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    align-items: center;
    width: 100%;
    min-height: 3rem;
}
</style>