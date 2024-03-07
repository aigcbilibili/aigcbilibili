<!--@我的-->
<template>
    <div class="wrap">
        <p class="component main-title font-fifth-color">@我的</p>
        <el-scrollbar style="height: 92%;">
        <div class="component">
            <div v-if="atData.length!=0">
                <div v-for="(at, index) in atData" :key="index">
                
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
const atData = []
// 获取@的数据
onMounted(()=>{

})
</script>

<style lang="scss" scoped>
@import "@/assets/css/messagePage.scss";
</style>