/**
 * 本部分关注点赞和收藏
 * api如下
 *  1. likeToVideo
 *  2. likeToComment
 *  3. likeToTrend
 *  4. 
 *  */

import { ElMessage } from "element-plus"
import request from "./index.js"
const basic_like = '/like/'
const basic_unLike = '/like/'
const basic_collect = '/collect/'

/**
 * 对 视频/评论/动态 点赞
 * 场景：@/Pages/videoDetail/VideoDetail
 *      @/Pages/user/UserCenter
 * 请求字段：
 *  @param {number} userId 发起点赞请求的用户id
 *  @param {number} objId 被点赞的对象id
 *  @param {string} objectType 被点赞的对象类型 
 * 返回字段：
 *  @return {boolean} is
 *  */
// 对视频点赞 
export const addLike = async (userId, objId, objectType) => {
    try {
        let postURL = basic_like
        let response = null
        postURL += 'like'
        switch (objectType) {
            case 'video': {
                console.log(33);
                response = await request.post(postURL, {
                    videoId: objId,
                    userId: userId,
                })
                break
            }
            case 'comment': {
                response = await request.post(postURL, {
                    // videoId: objId,
                    userId: userId,
                    commentId: objId
                })
                break
            }
        }
        return response
    } catch (e) {
        console.error(`点赞${objectType}失败：`, e);
    }
}
/**
 * 取消点赞
 */
export const deleteLike = async (userId, objId, objectType) => {
    try {
        let postURL = basic_unLike
        let unLikeParams = {}
        switch (objectType) {
            case 'video': {
                postURL += 'reCallLike'
                unLikeParams = {
                    videoId: objId,
                    userId: userId,
                }
                break;
            }
            case 'comment': {
                postURL += 'reCallLike'
                unLikeParams = {
                    commentId: objId,
                    userId: userId,
                }
                break;
            }
        }
        const response = await request.post(postURL, unLikeParams)
        return response
    } catch (e) {
        console.error(`取消点赞${objectType}失败：`, e);
    }
}

/**
 * 查看收藏文件夹：如果获取权限失败，则前端不会发出申请
 * 场景：@/Pages/user/UserCenter
 * 请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：
 *  @return {boolean} 
 *  */
export const fetchCollection = async (userId, videoId) => {
    try {
        const getURL = `/collect/getCollectGroup/${userId}`
        const response = await request.get(getURL, {
            userId: userId
        })
        return response.map((folder) => ({
            id: folder.id,
            name: folder.name,
            userId: userId,
            createTime: folder.createTime
        }))
    } catch (e) {
        ElMessage.error("获取收藏夹失败", e)
        console.error("获取收藏夹失败", e)
    }
}

/**
 * 查看收藏夹中的视频
 */
export const fetchVideoInCollect = async (userId) => {
    try {
        let postURL = basic_collect + `getCollectVideo/${userId}`
        const response = await request.get(postURL, {
            userId: userId
        })
        console.log(`验证收藏的视频:${response}`)
        return response
    } catch (e) {
        ElMessage.error(`查看收藏夹中视频失败`)
        console.log(`查看收藏夹中视频失败`)
    }
}

/**
 * 将视频添加入某个收藏夹或取出
 * 场景：@/Pages/videoDetail/VideoDetail
 * 请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：
 *  @return {boolean} name 
 */
export const editVideoToCollect = async (collectionId, videoId, type) => {
    try {
        let postURL = basic_collect
        if (type === 0) {
            postURL = basic_collect + 'collect' // 收藏
        } else {
            postURL = basic_collect + 'recallCollect' // 取消收藏
        }
        const response = await request.post(postURL, {
            collectGroupId: collectionId,
            videoId: videoId
        })
        return response
    } catch (e) {
        if (type === 0) {
            ElMessage.error(`将视频加入收藏夹（id：${collectionId}）失败：${e}`)
            console.error(`将视频加入收藏夹（id：${collectionId}）失败：${e}`)
        } else {
            ElMessage.error(`将视频取消收藏夹（id：${collectionId}）失败：${e}`)
            console.error(`将视频取消收藏夹（id：${collectionId}）失败：${e}`)
        }
    }
}

/**
 * 创建，修改收藏夹
 */
export const addCollection = async (collectionName, userId, collectionId) => {
    try {
        const postURL = basic_collect + 'editCollectGroup'
        const response = await request.post(postURL, {
            ...(collectionId && { id: collectionId }),
            name: collectionName,
            userId: userId
        })
        return response
    } catch (e) {
        ElMessage.error(`创建收藏夹失败${e}`)
        console.error(`创建收藏夹失败${e}`)
    }
}

/**
 * 删除收藏夹
 */
export const deleteCollections = async (collectionId) => {
    try {
        const postURL = basic_collect + 'deleteCollectGroup'
        const response = await request.post(postURL, {
            id: collectionId,
            // name: collectionName,
            // userId: userId
        })
        return response
    } catch (e) {
        ElMessage.error("删除收藏夹失败", e)
        console.error("删除收藏夹失败", e)
    }
}


//获取收藏夹
export const getCollections = async (videoId, UserId) => {
    let res = await request.get(`/collect/getVideoToCollectGroup/${UserId}/${videoId}`)
    return res
}

//修改收藏夹
export const updateCollections = async (arr) => {
    let res = await request.post('/collect/collect', arr);
    return res
}



//获取某个收藏夹下的视频
export const getCollectGroupByid = async (collectGroupId) => {
    let res = await request.get(`/collect/getCollectVideo/${collectGroupId}`)
    console.log('getCollectGroupByid', res);
    return res
}