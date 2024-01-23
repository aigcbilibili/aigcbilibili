/**
 * 本部分获取特点用户（权限分为up, user, other）的关注数、粉丝数等信息
 * api如下
 *  1. getUserInfo 
 *  2. getProfile
 *  3. getFollowing
 *  4. getFollower
 *  */ 
import request from "./index.js"

/**
 * 请求单个用户的
 */
export const getUserInfo = () =>{
    
}


/**
 * 请求单个用户的Profile
 * 场景：@/components/ProfileCard
 *      @/Pages/user/UserCenter
 * 请求字段：
 *  @param {number} 请求的对象用户id：对应userId
 * 返回字段：
 *  @return {string} name：对应nickname 
 *  @return {string} avatar 头像的img地址
 *  @return {number} followingNum 关注数：对应fansCount
 *  @return {number} followersNum 粉丝数：对应idolCount
 *  @return {string} intro 简介
 *  @return {number} playCount 播放量
 *  @return {number} videoLike 点赞量
 *  @return {number} isFocus 登录用户已经关注对方：0未关注，1已关注，2互相关注
 *  */ 
export const getProfile = async(userId) =>{
    try{
        const response = await request.get(`/getUserInfo/${userId}`)
        return response.data
    }catch(e){
        console.log(e)
    }
} 

/**
 * 请求单个用户的关注列表
 * 场景：@/Pages/user/UserCenter
 * 请求字段：
 *  @param {number} id 请求的对象用户id
 *  @param {boolean} isSame 请求对象id与自身是否一致，不一致则需修改对象的权限数组permiss、否则返回默认值true
 * 返回字段
 *  @return {[]} followingList 关注列表，其中包含属性id, name, avatar, followering, follower, intro（即返回多个getProfile类型的结果）           
 *  @return {boolean} permiss 是否允许他人查看关注列表，默认允许
 */
export const getFollowing = (id, isSame) => request.get(`/profile/following/${id}`+'?'+isSame)

/**
 * 请求单个用户的粉丝列表
 * 场景：@/Pages/user/UserCenter
 * 请求字段：
 *  @param {number} id 请求的对象用户id
 *  @param {boolean} isSame 请求对象id与自身是否一致，不一致则需修改对象的权限数组permiss、否则返回默认值true
 * 返回字段
 *  @return {[]} followerList 粉丝列表，其中包含属性id, name, avatar, followering, follower, intro（即返回多个getProfile类型的结果）           
 *  @return {boolean} permiss 是否允许他人查看关注列表，默认允许
 */
export const getFollower = (id, isSame) => request.get(`/profile/follower/${id}`+'?'+isSame)