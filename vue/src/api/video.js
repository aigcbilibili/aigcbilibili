/**
 * 本部分包括对视频的管理
 * 1. 读取首页
 * 2. 读取动态、收藏、历史的视频卡片
 * 3.  
 */
import { ElMessage } from "element-plus"
import { useUserInfo } from "../store/userInfo.js"
import request from "./index.js"

const userStore = useUserInfo()
const userId = userStore.getId()
const basic_video = '/'
const basic_video_get = basic_video + 'play/'
const basic_video_up = basic_video + '/'

/**
 * 读取首页的视频
 * 请求字段
 * @param none
 * 返回字段
 * @return 如下
 */
export const getVideoBig = async (startLoc) => {
    try {
        const response = await request.get(basic_video_get + 'getFirstPageVideo/' + startLoc)

        return response.map((videoInMain) => ({
            id: videoInMain.videoId, // int
            imgUrl: videoInMain.cover, // string
            authorId: videoInMain.authorId, // int
            authorName: videoInMain.authorName, // string
            createTime: videoInMain.createTime || "2024-1-21", // string
            playCount: videoInMain.playCount, // int
            danmukuCount: videoInMain.danmakuCount, // int
            length: videoInMain.length || "00:01:00", // string
            videoName: videoInMain.name, // string
            url: videoInMain.url, // string - 视频存储在minio中的访问路径
        }))
    } catch (e) {
        ElMessage.error(`首页视频读取失败${e}`)
        console.error(`首页视频读取失败${e}`)
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
 * @return
 */
export const getVideoSmall = async (location, userId, folderId) => {
    let getURL = basic_video_get
    if (location === 'trend') {
        getURL = `/notice/getNotice/getDynamicVideo/${userId}`
        const response = await request.get(getURL, { userId: userId })
        // 如果数据为空
        if (response.length === 0) {
            return []
        }
        return response.map((item) => ({
            id: item.id, // int
            upName: item.authorName, // string
            upId: item.authorId,
            videoId: item.videoId,
            avatar: item.authorCover, // string
            title: item.videoName, // string
            imgUrl: item.videoCover, // string
            isRead: item.status, // 是否已读
            createTime: item.createTime,
            status: item.status
        }))
    } else if (location === 'collect') {
        getURL = '/collect/getCollectVideo/' + userId
        const response = await request.get(getURL, { userId: userId, folderId: folderId })
        // 如果数据为空
        if (response.length === 0) {
            return []
        }
        return response.map((item) => ({
            id: item.videoId,
            title: item.title,
            imgUrl: item.videoCoverUrl,
            upName: item.authorName,
            createTime: item.createTime
        }))
    } else {
        getURL += 'getHistoryVideo/' + userId
        const response = await request.get(getURL, { userId: userId })
        // 如果数据为空
        if (response.length === 0) {
            return []
        }
        return response.map((item) => ({
            videoId: item.videoId,
            title: item.videoName,
            imgUrl: item.videoCover,
            avatar: item.authorCover,
            upId: item.authorId,
            upName: item.authorName,
            recordTime: item.createTime,
            status: item.status
        }))
    }
}

/**
 * 读取视频详情页的视频
 */
export const getVideoDetail = async (videoId, userId, collectGroupId) => {
    try {
        const getUrl = basic_video_get + `getDetailVideo/${videoId}/${userId}/${collectGroupId}`
        const response = await request.get(getUrl, {
            videoId: videoId, // 1
            userId: userId, // 1
            collectGroupId: collectGroupId // 收藏夹列表："1,2,3"
        })
        // 如果数据为空，则前端使用测试数据
        if (response.length === 0) {
            return []
        }
        return {
            id: response.id, // number
            url: response.url,
            title: response.name, // string
            intro: response.intro, // string
            createtime: response.createTime || '2024-01-22', // string
            // tags:"'大学生', '计算机', '工作', '张雪峰','这就是真实生活力量', '生活','日常','程序员','生活记录','记录','生活万花筒 9.0 拥抱真实生活'",
            danmukuCount: response.danmakuCount, // number
            likeCount: response.likeCount, // number
            collectCount: response.collectCount,
            playCount: response.playCount, // number
            tags: response.tags, // []或string
            isLiked: response.isLiked, // boolean
            isCollected: response.isCollected // boolean
        }
    } catch (e) {
        ElMessage.error("视频详情页获取失败")
        console.error("视频详情页获取失败", e)
    }
}

/**
 * 读取推荐视频列表
 */
export const getRecommendVideos = async (videoId) => {
    try {
        const getUrl = basic_video_get + 'getCommendVideo/' + videoId
        const response = await request.get(getUrl, { videoId: videoId })
        return response.map((item) => ({
            id: item.video_id, // int
            videoName: item.video_name, // string
            authorName: item.author_name, // string
            upId: item.author_id,
            imgUrl: item.cover, // string
            createTime: item.create_time,
            danmukuCount: item.danmaku_count, // number
            playCount: item.play_count, // number
            length: item.length, // String
            url: item.url, // String
        }))
    } catch (e) {
        console.error("获取推荐视频失败", e.message)
    }
}

/**
 * 将视频归为历史视频
 */
export const addVideoHistory = async (videoId, userId) => {
    try {
        const postURL = basic_video_get + `addPlayRecord/${videoId}/${userId}`
        const response = await request.post(postURL, {
            userId: userId,
            videoId: videoId
        })
        return response
    } catch (e) {
        ElMessage.error("添加历史视频失败")
        console.error("添加历史视频失败：", e)
    }
}

/**
 * 删除单条历史视频的记录
 */
export const deleteVideoHistory = async (videoId, userId) => {
    try {
        const postURL = basic_video_get + `deleteHistoryVideo`
        const response = await request.post(postURL, {
            userId: userId,
            videoId: videoId
        })
        return response
    } catch (e) {
        ElMessage.error("删除历史视频失败")
        console.error("删除历史视频失败：", e)
    }
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
export const fetchUpVideo = async (userId) => {
    try {
        const getURL = `selfCenter/getPersonalVideo/${userId}`
        const response = await request.get(getURL, {
            userId: userId
        })
        return response.map((video) => ({
            id: video.videoId,
            imgUrl: video.cover,
            length: video.length,
            videoName: video.name,
            authorId: userId,
            playCount: video.playCount,
            createTime: video.createTime
        }))
    } catch (e) {
        ElMessage.error("请求单个用户投稿结果失败：", e)
        console.error("请求单个用户投稿结果失败：", e)
    }
}

/**
 * 查看最近点赞的视频
 */
export const fetchRecentLikeVideo = async (userId) => {
    try {
        const getURL = '/user-center/selfCenter/getRemotelyLikeVideo/' + userId
        const response = await request.get(getURL, {
            userId: userId
        })
        return response.map((video) => ({
            id: video.videoId,
            imgUrl: video.cover,
            danmukuCount: video.danmakuCount,
            length: video.length,
            videoName: video.name,
            playCount: video.playCount,
            createTime: video.createTime
        }))
    } catch (e) {
        ElMessage.error("查看最近点赞视频失败：", e)
        console.error("查看最近点赞视频失败：", e)
    }
}

/**
 * 上传视频
 *  场景：@/Pages/user/UserCenter
 *  请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：@/pages/user/UserCenter
 *  @return {string} name 
 *  @return {[]}
 */
export const addUpVideo = async (file, intro, name, userId, cover) => {
    try {
        const formData = new FormData()
        console.log('获取到了什么video', file, cover)
        // // 数据结构
        formData.append('file', file)
        formData.append('intro', intro) // upData.value.intro
        formData.append('name', name)
        formData.append('userId', userId)
        formData.append('videoCover', cover)
        console.log('传递过程中的formData', formData)
        const postURL = 'createCenter/upload'
        const response = await request.post(postURL, formData
            , {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'X-Requested-With': 'XMLHttpRequest',
                    'authorization': 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTEiLCJyb2xlIjoidXNlciJ9.ybuiJ87Nq7xgcWrM50P_VgAF1P74fnEN8jCSH5daqR2re4hVTMYgkzMHWZlK104guM75RGWgVxNrtfnhinjR-g',
                    'laBiliBiliHeader': 'test_method_1'
                }
            })
        return response
    } catch (e) {
        ElMessage.error("上传视频失败")
        console.error("上传视频失败：", e)
    }
}

/**
 * 获取视频的前三帧
 */
export const getThreeVideoCovers = async (file) => {
    try {
        const getURL = basic_video_up + 'getVideoCover'
        const response = await request.post(getURL, {
            file: file
        }, {
            headers: {
                'Content-Type': 'application/multipart/form-data'
            }
        })
        return response
    } catch (e) {
        ElMessage.error(`返回视频前三帧失败：${e}`)
        console.error(`返回视频前三帧失败：${e}`)
    }
}

/**
 *  编辑视频
 *  场景：@/Pages/user/UserCenter
 *  请求字段：
 *  @param {number} 请求的对象用户id
 * 返回字段：@/pages/user/UserCenter
 *  @return {string} name 
 *  @return {[]}
 */
export const editUpVideo = async () => {
    try {
        const postURL = ``
        const response = await request.post(postURL, {

        })
        return response
    } catch (e) {
        ElMessage.error(`编辑视频合集失败${e}`)
        console.error(`编辑视频合集失败${e}`)
    }
}

/**
 * 新建和修改视频合集
 */
export const addAndEditVideoCompilations = async (id, intro, name, userId) => {
    try {
        const postURL = `/user-center/ensemble/addOrUpDaterEnsemble`
        const response = await request.post(postURL, {
            id: id,
            intro: intro,
            name: name,
            userId: userId
        })
        return response
    } catch (e) {
        ElMessage.error(`新建视频合集失败${e}`)
        console.error(`新建视频合集失败${e}`)
    }
}

/**
 * 获取视频合集
 */
export const fetchVideoCompilations = async (userId) => {
    try {
        const postURL = `/user-center/ensemble/getEnsembleList/${userId}`
        const response = await request.post(postURL, {
            userId: userId
        })
        return response
    } catch (e) {
        ElMessage.error(`获取视频合集失败${e}`)
        console.error(`获取视频合集失败${e}`)
    }
}

/**
 * 删除视频合集
 */
export const deleteCompilations = async () => {
    try {
        const postURL = ``
        const response = await request.post(postURL, {

        })
        return response
    } catch (e) {
        ElMessage.error(`删除视频合集失败${e}`)
        console.error(`删除视频合集失败${e}`)
    }
}

/**
 * 获取视频合集中视频
 */
export const fetchVideosFromCompilations = async (userId, videoId) => {
    try {
        const getURL = `/user-center/ensemble/getAllVideoInEnsemble/${userId}/${videoId}`
        const response = await request.get(getURL, {
            userId: userId,
            videoId: videoId
        })
        return response // 没数据时会返回null
    } catch (e) {
        ElMessage.error(`查看视频合集中的视频失败：${e}`)
        console.error(`查看视频合集中的视频失败：${e}`)
    }
}

/**
 * 将视频移入合集
 */
export const addVideoToCompilation = async () => {
    try {
        const postURL = ``
        const response = await request.post(postURL, {

        })
        return response
    } catch (e) {
        ElMessage.error(`将视频移入合集失败：${e}`)
        console.error(`将视频移入合集失败：${e}`)
    }
}

/**
 * 将视频移出合集
 */
export const removeVideoFromCompilation = async () => {
    try {
        const postURL = ``
        const response = await request.post(postURL, {

        })
        return response
    } catch (e) {
        ElMessage.error(`将视频移出合集失败：${e}`)
        console.error(`将视频移出合集失败：${e}`)
    }
}




export const getUserInfoVideo = async (selfId, visitedId) => {
    const res = await request.get(`/selfCenter/getPersonalCenterContent/${selfId}/${visitedId}`);
    console.log('getUserInfoVideo', res);
    return res;
}



//取关
export const removeFollowing = async (userId, upId) => {
    const res = await request.post(`/follow/recallFollow`, {
        fansId: Number(userId),
        idolId: Number(upId)
    });
    return res;
}


//关注
export const addFollowing = async (userId, upId) => {
    const res = await request.get(`/follow/follow`, {
        fansId: Number(userId),
        idolId: Number(upId)
    });
    return res;
}