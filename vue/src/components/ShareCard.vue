<!--分享页面-->
<template>
    <div class="share-wrap based-box">
        <!--封面和link-->
        <div class="flex-baseline-container" style="margin: 1rem 2rem;">
            <div class="share-cover-wrap flex-center-container">
                <img :src="shareCover" style="width: 100%; height: 100%; border-radius: 10px;" />
            </div>
            <div class="share-link-wrap">
                <p class="font-title">本页面链接</p>
                <div class="link-bg flex-center-container">
                    {{currentURL}}
                    <div style="position: absolute;margin:1rem; width: 5rem;
                    height: 7rem; text-overflow: ellipsis;"></div>
                </div>
            </div>
        </div>
        <!--分享到本系统中的用户-->
        <div class="share-header">
            <p class="font-title" style="margin-left: 1rem;">向好友分享</p>
            <div class="flex-left-container">
                <div v-if="lastElem" style="width: 2rem;"><!--上一个-->
                    上一条
                </div>
                <div v-for="(item, index) in FriendData" :key="index" 
                class="flex-column-center-container" style="margin: 0 0.8rem;">
                    <img :src="item.cover" class="second-common-avatar common-based-btn" @click="shareToFriend(item.userId)"  />
                    <p>{{item.nickname.length>=4 ? item.nickname.substr(0,4)+"...": item.nickname}}</p>
                </div>
                <div v-if="nextElem" style="width: 2rem;">
                    下一条
                </div><!--下一个-->
            </div>
        </div>
        <!--其他类别的分享-->
        <div class="share-header" style="margin-top: 1rem;">
            <p class="font-title" style="margin-left: 1rem;">转发分享</p>
            <div class="flex-left-container" style="margin: 0.5rem 0.8rem 0 0.8rem;" >
                <img src="@/assets/img/share/zhifubao.jpg" class="second-common-avatar common-based-btn" style="margin-right: 1rem;" @click="shareToOther()" />
                <img src="@/assets/img/share/moments.jpg" class="second-common-avatar common-based-btn" style="margin-right: 1rem;" @click="shareToOther()" />
                <img src="@/assets/img/share/weixin.jpg" class="second-common-avatar common-based-btn" style="margin-right: 1rem;" @click="shareToOther()" />
                <img src="@/assets/img/share/qq.jpg" class="second-common-avatar common-based-btn" style="margin-right: 1rem;" @click="shareToOther()" />
                <img src="@/assets/img/share/qqspace.jpg" class="second-common-avatar common-based-btn" style="margin-right: 1rem;" @click="shareToOther()" />
                <img src="@/assets/img/share/weibo.jpg" class="second-common-avatar common-based-btn" style="margin-right: 1rem;" @click="shareToOther()" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue'
import { fetchFollowingsList, fetchFollowersList } from "@/api/user"
import { useUserInfo } from "@/store/userInfo"
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
const router = useRouter()
const currentURL = window.location.href // 获取当前页面的URL
const userInfo = useUserInfo() // 使用登录信息
const userId = userInfo.getId() // 登录用户的id
const FriendData = ref([]) // 获得朋友的数组
const lastElem = ref(false) // 上一条
const nextElem = ref(false) // 下一条
const props = defineProps({
    upName: {
        type: String,
        required: true,
        default: "测试"
    },
    videoTitle: {
        type: String,
        required: true,
        default: "视频标题"
    },
    videoCover: {
        type: String,
        required: true,
        default: require("@/assets/img/cover.jpeg")
    }
})
const shareCover = props.videoCover
// 获取当前朋友的头像和id
const getFriendData = async() => {
    const followingArray = await fetchFollowingsList(userId)
    const followerArray = await fetchFollowersList(userId)
    const mergedArray = followingArray.concat(followerArray)
    FriendData.value = Array.from(new Set(mergedArray))
}
// 将内容分享给私人聊天的朋友
const shareToFriend = (upId) => {
    const routeURL = router.resolve({
        name: "chatPage",
        params: {
            userId: userId,
        },
        query: {
            receiverId: upId,
            content: {
                upName: props.upName,
                videoUrl: currentURL,
                title: props.videoTitle,
            }
         }
    })
    window.open(routeURL.href, '_blank')
}
// 将内容分享到其他地方
const shareToOther = () => {
    ElMessage.info("功能开发中")
}
onMounted(()=>{
    getFriendData()
})
</script>

<style lang="scss" scoped>
.share-wrap {
    background: #fff;
    width: 100%;
    height: 100%;
    div {
        margin-top: 0.5rem;
    }
    .share-cover-wrap {
        width: 12rem;
        height: 8rem;
    }
    .share-link-wrap {
        margin-left: 1rem;
        width: 5rem;
        height: 5rem;
        .link-bg {
            background: #cedff1;
            width: 13rem;
            height: 6rem;
            border-radius: 10px;
            padding: 0.5rem;
        }
    }
}
.font-title {
    font-weight: 600;
    font-size: 1.1rem;
    width: 7rem;
}
.share-header {
    margin: 0.5rem 1rem;
}
</style>