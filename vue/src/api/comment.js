import { ElMessage } from 'element-plus'
import request from './index.js'

/**  
* 获取所有评论
* @param
*/
export const fetchAllComments = async (videoId, userId) => {
    const topComments = 0
    const childrenComments = 1
    let num_comments = 0 // 所有评论数
    let total_comments = [] // 所有评论
    let getUrl = `/comment/getComment/${videoId}/${userId}/`
    // 顶层评论
    try {
        const response = await request.get(getUrl + topComments, {
            videoId: videoId,
            userId: userId,
            type: 0
        })
        console.log(`看下response:${JSON.stringify(response)}`)
        num_comments = response.commentCount
        response.commentResponseList.forEach((item) => total_comments.push({
            id: item.id,
            senderId: item.senderId,
            senderName: item.senderName,
            avatar: item.senderCoverUrl, // 只有发送者需要有头像
            parentId: item.topId,
            receiverId: item.receiverId,
            receiverName: item.receiverName,
            content: item.content,
            isLike: item.isLike, // 登录用户是否点赞过该评论
            likeCount: item.likeCount,
            createtime: item.createTime, // 评论的时间
            replyLen: 0,
            replyIdList: []
        }))
    } catch (e) {
        ElMessage.error(`获取所有顶层评论错误：${e}`)
        console.error("获取所有顶层评论错误:", e)
    }
    // 次级评论
    try {
        const response = await request.get(getUrl + childrenComments, {
            videoId: videoId,
            userId: userId,
            type: 1
        })
        response.commentResponseList.forEach((item) => {
            total_comments.map((comment) => {
                if (item.topId === comment.id) {
                    comment.replyLen += 1
                    comment.replyIdList.push({
                        senderName: item.nickName,
                        senderId: item.userId,
                        receiverName: item.receiverName,
                        receiverId: item.parentId,
                        avatar: item.coverUrl,
                        content: item.content,
                        likeCount: item.likeCount,
                        isLike: item.isLike,
                        createtime: item.createTime
                    })
                }
            })
        })
    } catch (e) {
        ElMessage.error(`获取所有评论的回复错误：${e}`)
        console.error("获取所有评论的回复错误:", e)
    }
    console.log(`看下评论数${num_comments}和所有评论${total_comments}`)
    return {
        commentCount: num_comments,
        commentData: total_comments
    }
}
export const getComment = (videoId, userId, type) => {

    ///comment/getComment/{videoId}/{userId}/{type}
    return request.get(`/comment/getComment/${videoId}/${userId}/${type}`)

}


/**
 * 发表评论
 * @param content
 * @param senderId
 * @param parentId
 * @param receiverId
 * @param videoId
 */
export const addComment = async (content, senderId, videoId, receiverId, parentId) => {
    try {
        const postUrl = '/comment/comment'
        console.log(`看下发表的评论`)
        await request.post(postUrl, {
            content: content,
            parentId: parentId,
            topId: receiverId,
            userId: senderId,
            videoId: videoId
        })
        return true // 无参数，只要是200都能用
    } catch (e) {
        console.error("发表评论失败:", e)
    }
}

/**
 * 编辑评论
 */
export const editComment = async (content, senderId) => {

}

/**
 * 删除评论
 */
export const deleteComment = async (content, senderId) => {

}