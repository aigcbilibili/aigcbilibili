// 弹幕：自定义
import request from "./index.js"

const basicUrl = '/danmaku/'
export const fetchDanMu = async(videoId) => {
    try {
        const getUrl = basicUrl + 'getDanmaku/' + videoId
        const response = await request.get(getUrl, {videoId: videoId})
        return response.map((danmu)=>({
            content: danmu.content,
            createTime: danmu.createTime,
            timeStamp: danmu.place, // string: 弹幕在视频中的时刻
            userId: danmu.userId,
        }))
    }catch (e) {
        console.log(e)
    }
}

export const addDanMu = async(videoId) => {
    try {
        const getUrl = basicUrl + 'addDanmaku'
        const response = await request.get(getUrl, {videoId: videoId})
        return response
    }catch (e) {
        console.error("撤回弹幕失败",e)
    }
}

export const deleteDanMu = async(videoId) => {
    try {
        const getUrl = basicUrl + 'addDanmaku'
        const response = await request.get(getUrl, {videoId: videoId})
        return response
    }catch (e) {
        console.error("撤回弹幕失败", e)
    }
}