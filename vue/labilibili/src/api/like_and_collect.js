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
const basic_unLike = '/unlike/'
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
export const addLike = async(userId, commentId, videoId, objectType) =>{ 
    try{
        let postURL = basic_like
        let response = null
        postURL += 'like'
        switch(objectType){
            case 'video': {
                response = await request.post(postURL, {
                  videoId: videoId,
                  userId: userId,
                })
                break
            }
            case 'comment': {
                response = await request.post(postURL, {
                    videoId: videoId,
                    userId: userId,
                    commentId: commentId
                })
                break
            }
        }
        return response
    }catch(e){
        console.error(`点赞${objectType}失败：`, e);
    }
}
/**
 * 取消点赞
 */
export const deleteLike = async(userId, objId, objectType) => {
    try{
        let postURL = basic_unLike
        let unLikeParams = {}
        switch(objectType){
            case 'video': {
                postURL += 'unLikeToVideo'
                unLikeParams = {
                    videoId: objId,
                    userId: userId,
                }
                break;
            }
            case 'comment': {
                postURL += 'unLikeToComment'
                unLikeParams = {
                    commentId: objId,
                    userId: userId,
                }
                break;
            }
        }
        const response = await request.post(postURL, unLikeParams)
        return response
    }catch(e){
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
export const fetchCollection = async(userId) =>{ 
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
    }catch(e) {
        ElMessage.error("获取收藏夹失败", e)
        console.error("获取收藏夹失败", e)
    }
}

/**
 * 将视频添加入某个收藏夹
 * 场景：@/Pages/videoDetail/VideoDetail
 * 请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：
 *  @return {boolean} name 
 */
export const addVideoToCollect = async(collectionId, videoId) => {
    try {
        const postURL = basic_collect + 'collect'
        const response = await request.post(postURL, {
            collectGroupId: collectionId,
            videoId: videoId
        })
        return response
    }catch(e){
        ElMessage.error(`将视频加入收藏夹（id：${collectionId}）失败：${e}`)
        console.error(`将视频加入收藏夹（id：${collectionId}）失败：${e}`)
    }
}

/**
 * 将视频从某个收藏夹中取出
 */
export const removeVideoFromCollect = async() => {
    try {

    }catch(e){
        console.error("将视频从收藏夹取出失败", e)
    }
}

/**
 * 创建收藏夹
 */
export const addCollection = async(userId, collectionName, collectionId) => {
    const postURL = basic_collect + 'editCollectGroup'
    const response = await response.post(postURL, {
        id: userId,
        name: collectionName,
        id: collectionId    
    })
}

/**
 * 删除收藏夹
 */
export const deleteCollections = async() => {
    try {
        const getURL = basic_collect + 'deleteCollectGroup'
    }catch(e) {
        console.error("获取收藏夹失败", e)
    }
}

/**
 * 修改收藏夹
 */
export const editCollections = async() => {
    try {
        const getURL = basic_collect + 'deleteCollectGroup'
    }catch(e) {
        console.error("获取收藏夹失败", e)
    }
}

/**
 * 查看收藏夹中的视频
 */
export const fetchVideosFromCollect = async (userId) => {
    try{
        const getURL = `/video/getCollectVideo/${userId}`
    }catch(e){
        console.error("查看收藏夹中的视频失败：", e)
    }
}
