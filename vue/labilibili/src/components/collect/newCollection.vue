<!--创建新文件夹（收藏夹和视频合集）的弹窗-->
<template>
    <div class="new-collection-panel">
        <p class="new-collection-title">新建文件夹</p>
        <div class="flex-left-container">
            <p class="new-collection-name">收藏夹名称</p>
            <aInput class="new-collection-input" />
        </div>
        <div class="flex-left-container">
            <p class="new-collection-name">简介</p>
            <aTextarea class="new-collection-input" />
        </div>
        <el-button class="new-collection-btn">创建</el-button>
    </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from "vue"
import aInput from "@/components/public/aInput.vue"
import aTextarea from "@/components/public/aTextarea.vue"
onMounted(()=>{

})
</script>

<style lang="scss" scoped>
.new-collection-panel {
    width: 15rem;
    height: 12rem;
    padding: 1rem;
    border-radius: 10px;
    background-color: #fff;
    position: relative;
}
.new-collection-title {
    font-weight: 800;
    font-size: 1.2rem;
    color: #3455ab;
    margin-bottom: 0.5rem;
}
.new-collection-name {
    width: 8rem;
    font-size: 0.9rem;
}
.new-collection-input {
    border: 1px solid #999;
}
.new-collection-btn {
    padding: 0.3rem;
    font-size: 0.8rem;
    position: absolute;
    bottom: 1rem;
    background-color: #3455ab;
    &:hover, &:active {
        background-color: #3455ab;
    }
}
</style>