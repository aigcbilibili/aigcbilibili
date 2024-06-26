<!--动态和历史-->
<template>
    <div style="position: relative;width: 100%;height: 100%;">
        <div style="position: absolute; top: 0.5rem; left: 1rem;"><!--左侧-->
            <img v-lazy="item.avatar" class="first-common-avatar" @click.stop="turnToUserCenter(item.upId)" /><!--动态：他人-->
            </div>
        <div style="position: absolute; left: 6rem;" class="flex-column-left-max-container">
            <p style="font-weight: 600;">{{item.title}}</p>
            <p>{{item.upName}}</p>
            <p style="font-size: 0.8rem;">{{item.createTime}}</p>
        </div>
        <div style="position: absolute; top: 0.5rem; right: 1rem;">
            <img :src="item.imgUrl" style="width: 5.5rem; height: 3rem;" />
        </div>    
    </div>
</template>

<script setup>
import { ElMessage } from "element-plus"
import { defineProps } from "vue"
import { useRouter } from 'vue-router'
const router = useRouter()
const props = defineProps({
    videoRecord: {
        type: Object,
        default: undefined,
        required: false
    }
}) 
const item = props.videoRecord
// 跳转到对方的个人主页
const turnToUserCenter = (upId) => {
    if(!upId) {
        ElMessage.error("该视频作者id获取失败，无法跳转对应页面")
        return
    }
    const routeURL = router.resolve({
        path: `/userCenter/myItem/${upId}`,
    })
    window.open(routeURL.href, '_blank')
}

</script>

<style lang="scss" scoped>
</style>