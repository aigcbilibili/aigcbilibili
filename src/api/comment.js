import request from './index.js'

/**  
* 获取所有评论
* @param
*/
export const fetchAllComments = async(videoId) => {
    try{
        const getUrl = '/comment/getVideoComment/' + videoId
        const response = await request.get(getUrl,{videoId:videoId})
        return response.map((item)=>({
            id: item.id,
            senderId: item.userId,
            senderName: item.nickName,
            avatar: item.coverUrl, // 只有发送者需要有头像
            parentId: item.topId,
            receiverId: item.parentId,
            receiverName: item.receiverName,
            content: item.content,
            isLike: item.isLike, // 登录用户是否点赞过该评论
            likeCount: item.likeCount,
            createtime: item.createTime // 评论的时间
        }))
    }catch(e){
        console.error("获取所有评论接口错误:", e)
    }
}

/**
 * 发表评论
 * @param content
 * @param senderId
 * @param parentId
 * @param receiverId
 * @param videoId
 */
export const addComment = async(content,senderId, parentId, receiverId, videoId) => {
    try{
        const postUrl = '/comment/commentToComment'
        await request.post(postUrl, {
            // comment: {
                content: content,
                parentId: receiverId,
                topId: parentId,
                userId: senderId,
                videoId: videoId
            // }
        })
        return true // 无参数，只要是200都能用
    }catch(e){
        console.error("发表评论失败:", e)
    }
}

/**
 * 对评论点赞
 * @param senderId
 * @param commentId
 * //@param videoId
 */
export const addThumbsUp = async(senderId, commentId) => {
    try{
        const postUrl = '/like/likeToComment'
        await request.post(postUrl, {
            commentLikeRequest:{
                commentId: commentId,
                userId: senderId,
                // videoId: videoId
            }
        })
        return true
    }catch(e){
        console.error("对评论点赞失败：", e)
        return false
    }
}

/**
 * 取消对评论点赞
 * @param senderId
 * @param commentId
 * //@param videoId
 */
export const deleteThumbsUp = async(senderId, commentId) => {
    try{
        const postUrl = '/like/unDoLikeToComment/'
        await request.post(postUrl, {
            commentLikeRequest:{
                commentId: commentId,
                userId: senderId,
                // videoId: videoId
            }
        })
        return true
    }catch(e){
        console.error("取消对评论点赞失败：", e)
        return false
    }
}