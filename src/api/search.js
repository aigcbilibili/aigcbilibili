/**
 * 本部分关注搜索功能
 * api如下
 *  1. 请求搜索框的历史和热点
 *  2. 
 *  */ 

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
 * 获取视频搜索结果
 * 场景：@/Pages/search/searchPage
 *  请求字段：
 * @param none
 * @return {[]} 搜索数据的结果
 */
export const fetchVideoSearchRes = async(keyword) => {
    try{
        console.log("我的keyword呢", keyword)
        const getURL = basic_search + `videoKeywordSearch/${keyword}/1/1`
        const response = request.get(getURL, {
            
        })
        return response
    }catch(e){
        console.log('搜索结果报错：', e)
    }
}

/**
 * 
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[JSON]} historyAndHot 
 */

