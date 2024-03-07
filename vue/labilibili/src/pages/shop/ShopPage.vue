<!--商城首页-->
<template>
    <div class="shop-page">
        <biliHeader class="bili-header" />
        <!--搜索-->
        <searchComponent class="search-box flex-based-container" />
        <!--核心内容-->
        <div class="shop-main-content">
             <!--轮播图-->
             <div class="coursel-wrap flex-center-container" >
                <div v-for="(item, index) in courselData" :key="index"
                class="based-box coursel-item" :class="{'coursel-item-chosen':index===currentCourselIndex}">
                    {{item.title}}
                </div>
            </div>
            <!--分类-->
            <div class="categroy-wrap common-box flex-column-left-max-container">
                <div v-for="(item, index) in goodsType" :key="index" class="categroy-item font-fifth-color">{{item.name}}</div>
            </div>
           
        </div>
        <!---->
    </div>
</template>

<script setup>
import { onMounted, ref, defineAsyncComponent } from "vue"
import shopType from "@/assets/other/shopType.json"
import biliHeader from "@/components/BiliHeader"
import shopCoursel from '@/assets/data/shopCoursel'
const searchComponent = defineAsyncComponent(()=>
    import('@/components/search/SearchPanel.vue')
)
const goodsType = shopType.data
const courselData = ref([])
const currentCourselIndex = ref(0) // 当前轮播图的id
// 修改当前轮播图的大图
const changeCourselItem = () => {

}
onMounted(()=>{
    courselData.value = shopCoursel['data']
})
</script>

<style lang="scss" scoped>
$top-distance: 1.5rem;
$left-distance: 1rem;
.shop-page {
    background: rgb(108 149 244);
    min-height: 45.6rem;
    min-width: 51.3rem;
    height: auto;
    width: auto;
    z-index: -1;
}
.bili-header {
    min-height: 64px;
    position: relative;
    margin: 0 auto;
    max-width: 2560px;
    width: 100%;
}
.search-box {
    width: 25rem;
    margin: $top-distance $left-distance;
    ::v-deep .search-input {
        width: 15rem;
    }
    ::v-deep .search-btn {
        width: 4rem;
        height: 2.2rem;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.1rem;
    }
}
.shop-main-content {
    margin: $top-distance $left-distance+1rem;
}
.coursel-wrap {
    width: calc(80%);
    min-height: 30rem;
    height: auto;
    margin-bottom: 2rem;
    .coursel-item {
        width: 30rem;
        height: 20rem;
        background-color: #fff;
    }
    .coursel-item-chosen {
        z-index: 5;
    }
}
// 开发中先不设置
// .categroy-wrap {
//     display: none;
// }
.categroy-wrap {
    display: flex;
    .categroy-item {
        height: 2rem;
        font-size: 1.2rem;
        font-weight: 600;
    }
}
@media screen and (min-width: 1020px) {
    .shop-page {
        min-height: 46.3rem;
    }

}
</style>