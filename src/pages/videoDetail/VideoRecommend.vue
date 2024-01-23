<!--接着播放的推荐-->
<template>
    <!--显示集数-->
    <div v-if="true" class="video-episodes">
        显示视频集
    </div>
    <!--广告位-->
    <div class="video-ad">
        广告占位
        <!-- <img src="@/assets/img/ad.png" alt="广告位" /> -->
    </div>
    <!--选择不同的栏目-->
    <div class="video-category">
        <button @click="prevSlide" class="video-category-controller">上一张</button>
        <div class="video-category-item" v-for="(item, index) in recommendCategory" :key="index"><!--NOTE 虽然@v-for不报错，但不能这么用。因为v-for是指令而非事件-->
            {{item.name}}
        </div>
        <button @click="nextSlide" class="video-category-controller">下一张</button>
    </div>
    <!--推荐视频--><!--虚拟滚动，分页加载列出项目-->
    <div class="video-recommend-content">
        <div class="video-recommend-item" v-for="(item,index) in relatedInfo.similarvideo" :key="index">
            <div class="video-recommend-item-img">
                <video :src="item.url" class="video-content" controlslist="nodownload" style="width: 10rem;height: 10rem;"
                loading="eager" onload="fsrCb" onerror="typeof window.imgOnError === 'function'&& window.imgOnError(this)"></video>
            </div>
            <div class="video-recommend-item-info">
                <div class="video-recommend-item-title">{{item.videoname}}</div>
                <div class="video-recommend-item-time">{{item.createtime}}</div>
            </div>
        </div>
    </div>
</template>

<script setup>

/**
 * 推荐的分类 
 */
 const recommendCategory = [{
    id: 1,
    name: '全部'
},{
    id: 2, /* 默认号 */
    name: '来自作者'
},{
    id: 3,
    name: '相关内容'
},{
    id: 4,
    name: '热门视频'
},{
    id: 5,
    name: '为你推荐'
}]
// 推荐类型的滚动
function prevSlide(){

}
function nextSlide(){

}
</script>

<style lang="scss" scoped>

</style>