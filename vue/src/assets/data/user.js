/**
 * 用户信息测试数据
 */
// 自己（非up主）
export const mine = {
    id: 1,
    name: "咸鱼1号",
    avatar: require("@/assets/avater.png"), // url：暂时用本地的数据
    followingNum: 100,
    followersNum: 100,
    intro: "1号的测试数据",
    playCount: 99,
    videoLike: 99,
    isFocus: 0 // 不能关注自己
}

// 他人（非up主）
export const other = {
    id: 2,
    name: "咸鱼2号",
    avatar: require("@/assets/avater.png"), // url：暂时用本地的数据
    followingNum: 200,
    followersNum: 200,
    intro: "2号的测试数据",
    playCount: 299,
    videoLike: 299,
    isFocus: 0 // 未关注他
}

// up主
export const up = {
    id: 3,
    name: "咸鱼3号",
    avatar: require("@/assets/avater.png"),
    followingNum: 300,
    followersNum: 300,
    intro: "3号的测试数据",
    playCount: 399,
    videoLike: 399,
    isFocus: 0 // 未关注他
}