/**
 * 
 */
import { ElMessage } from "element-plus"
import request from "./index.js"
const basic = '/'
const basic_notice_get = basic + 'getNotice/'
const basic_notice_post = basic + 'changeNoticeStatus/'

/**
 * 评论数、点赞数和动态数的获取
 */
export const fetchNoticeNum = async(userId) => {
    try {
        const getURL = basic_notice_get + `getNoticeCount/${userId}`
        console.log('看下获取', getURL)
        const response = await request(getURL, {
            userId: userId
        })
        return {
            totalCount: response.totalCount,
            chatCount: response.chatCount,
            commentCount: response.commentCount,
            dynamicVideoCount: response.dynamicVideoCount,
            likeCount: response.likeCount,
            // totalCount: 20,
            // chatCount: 11,
            // commentCount: 3,
            // dynamicVideoCount: 6,
            // likeCount: 6,
        }
    }catch(e){
        console.error("评论数、点赞数和动态数获取失败：", e)
        ElMessage.error("评论数、点赞数和动态数获取失败")
    }
}

/**
 * 获得点赞消息
 */
export const fetchLikeNotice = async() => {
    try {
        const getURL = basic_notice_get + `getLikeNotice/${userId}`
        const response= await request.get(getURL, {
            userId: userId
        })
        return response
    }catch(e){
        console.error(`获得点赞消息失败：${e}`)
    }
}

/**
 * 获得评论消息
 */
export const fetchCommentNotice = async(userId) => {
    try {
        const getURL = basic_notice_get + `getCommentNotice/${userId}`
        const response = await request.get(getURL, {
            userId: userId
        })
        return {

        }
    } catch(e) {
        ElMessage.error("获得评论消息失败")
        console.error(`获得评论消息失败：${e}`)
    }
}

/**
 * 修改评论通知为已读
 */
export const editReplyToRead = async(userId) => {
    try{
        const postURL = basic_notice_post + 'changeCommentNoticeStatus'
        const response = await request.post(postURL, {
            userId: userId
        })
        return response
    }catch(e){
        ElMessage.error("令评论已读失败")
        console.error("修改评论通知为已读失败：", e.message)
    } 
}

/**
 * 修改点赞通知为已读
 */
export const editLikeToRead = async(userId) => {
    try{
        const postURL = basic_notice_post + 'changeLikeNoticeStatus'
        const response = await request.post(postURL, {
            userId: userId
        })
        return response
    }catch(e){
        ElMessage.error("令点赞已读失败")
        console.error("修改点赞通知为已读失败：", e.message)
    }
}

/**
 * 修改动态已读
 */
export const editTrendToRead = async(userId) => {
    try{
        const postURL = basic_notice_post + 'changeDynamicVideoStatus'
        const response = await request.post(postURL, {
            userId: userId
        })
        return response
    }catch(e){
        ElMessage.error("令动态已读失败")
        console.error("修改点赞通知为已读失败：", e.message)
    }    
}