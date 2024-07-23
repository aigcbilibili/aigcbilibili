// 弹幕：自定义
import { ElMessage } from "element-plus"
import request from "./index.js"

const basic_danmu = '/danmaku/'
export const fetchDanMu = async (videoId) => {
    try {
        const getUrl = basic_danmu + 'getDanmaku/' + videoId
        const response = await request.get(getUrl, { videoId: videoId })
        console.log("查看弹幕显示", JSON.stringify(response))
        return response.map((danmu) => ({
            content: danmu.content,
            createTime: danmu.createTime,
            timeStamp: danmu.place, // string: 弹幕在视频中的时刻
            userId: danmu.userId,
        }))
    } catch (e) {
        ElMessage.error("获取弹幕失败", e)
        console.error("获取弹幕失败", e)
    }
}

export const addDanMu = async (videoId) => {
    try {
        const getUrl = basic_danmu + 'addDanmaku'
        const response = await request.get(getUrl, { videoId: videoId })
        return response
    } catch (e) {
        console.error("撤回弹幕失败", e)
    }
}

export const deleteDanMu = async (videoId) => {
    try {
        const getUrl = basic_danmu + 'addDanmaku'
        const response = await request.get(getUrl, { videoId: videoId })
        return response
    } catch (e) {
        console.error("撤回弹幕失败", e)
    }
}

/**
 * 
 *  [
            230.523, // time
            0, //type
            16777215,//color
            "618c713c",      //author:
            "键盘妹子挺好看？" // text
      
        ],



content
: 
"弹幕"
createTime
: 
"2024-03-10 12:25:10"
place
: 
null
userId
: 
2
videoId
: 
null

 */



export const getDanmuList = async (videoId) => {
    let res = await request.get(`/danmaku/getDanmaku/${videoId}`)
    let data = res.map((item, index) => {
        return {
            "time": item.place,
            "text": item.content,
            "color": item.color,
            "type": item.type,
            "author": item.userId
        }
    })
    return data

}


export const addDanmu = async (data) => {
    await request.post('/danmaku/addDanmaku', {
        content: data.content,
        place: data.place,
        userId: data.userId,
        videoId: Number(data.videoId),
        type: data.type,
        color: data.color
        // time:data.time
    })
    return true
}