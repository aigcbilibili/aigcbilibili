<template>
    <div class="flex-column-left-max-container">
        <div class="based-box trend-item send-wrap" style="margin-top: 1rem;">
            <textarea class="trend-input-panel">           
            </textarea>
            <div class="flex-between-container trend-input-tools">
                <div class="flex-center-container">

                </div>
                <el-button type="primary" class="send-trend-btn common-btn-center">发送</el-button>   
            </div>
        </div>
        <div class="based-box trend-item all-user flex-left-container">
            <div class="trend-user-item">
                <img src="@/assets/img/trend/all_trend.png" />
                <p>全部动态</p>
            </div>
            <div v-for="(user, index) in followingData" :key="index" 
            class="trend-user-item" @click="changeTrendType(index)">
                <img :src="user.avatar" :class="{'trend-user-img-chosen':trendChosen===user.id}" />
                <p>{{user.upName}}</p>
            </div>
        </div>
        <div class="trend-item based-box trend-wrap">
            <!--动态-->
            <div v-if="trendChosen===-1 && trendData.length>0" class="">
                
            </div>
            <!--其他用户单独的-->
            <div v-else-if="trendChosen!==-1 && followingData[trendChosen].trends.length>0" >
                <div v-for="(item, index) in followingData[trendChosen].trends" :key="index" 
                class="based-box trend-record-item">
                    <trendRecordVue :trend-record="item" />
                </div>
            </div>
            <div v-else>
                <img src="@/assets/img/utils/noData.png" />
            </div>
        </div>    
    </div>
</template>

<script setup>
import { ref, onMounted, defineAsyncComponent } from "vue"
import { fetchFollowingsList } from "@/api/user"
import { getVideoSmall } from "@/api/video"
import { useUserInfo } from "@/store/userInfo"
// 获取单条动态记录的模板
const trendRecordVue = defineAsyncComponent(()=>
    import ("./TrendRecord.vue")
)
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const followingData = ref([]) // 关注者的动态
const trendChosen = ref(-1) // -1是所有，其他是userId
const trendData = ref([]) // 所有动态数据
const changeTrendType = (followIndex) => { // 切换动态类型数据
    if(followIndex!==trendChosen.value) {
        trendChosen.value = followIndex // 更新trendChosen
    }
}
onMounted(async()=>{
    let tmp_following = await fetchFollowingsList(userId) // 获得关注列表
    trendData.value = await getVideoSmall('trend', userId, "") // 获得总动态数据
    let id = 0
    // 获得动态
    tmp_following.forEach((user)=>{
        followingData.value.push({
            id_: id,
            upId: user.userId,
            upName: user.nickname,
            avatar: user.cover,
            trends: []
            })
        // 如果tmp_trend中有upId与userId相同的话，放入trends
        trendData.value.forEach((trend)=>{
            if (userId.upId === trend.userId) {
                followingData.value[id].trends.push(trend)
            }
        })
        id += 1
    })
})
</script>

<style lang="scss" scoped>
@import url("@/assets/css/trend.scss");
$left-width: 15rem;
$center-width: 40rem;
$right-width: 30rem;
$here-font-color: #0f4377;
.send-wrap {
    width: $center-width;
    height: 19rem;
    margin-top: 1rem;
}
.all-user {
    width: $center-width;
    height: 6rem;
}
.trend-wrap {
    width: $center-width;
    height: auto;
}
.trend-user-img-chosen {
    width: 3.2rem;
    height: 3.2rem;
    border-radius: 50%;
}
.trend-user-item {
    padding: 1.5rem;
    img {
        width: 3rem;
        height: 3rem;
        border-radius: 50%;
    }
    p {
        font-size: 0.9rem;
        color: $here-font-color;
    }
}
.trend-input-panel {
    width: $center-width - 3rem;
    height: 13rem;
    margin-top: 1rem;
    border-color: #adc3fa;
}
.trend-record-item {
    width: $center-width;
    min-height: 5rem;
    height: auto;
}
.trend-input-tools {
    margin: 0.5rem 1rem;
}
.send-trend-btn {
    background-color: #adc3fa;
    border: none;
    &:hover, &:active {
        background-color: #48619e;
        animation: 0.1s ease-in-out;
    }
}
</style>