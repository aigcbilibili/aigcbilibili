/**
 * 本部分请求登录信息
 * api如下
 *  1. verification 验证码
 *  2. varifyLogin 请求登录
 *  */
import request from "./index.js"

/**
 * 验证码
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {base64}/{blob} captcha
 */
export const getCaptcha = async () => {
    try{
        const response =  await request.post('/login/getCaptcha', {})
        const imgSrc = "data:image/png;base64," + response
        return imgSrc     
    }catch(e){
        console.error('Error loading captcha:', e)
    }
}

/**
 * 请求登录
 * 场景：@/Pages/user/UserCenter
 * 请求字段：统一为loginRequest
 * @param {string} userName
 * @param {string} password
 * @param {string} captcha
 * 返回字段
 * @return {string} token
 * @return {number} userId
 */
export const verifyLogin = async (userName, password, captcha) => {
    try {
        const response = await request.post('/login/login', {
            userName: userName,
            password: password,
            captcha: captcha
        })
        console.log('看下data?', response)
        console.log('看下user？',response.user)
        // const token = window.JSON.parse(response.token)
        if (typeof token !== 'string') {
            throw new Error('Invalid token')
        } else {
            return token
        }
    } catch (e) {
        console.error('服务端返回的错误',e);;
    }
}