/**
 * 本部分关注搜索功能
 * api如下
 *  1. 请求搜索框的历史和热点
 *  2. 
 *  */ 

import { ElMessage } from "element-plus"
import request from "./index.js"

const basic_search = '/search/'
/**
 * 请求视频搜索框的历史
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[]} history
 */
export const fetchVideoHistory = async() => {
    try{
        const history = await request.get('/search/history',)
        return history
    }catch(e){
        ElMessage.error('搜索框历史接口报错', e)
        console.error('搜索框历史接口报错', e)
    }
}

/**
 * 请求视频搜索框的热点
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[]} hot
 */
export const fetchVideoHot = async() => {
    try{
        const hot = await request.get('/search/hot',)
        return hot
    }catch(e){
        ElMessage.error('搜索框热点接口报错', e)
        console.error('搜索框热点接口报错', e)
    }
}

/**
 * 请求搜索结果的页数和文件数
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[Number, Number, Number, Number]} 视频结果的总页数、文件数，用户结果的总页数、文件数 
 */
export const fetchVideoResNum = async(keyword) => {
    try {
        const getURL = `/search/totalKeywordSearch/${keyword}`
        const response = await request.get(getURL, {
            keyword: keyword
        })
        return response
    }catch(e){
        ElMessage.error(`请求搜索结果的页数和文件数失败：${e}`)
        console.error('请求搜索结果的页数和文件数失败：', e)
        return Promise.reject(e)
    }
}


/**
 * 获取视频搜索结果的视频结果
 */
export const fetchVideoResFirst = async(keyword, pageNumber, type) => {
    try {
         const getURL = basic_search + `videoKeywordSearch/${keyword}/${pageNumber}/${type}`
         const response = await request.get(getURL, {
            keyword: keyword
         })
         return response.map((video)=>({
            id: video.video_id,
            imgUrl: video.cover,
            intro: video.intro,
            authorName: video.author_name,
            url: video.url,
            danmukuCount: video.danmakuCount,
            length: video.length,
            videoName: video.video_name,
            playCount: video.play_count,
            createTime: video.create_time
         }))
    } catch(e) {
        ElMessage.error(`搜索结果视频项报错：${e}`)
        console.log(`搜索结果视频项报错：${e}`)
        return Promise.reject(e)
    }
}

/**
 * 获取视频搜索结果的用户结果
 */
export const fetchVideoResSecond = async(keyword, pageNumber, type, userId) => {
    try {
        const getURL = basic_search + `userKeywordSearch/${keyword}/${pageNumber}/${type}/${userId}`
        const response = await request.get(getURL, {
            keyword: keyword,
            pageNumber: pageNumber,
            type: type,
            userId: userId
        })
        return response.map((user)=>({
            id: user.id,
            name: user.nickname,
            avatar: user.cover,
            followersNum: user.fans_count, 
            intro: user.intro,
            isFollowing: user.is_follow,
        }))
    } catch(e) {
        ElMessage.error(`搜索结果用户项报错：${e}`)
        console.error(`搜索结果用户项报错：${e}`)
        return Promise.reject(e)
    }
}

/**
 * 
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[JSON]} historyAndHot 
 */

