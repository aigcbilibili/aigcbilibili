/**
 * 本部分获取特点用户（权限分为up, user, other）的关注数、粉丝数等信息
 * api如下
 *  1. fetchUserInfo
 *  2. getFollowing
 *  3. getFollower
 *  */ 
import { ElMessage } from "element-plus"
import request from "./index.js"

const basic = '/'
const basic_info = basic + 'userInfo/'
const basic_follow = basic + 'follow/'

/**
 * 请求单个用户的Profile
 * 场景：@/components/user/ProfileCard
 *      @/components/user/useInfo
 * 请求字段：
 *  @param {number} 发起查询请求的对象用户id
 *  @param {number} 被查询的对象用户id
 * 返回字段：
 *  @return {string} name：对应nickname 
 *  @return {string} avatar 头像的img地址
 *  @return {number} followingNum 关注数：对应fansCount
 *  @return {number} followersNum 粉丝数：对应idolCount
 *  @return {string} intro 简介
 *  @return {number} playNum 视频的播放量
 *  @return {number} likeNum 视频的点赞量
//  *  @return {number} isFocus 登录用户已经关注对方：0未关注，1已关注，2互相关注
 *  */ 
export const fetchUserInfo = async(userId, upId) =>{
    try{
        const getURL = basic_info + `getUserInfo/${userId}/${upId}`
        const response = await request.get(getURL, {
            userId1: userId,
            userId2: upId
        })
        // console.log(`看下用户信息：${JSON.stringify(response)}`)
        return ({
            "id": response.id,
            "name": response.nickname,
            "avatar": response.cover,
            "followingNum": response.idolCount,
            "followersNum": response.fansCount,
            "isFollowing": response.isFollowing, // 是否已关注
            "intro": response.intro || "",
            "playNum": response.playCount,
            "likeNum": response.likeCount
        })
    }catch(e){
        console.log('获取简介失败', e)
    }
} 

/**
 * 修改用户信息
 */
export const editUserProfile = async(userId, coverImg, intro, nickName) => {
    try {
        console.log("修改用户简介")
        const postURL = basic_info + 'getUserInfo'
        const response = await request.post(postURL, {
            id: userId,
            coverImg: coverImg,
            intro: intro,
            nickname: nickName
        })
        return response
    } catch (e) {
        ElMessage.error("修改用户信息接口失败")
    }    
}

/**
 * 本部分为修改关注关系
 * api如下
 *  1. addFollowing
 *  2. cancalFollowing
 * （未来可出的功能： 删除粉丝，拉黑他人） 
 *  */ 

/**
 * 发起关注
 * 场景：@/components/user/ProfileCard
 *      @/components/user/useInfo
 *      @/components/user
 * 请求字段：
 *  @param {number} userId 发起关注请求的用户id
 *  @param {number} upId 接受请求的对象用户id
 * 返回字段：
 *  @return {boolean} 操作是否成功
 */
export const addFollowing = async(userId, upId) => {
    try {
        const postURL = basic_follow + 'follow'
        const response = await request.post(postURL, {
            fansId: userId,
            idolId: upId
        })
        return response
    } catch(e) {
        ElMessage.error(`发起关注失败：${e}`)
        console.error(`发起关注失败：${e}`)
    }
}

/**
 * 取消关注
 * 场景：@/components/ProfileCard
 *      @/Pages/user/UserCenter(中的关注/粉丝列表)
 * 请求字段：
 *  @param {number} userId 发起关注请求的用户id
 *  @param {number} upId 接受请求的对象用户id
 * 返回字段：
 *  @return {boolean} 操作是否成功
 */
export const removeFollowing = async(userId, upId) => {
    try{
        const postURL = basic_follow + 'recallFollow'
        const response = await request.post(postURL, {
            fansId: userId,
            idolId: upId
        })
        return response
    } catch(e) {
        ElMessage.error(`取消关注失败：${e}`)
        console.error("取消关注失败：", e)
    }   
}

/**
 * 查看关注列表
 */
export const fetchFollowingsList = async(userId) => {
    try {
        const getURL = basic_follow + `getIdolOrFansList/${userId}/0`
        const response  = await request.post(getURL, {
            userId: userId,
            type: 0
        })
        return response
    } catch (e) {
        ElMessage.error("查看关注列表失败")
        console.error("查看关注列表失败（接口getIdolOrFansList, type-0）", e)
    }
}

/**
 * 查看粉丝列表
 */
export const fetchFollowersList = async(userId) => {
    try {
        const getURL = basic_follow + `getIdolOrFansList/${userId}/1`
        const response  = await request.post(getURL, {
            userId: userId,
            type: 1
        })
        console.log("粉丝列表:", response)
        return response
    } catch (e) {
        ElMessage.error("查看粉丝列表失败")
        console.error("查看粉丝列表失败（接口getIdolOrFansList, type-0）：", e)
    }
}