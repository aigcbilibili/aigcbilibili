/**
 * 音频测试资源
 */
export const videoMain = [{
    id: 1, // 视频id，跳转到详情页
    title: '动态-测试视频',
    upName: '咸鱼n号',
    imgUrl: require('@/assets/img/cover.jpeg'), // 视频封面
    videoUrl: '', // 视频播放url
    playCount: 10,
    commentNum: 22
}]

export const VideoTrend = [{
    id: 1, // 视频id，跳转到详情页
    avatar: require("@/assets/img/avater.png"),
    title: '动态-测试视频',
    imgUrl: require('@/assets/img/cover.jpeg'), // 视频封面
    upName: '咸鱼n号',
    isRead: true, // 是否已读
    creatTime: '2023-12-26 12:00:00' // 视频创建时间 -》 xx月前发布：需前端计算逻辑处理
}]

export const VideoCollect = [{
    id: 1,
    collectGroupId: 1,
    title: '收藏-测试视频',
    imgUrl: require('@/assets/img/cover.jpeg'),
    upName: '咸鱼n号',
    createTime: '2023-12-26 12:00:00'
}]

export const VideoHistory = [{
    id: 1,
    title: '历史-测试视频',
    imgUrl: require('@/assets/img/cover.jpeg'),
    upName: '咸鱼n号',
    recordTime: '2023-12-26 12:00:00' // 历史记录创建时间
}]