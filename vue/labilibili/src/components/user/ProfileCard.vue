<template>
    <div style="position: relative;overflow: visible;">
        <img :src="firstAvatar" alt="头像" @mouseover="isUpIntro = true" @mouseout="isUpIntro = false"
            class="first-common-avatar" @click="turnToOtherCenter(userId)" />
        <div v-if="isUpIntro" @mouseout="isUpIntro = false" class="profile-pop-up">
            <div class="profile-pop-up-name font-first-color">{{ infoData.name }}</div>
            <div class="follow-data">
                <span style="vertical-align: -0.1rem;" @click="follow()">关注数{{ infoData.followingNum }}</span>
                <span>粉丝数{{ infoData.fansNum }}</span>
            </div>
            <div class="profile-pop-up-intro">
                <p>{{ infoData.intro }}</p>
            </div>
            <div class="concern-group">
                <button v-if="!infoData.isFocus" class="concern common-btn-1 main-first-color">关注</button>
                <button v-else-if="infoData.isSame">无法关注</button>
                <button class="sendMessage common-btn-1">消息</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref, onMounted, defineProps } from 'vue'
import { fetchUserInfo } from '@/api/user'
// import { addFollowing, removeFollowing } from '@/api/follow'
import { useRouter } from 'vue-router'

const router = useRouter()
const isUpIntro = ref(false)
const props = defineProps({
    avatar: {
        type: Object,
        required: true,
        default: ""
    },
    id: {
        type: Number,
        required: true,
        default: 1
    }
})
const firstAvatar = props.avatar
const userId = props.id
// 新打开跳转他人的个人主页
const turnToOtherCenter = (userId) => {
    const routeURL = router.resolve({
        path: `/userCenter/myItem/${userId}`,
    })
    window.open(routeURL.href, '_blank')
}
// onMounted
onMounted(() => {
    // 获取信息数据
    // fetchUserInfo(1).then(res=>{
    //     console.log(res)
    // })
})
// 要放上去的数据
const infoData = reactive({
    name: '咸鱼',
    avatar: firstAvatar,
    followingNum: 100, // 关注人数
    fansNum: 100, // 粉丝人数
    thumbsUpNum: 43, // 视频或文章点赞数
    playCount: 69, // 视频播放数
    intro: '[读取失败] 测试：做个快乐的咸鱼',
    isFocus: false,
    isSame: false
})

/**
 * 关注逻辑的实现
 */
const follow = () => {
    // 判断是否登录对象和
    infoData.isFocus = ~infoData.isFocus
    if (infoData.isFocus) {
        // 取消关注
        // const res = removeFollowing()
        alert('关注成功')
    } else {
        // 关注
        // const res = addFollowing()
        alert('关注成功')

    }
}

</script>

<style scoped>
.profile-pop-up {
    position: absolute;
    top: 2.5rem;
    /* position: absolute定位 */
    left: 3rem;
    max-width: 20rem;
    height: 7.2rem;
    padding: 1rem;
    border-radius: 10px;
    z-index: 5;
    background: #fff;
    box-shadow: 0 2px 25px 0 #79b1eca9;
}

button {
    width: 3.5rem;
    height: 1.6rem;
    border-radius: 10px;
    border: none;
}

.profile-pop-up-name {
    font-weight: 600;
}

.profile-pop-up-intro {
    margin-top: 0.5rem;
    height: 2.5rem;
    overflow: hidden;
}

.follow-data span:first-child {
    margin-right: 1rem;
}

.concern-group {
    position: absolute;
    right: 1rem;
}

.concern-group button:first-child {
    margin-right: 1rem;
}

.concern {
    font-size: 0.9rem;
    color: #fff;
}

.concern:hover {
    background: #6ba2dca9;
}

.concern:active {
    background: #4e7caea9;
}

.not-concern {
    background: #fff;
    color: #79b1eca9;
    border: 1px solid #79b1eca9;
    font-size: 0.9rem;
}
</style>