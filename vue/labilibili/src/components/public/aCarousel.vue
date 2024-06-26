<!--轮播图-->
<template>
    <div class="video-category flex-center-container">
        <button @click="prevSlide()" 
        class="video-category-controller controller-left common-based-btn font-fifth-color">上一项</button>
        <div class="video-category-item based-box common-based-btn" 
        :class="item.id-1===config.currentIndex?'tags-and-labels':'recommend-labels'" 
        v-for="(item, index) in visibleCategory" :key="index"
        @click="changeRecommendType(item.id-1)"><!--NOTE 虽然@v-for不报错，但不能这么用。因为v-for是指令而非事件-->
            {{item.name}}
        </div>
        <button @click="nextSlide()" 
        class="video-category-controller controller-right common-based-btn font-fifth-color">下一项</button>
    </div>
</template>

<script setup>
import { computed, defineProps, defineEmits } from "vue"
/**
 * props和emits
 */
const config = defineProps({
    currentIndex: {
        type: Number,
        required: true,
        default: 0
    },
    itemsPerPage: {
        type: Number,
        required: true,
        default: 0
    },
    recommendCategory: {
        type: Array,
        required: true,
        default: undefined
    }    
})
const recLen = config.recommendCategory.length
const backData = defineEmits('editIndex')
/**
 * 可视化分类
 */
const visibleCategory = computed(() => {
    // 根据当前索引和每页显示数量，计算轮播项
    const start = config.currentIndex
    const end = start + config.itemsPerPage;
    let visibleData = config.recommendCategory.slice(start, end)
    // 如果start过大
    let i = 0
    while(visibleData.length < config.itemsPerPage) {
        visibleData.push(config.recommendCategory[i++])
    } 
    return visibleData
})
/**
 * 推荐相关的交互
 */
// 切换推荐状态
const changeRecommendType = (index) => {
    config.currentIndex = index
    emits('editIndex', config.currentIndex)
}
// 推荐类型的滚动
const prevSlide = () => {
    config.currentIndex = (config.currentIndex - 1 + Math.ceil(recLen)) % Math.ceil(recLen)
    emits('editIndex', config.currentIndex)
}
const nextSlide = () => {
    config.currentIndex = (config.currentIndex + 1) % recLen
    emits('editIndex', config.currentIndex)
}
</script>

<style lang="scss" scoped>

</style>