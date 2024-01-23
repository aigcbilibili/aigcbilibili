/**
 * 本部分包括对视频的管理
 * 1. 读取首页
 * 2. 读取动态、收藏、历史的视频卡片
 * 3.  
 */
import request from "./index.js"

/**
 * 读取首页的视频
 * 请求字段
 * @param none
 * 返回字段
 * @return 如下
 */
export const getVideoBig = async() =>{
    try{
        const response = await request.get('/video/getFirstPageVideo')
        return response.map((videoInMain)=>({
            id: videoInMain.videoId, // int
            imgUrl: videoInMain.coverUrl, // string
            authorId: videoInMain.authorId, // int
            authorName: videoInMain.authorName, // string
            createTime: videoInMain.createTime || "2024-1-21", // string
            playCount: videoInMain.playCount, // int
            danmukuCount: videoInMain.danmakuCount, // int
            length: videoInMain.length || "00:01:00", // string
            videoName: videoInMain.name, // string
            url: videoInMain.videoUrl, // string - 视频存储在minio中的访问路径
        }))
    }catch(e){
        console.log(e)
    }
}

/**
 * 读取首页动态，收藏夹和历史的视频
 * @param {string} location "动态"，"收藏", "历史"
 * 
 * 请求字段
 * @param {number} userId
 * @param {number} folderId
 * 返回字段
 * 
 */
export const getVideoSmall = async (location, userId, folderId) => {
    let getURL = '/video/' 
    if(location==='trend'){
        getURL += 'getDynamicVideo/' + userId
        const response = await request.get(getURL, {userId:userId})
        // 如果数据为空
        if(response.length===0){
            return []
        }
        return response.map((item)=>({
            id: item.videoId, // int
            upName: item.authorName, // string
            avatar: item.userCoverUrl, // string
            title: item.title, // string
            imgUrl: item.videoCoverUrl, // string
            isRead: item.status, // 是否已读
            createTime: item.createTime
        }))
    }else if(location==='collect'){
        getURL += 'getCollectVideo/'+ userId
        const response = await request.get(getURL, {userId:userId, folderId:folderId})
        // 如果数据为空
        if(response.length===0){
            return []
        }
        return response.map((item)=>({
            id: item.videoId,
            title: item.title,
            imgUrl: item.videoCoverUrl,
            upName: item.authorName,
            createTime: item.createTime
        }))
    }else{
        getURL += 'getHistoryVideo/'+ userId
        const response = await request.get(getURL, {userId:userId})
        // 如果数据为空
        if(response.length===0){
            return []
        }
        return response.map((item)=>({
            id: item.videoId,
            title: item.title,
            imgUrl: item.videoCoverUrl,
            upName: item.authorName,
            recordTime: item.recordTime
        }))
    }
}

/**
 * 设置首页动态，收藏夹和历史的视频为已读
 */
export const setVideoSmall = async(videoId) =>{
    
}


/**
 * 读取视频详情页的视频
 */
export const getVideoDetail = async(videoId) =>{
    const getUrl = '/video/getDetailVideo/'+videoId
    const response = await request.get(getUrl, {videoId: videoId})
    // 如果数据为空，则前端使用测试数据
    if(response.length===0){
        return []
    }
    return (()=>({
        id: response.videoId, // number
        title: response.name, // string
        intro: response.intro, // string
        createTime: response.createTime || '2024-01-22', // string
        danmukuCount: response.danmakuCount, // number
        likeCount: response.likeCount, // number
        playCount: response.playCount, // number
        tags: response.tags, // []或string
        isLiked: response.isLiked, // boolean
        isCollected: response.isCollected // boolean
    }))
}

/**
 * 将视频添加入某个收藏夹
 */
export const addVideoCollect = async() => {

}

/**
 * 将视频归为历史视频
 */
export const addVideoHistory = async() => {

}

/**
 * 请求单个用户的投稿结果（我的视频）
 *  场景：@/Pages/user/UserCenter
 *  请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：@/pages/user/UserCenter
 *  @return {string} name 
 *  @return {[]}
 *  */ 