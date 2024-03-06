<template>
    <div class="user-info-interact user-info-item flex-center-container">
        <el-button type="primary" :class="isFollowing?'user-second-btn':'user-first-btn'" 
        class="user-interact-btn common-based-btn" @click="handleFollow()">
            <p>{{ followText }}</p>
        </el-button>
        <el-button type="primary" class="user-interact-btn user-first-btn common-based-btn" 
        @click="turnToChat()">发消息
        </el-button>
    </div>
</template>

<script setup>
import { ref, watch, onBeforeUnmount } from "vue"
import { addFollowing, removeFollowing, defineProps } from "@/api/user"
import { useRouter } from "vue-router"
import { useUserInfo } from "@/store/userInfo"
const isEditFollow = ref(false) // 是否更改关注状态
const followText = ref("关 注") // 初始化视为未关注该up
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const router = useRouter()
// 传来的参数
const props = defineProps({
    isFollowing: {
        type: Boolean,
        default: false
    },
    upId: {
        type: Number,
        default: false
    }
})
const isFollowing = ref(props.isFollowing) // 是否关注，默认为否
const currentFollowing = props.isFollowing // 当前关注状态
const upId = ref(props.upId) // 传来的up对象Id
// 监听followText的点击状态
watch(isFollowing, (newItem, oldItem) => { // 关注数据
    if(newItem!=oldItem) {
        followText.value = isFollowing.value? "已关注": "关 注"
    }
},{deep: true})
//关注和取消的代码
const handleFollow = () => {
    isFollowing.value = !isFollowing.value
}
// 发消息
const turnToChat = () => {
    const routeURL = router.resolve({
        path: `/message/MyChat/${userId}`,
    })
    window.open(routeURL.href, '_blank')
}
onBeforeUnmount(()=>{
    if(currentFollowing!=isFollowing.value && isEditFollow) {
        if(isFollowing.value) {
            res = removeFollowing(userId, upId.value)
        } else {
            res = addFollowing(userId, upId.value)
        }
    }
})
</script>

<style lang="scss" scoped>
$user-box-first-color: rgb(161, 150, 235);
$user-box-second-color: rgb(192, 183, 249);
$user-box-padding-left-right: 2rem;
.user-info-item {
    position: absolute;
}
.user-info-interact {
    bottom: 1rem;
    right: $user-box-padding-left-right;
    .user-interact-btn, .user-interact-btn:visited {
        border-radius: 20px !important;
        border: none !important;
        padding: 1.1rem !important;
    }
    .user-first-btn {
        background-color: $user-box-second-color !important; 
        &:hover, &:active{
            background-color: $user-box-first-color !important;
        }
    }
    .user-second-btn {
        background-color: #fff !important;
        border: 1px solid $user-box-second-color !important; 
        color: $user-box-first-color;
        &:hover, &:active{
            background-color: #fff !important;
            color: $user-box-first-color;
        }
    }   
}
@media screen and (min-width: 1020px){
    .user-info-interact{
        right: $user-box-padding-left-right+1rem;
        .user-interact-btn:first-child{
        margin-right: 1rem;
        }
    }
}
</style>