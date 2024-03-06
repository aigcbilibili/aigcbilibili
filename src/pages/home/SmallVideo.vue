<!--动态和历史-->
<template>
    <div style="position: absolute; top: 0.5rem; left: 1rem;"><!--左侧-->
        <img :src="item.avatar" class="first-common-avatar" @click.stop="turnToUserCenter(item.upId)" />
    </div>
    <div style="position: absolute; left: 6rem;" class="flex-column-left-max-container">
        <p style="font-weight: 600;">{{item.title}}</p>
        <p>{{item.upName}}</p>
        <p style="font-size: 0.8rem;">{{item.createTime}}</p>
    </div>
    <div style="position: absolute; top: 0.5rem; right: 1rem;">
        <img :src="item.imgUrl" style="width: 5.5rem; height: 3rem;" />
    </div>    
</template>

<script setup>
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
    const routeURL = router.resolve({
    path: `/userCenter/`,
    query: {
        userId: upId,
        
        }
    })
    window.open(routeURL.href, '_blank')
}

</script>

<style lang="scss" scoped>
</style>