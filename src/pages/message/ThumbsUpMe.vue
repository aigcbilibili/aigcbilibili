<!--点赞我的-->
<template>
    <div class="wrap">
        <p class="component main-title font-fifth-color">点赞我的</p>
        <el-scrollbar style="height: 92%;">
        <div class="component content">
            <div v-if="thumbsUpData.length!=0">
                <div v-for="(thumbsUp, index) in thumbsUpData" :key="index">
                
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
import { editLikeToRead } from "@/api/notice"
import { useUserInfo} from "@/store/userInfo"
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId()
const thumbsUpData = []
// 获取点赞通知的数据
onMounted(async()=>{
    await editLikeToRead(userId)
})
</script>

<style lang="scss" scoped>
@import "@/assets/css/messagePage.scss";
</style>