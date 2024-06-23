<!--单条动态记录-->
<template>
    <div class="based-box">
        <div class="flex-left-container">
            <img :src="recordData.avatar" class="first-common-avatar" />
            <div class="flex-column-left-max-container">
                <div class="trend-content">
                    
                </div>
            </div>
        </div>
        <div class="trend-func">

        </div>
    </div>
</template>

<script setup>
import { defineProps } from "vue"
const props = defineProps({
    trendRecord: {
        type: Object,
        required: true,
        default: null
    }
})
const recordData = props.trendRecord
console.log(`看下获取的数据：${recordData}`)
</script>

<style lang="scss" scoped>

</style>