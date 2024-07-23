<template>
    <!--排行页面-->
    <div class="trend-page flex-baseline-container">
        <!--左栏的用户和关注-->
        <div class="flex-column-center-container left-container">
            <userVue />
        </div>
        <!--中间查看动态信息-->
        <div class="">
            <trendCoreVue />
        </div>
        <!--右侧-->
        <div class="right-container"  style="margin-right: 6rem;">
            <topicInfoVue /> 
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, defineAsyncComponent} from "vue"
import { useUserInfo} from "@/store/userInfo"
const userInfo = useUserInfo() // 使用登录信息
const trendData = ref([]) // 动态数据
const userVue = defineAsyncComponent(()=>
    import ('./UserAndLive.vue')
)
const trendCoreVue = defineAsyncComponent(()=>
    import ("./TrendCore.vue")
)
const topicInfoVue = defineAsyncComponent(()=>
    import ("./TopicInfo.vue")
)

</script>

<style lang="scss" scoped>
@import url("@/assets/css/trend.scss");

$left-right-side-width: 8rem;
.trend-page {
    min-width: 45rem;
    min-height: 42rem;
    width: auto;
    height: auto;
    margin: 2rem;
    // background-color: #000;
}
@media screen and (min-width:1020px) {
    .left-container {
        margin-left: $left-right-side-width;
    }    
    .right-container {
        margin-right: $left-right-side-width;
    }
}
</style>