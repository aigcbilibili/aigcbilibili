<!--评论的顶层回复和对评论的回复-->
<template>
    <div class="comment-form flex-center-container">
        <img :src="getSendUser.avatar" class="send-user-avatar first-common-avatar" />
        <div style="cursor: text;">
            <textarea class="comment-area" v-model="content" placeholder="和谐的评论更能促进交流~"></textarea>
        </div>
        <button class="send-comment-btn common-btn-center detail-btn" @click="sendComment()">发 布</button>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, computed, onMounted } from 'vue'
import { addComment } from '@/api/comment'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
const content = ref('') //
// 接收发布评论的用户信息
const props = defineProps({
    value: {
        type: Object,
        required: true
    },


})

const getSendUser = ref(props.value)
const emits = defineEmits(['send:returnReply', 'isShow', 'getData'])
const replyComputed = computed({
    get: () => content.value,
    set: function (val) {
        emits('send:returnReply', val)
    }
})
const router = useRouter()
const route = useRoute()
/**
 * 评论发布功能
 */
// 发布评论
const sendComment = async () => {
    // 验证评论

    // // 前端显示：要传递到CommentPanel中的列表里
    // replyComputed.value = {
    //     id: 0,
    //     avatar: getSendUser.value,
    //     senderId: getSendUser.value,
    //     senderName: "",
    //     content: content.value,
    //     createtime: "2024",
    //     likeCount: 0,
    //     isLike: false,
    // }
    // 传递到后
    // console.log('咕咕', content.value)
    if (props.value.senderId === 0) {
        localStorage.setItem('path', route.fullPath)
        return router.push({ path: '/login' })
    }
    if (!content.value) return

    // console.log('des', props.value);

    const res = await addComment(content.value,
        getSendUser.value.senderId, getSendUser.value.videoId, getSendUser.value.receiverId, getSendUser.value.parentId)
    console.log('评论数据传递：结果', res)
    // 清空当前
    if (res) {
        content.value = ""
        emits('isShow')
        emits('getData')
    } else {
        ElMessage.error('发布评论失败')
    }
}
onMounted(() => {
    // 

})

</script>

<style lang="scss" scoped>
.comment-form {
    padding: 1rem 1rem;
    height: 5rem;
}

.send-user-avatar {
    display: none;
}

.comment-form .comment-area {
    width: 35rem;
}

.comment-form .send-comment-btn {
    font-weight: 600;
    font-size: 1.4rem;
    width: 5rem;
    height: 3.3rem;
    padding: 0.5rem;
    margin-left: 0.5rem;
}

@media screen and (min-width: 1020px) {
    .comment-form .comment-area {
        /* NOTE 形式要统一，使用comment-form限制 */
        width: 45rem;
    }

    .send-user-avatar {
        display: flex;
        margin-right: 1rem;
        margin-top: -0.5rem;
    }
}
</style>