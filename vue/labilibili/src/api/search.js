/**
 * 本部分关注搜索功能
 * api如下
 *  1. 请求搜索框的历史和热点
 *  2. 
 *  */ 

import { ElMessage } from "element-plus"
import request from "./index.js"

/**
 * PART A. 视频
 */
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
        console.log('搜索框历史和热点接口报错', e)
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
        console.log('搜索框历史和热点接口报错', e)
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
        console.log('请求搜索结果的页数和文件数失败：', e)
    }
}

/**
 * 获取视频搜索结果的总页数和文档数
 * 场景：@/Pages/search/searchPage
 *  请求字段：
 * @param none
 * @return {[]} 搜索数据的结果
 */
export const fetchVideoSearchLen = async(keyword) => {
    try{
        const getURL = basic_search + `totalKeywordSearch/${keyword}`
        const response = await request.get(getURL, {
            keyword: keyword
        })
        return {
            total_user_num: response.total_user_num,
            total_user_page: response.total_user_page,
            total_video_num: response.total_video_num,
            total_video_page: response.total_video_page
        }
    }catch(e){
        ElMessage.error(`搜索结果报错：${e}`)
        console.log(`搜索结果报错：${e}`)
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
            id: video.videoId,
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
            
         }))
    } catch(e) {
        ElMessage.error(`搜索结果用户项报错：${e}`)
        console.error(`搜索结果用户项报错：${e}`)
    }
}

/**
 * 
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[JSON]} historyAndHot 
 */

