/**
 * 本部分为修改关注关系
 * api如下
 *  1. addFollowing
 *  2. cancalFollowing
 * （未来可出的功能： 删除粉丝，拉黑他人） 
 *  */ 
import request from "./index.js"

/**
 * 发起关注
 * 场景：@/components/ProfileCard
 *      @/Pages/user/UserCenter(中的关注/粉丝列表)
 * 请求字段：
 *  @param {number} send_follow_id 发起请求的用户id
 *  @param {number} get_follow_id 接受请求的对象用户id
 * 返回字段：
 *  @return {boolean} 操作是否成功
 */
export const addFollowing = () => {request.post()}

/**
 * 取消关注
 * 场景：@/components/ProfileCard
 *      @/Pages/user/UserCenter(中的关注/粉丝列表)
 * 请求字段：
 *  @param {number} send_follow_id 发起请求的用户id
 *  @param {number} get_follow_id 接受请求的对象用户id
 * 返回字段：
 *  @return {boolean} 操作是否成功
 */
export const removeFollowing = () => {request.post()}