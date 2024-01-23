/**
 * 本部分关注点赞和收藏
 * api如下
 *  1. likeToVideo
 *  2. likeToComment
 *  3. likeToTrend
 *  4. 
 *  */ 

import request from "./index.js"

/**
 * 对 视频/评论/动态 点赞
 * 场景：@/Pages/videoDetail/VideoDetail
 *      @/Pages/user/UserCenter
 * 请求字段：
 *  @param {number} id 请求的对象用户id
 * 返回字段：
 *  @return {boolean} is
 *  */ 
// 对视频点赞
export const likeToVideo = () =>{ request.post('') }
// 对评论点赞
export const likeToComment = () =>{ request.post('') }
// 对动态点赞
export const likeToTrend = () =>{ request.post('') }

/**
 * 对视频收藏
 * 场景：@/Pages/videoDetail/VideoDetail
 * 请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：
 *  @return {boolean} name 
 *  */ 
// 收藏视频
export const collectToVideo = () =>{ request.post('') }

/**
 * 查看收藏的视频：如果获取权限失败，则前端不会发出申请
 * 场景：@/Pages/user/UserCenter
 * 请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：
 *  @return {boolean} 
 *  */ 
export const getCollect = () =>{ request.post('') }