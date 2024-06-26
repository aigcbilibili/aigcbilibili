<template>
    <div class="user-info-interact user-info-item flex-center-container">
        <el-button type="primary" :class="isFollowing ? 'user-second-btn' : 'user-first-btn'"
            class="user-interact-btn common-based-btn" @click.stop="handleFollow()">
            <p>{{ isFollowing ? '已关注' : '关注' }}</p>
        </el-button>
        <el-button type="primary" class="user-interact-btn user-first-btn common-based-btn"
            @click.stop="turnToChat()">发消息
        </el-button>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineEmits } from "vue"
// import { addFollowing, removeFollowing } from "@/api/user"
import { removeFollowing, addFollowing } from "@/api/video"
import { useRouter } from "vue-router"
import { useUserInfo } from "@/store/userInfo"
import { ElMessage } from "element-plus"
import Debounce from '@/static/debounce'
import { watch } from "vue"
const followText = ref("关 注") // 初始化视为未关注该up
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const router = useRouter()
const debounce = new Debounce() // 防抖
// 传来的参数
const props = defineProps({
    isFollowing: {
        type: Boolean,
        required: true,
    },
    upId: {
        type: Number,
        required: true,
        default: false
    },
    userInfo: {
        type: Object,
        required: true,
        default: () => {
            return {}
        }
    }
})
const upId = ref(props.upId) // 传来的up对象Id
const emits = defineEmits(['update:isFollowingValue'])
const isFollowing = ref(props.isFollowing)
//关注和取消的代码
const handleFollow = async () => {
    if (userId === 0 || !userId) {
        ElMessage.error(`您未登录，无法发起关注`)
        return
    }
    if (userId === props.userInfo.userId) {
        ElMessage.error(`您不能关注自己`)
        return
    }
    if (isFollowing.value) {
        await removeFollowing(userId, props.userInfo.userId)
    } else {
        await addFollowing(userId, props.userInfo.userId)
    }
    isFollowing.value = !isFollowing.value
    followText.value = isFollowing.value ? "已关注" : "关 注"

}
// 发消息
const turnToChat = () => {
    sessionStorage.setItem('commentInfo', JSON.stringify({ senderCoverUrl: props.userInfo.cover, senderName: props.userInfo.nickname, senderId: props.userInfo.userId }))
    router.push({ path: `/message/MyChat/${userId}` })
}
onMounted(() => {
    // console.log('pros', props.isFollowing);
    // setTimeout(() => {
    //     followText.value = props.isFollowing ? "已关注" : "关 注";
    // }, 1000)

    console.log('444', props.userInfo);
})
watch(() => props.isFollowing, (val) => {
    isFollowing.value = val
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

    .user-interact-btn,
    .user-interact-btn:visited {
        border-radius: 20px !important;
        border: none !important;
        padding: 1.1rem !important;
    }

    .user-first-btn {
        background-color: $user-box-second-color !important;

        &:hover,
        &:active {
            background-color: $user-box-first-color !important;
        }
    }

    .user-second-btn {
        background-color: #fff !important;
        border: 1px solid $user-box-second-color !important;
        color: $user-box-first-color;

        &:hover,
        &:active {
            background-color: #fff !important;
            color: $user-box-first-color;
        }
    }
}

@media screen and (min-width: 1020px) {
    .user-info-interact {
        right: $user-box-padding-left-right+1rem;

        .user-interact-btn:first-child {
            margin-right: 1rem;
        }
    }
}
</style>