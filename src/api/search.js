/**
 * 本部分关注搜索功能
 * api如下
 *  1. 请求搜索框的历史和热点
 *  2. 
 *  */ 

import request from "./index.js"

/**
 * 请求搜索框的历史和热点
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[JSON]} historyAndHot 
 */
export const getHistoryAndHot = () => {
    const history = request.get('/search/history',)
    const hot = request.get('/search/hot',)
    return {history, hot}
}

/**
 * 
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[JSON]} historyAndHot 
 */
export const askForSearch = () => request.get()

/**
 * 
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {[JSON]} historyAndHot 
 */

