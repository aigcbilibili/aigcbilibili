/**
 * 本部分请求登录信息
 * api如下
 *  1. verification 验证码
 *  2. varifyLogin 请求登录
 *  */
import { ElMessage } from "element-plus"
import { useRefreshToken } from '@/store/token' // 长短token的使用
import request from "./index.js"
import axios from "axios"

const login_basic_url = '/login/'
const login_api_url = '/api/login/'
const getAndRefreshToken = (response) => {
    console.log('response', response);
    const authToken = response.headers['shortauthorization']
    const refreshTokenStore = useRefreshToken()
    if (authToken) {
        request.defaults.headers.common['shortauthorization'] = authToken
        refreshTokenStore.isTokenPolling = true
        refreshTokenStore.saveData(authToken)
    }
}

/**
 * 请求账密的验证码
 * 场景：@/Pages/login/LoginPage
 *  请求字段：
 * @param none
 * @return {base64}/{blob} captcha
 */
export const getCaptcha = async () => {
    try {
        const response = await request.get('/login/getCaptcha', {})
        const imgSrc = "data:image/png;base64," + response
        return imgSrc
    } catch (e) {
        console.error('Error loading captcha:', e)
    }
}

/**
 * 请求账密登录
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
        console.log(`嘿嘿1-？${userName}, ${password},${captcha}`)
        const response = await axios.post(login_api_url + 'passwordLogin', {
            userName: userName,
            password: password,
            captcha: captcha
        })
        console.log(`嘿嘿2-？`)
        getAndRefreshToken(response)
        return response.data.data
    } catch (e) {
        ElMessage.error('服务端返回的错误', e)
        console.error('服务端返回的错误', e);;
    }
}

/**
 * 请求注册
 */
export const enroll = async (username, nickName, password) => {
    try {
        const postURL = '/register/passwordRegister'
        const response = await request.post(postURL, {
            userName: username,
            nickName: nickName,
            password: password,
        })

        return response
    } catch (e) {
        console.error('注册错误', e)
    }
}

/**
 * 忘记密码
 */
export const forget = async (userName, password, makeSurePassword) => {
    try {
        const postURL = '/forgetPassword/remakePassword'
        const response = await request.post(postURL, {
            userName,
            password,
            makeSurePassword,

        })

        return response
    } catch (e) {
        console.error('错误', e)
    }
}


/**
 * 手机号获取验证码
 */
export const getPhoneCaptcha = async (phoneNumber) => {
    try {
        const getURL = login_basic_url + 'phoneNumberCaptcha/' + phoneNumber
        const response = await request.get(getURL, {
            phoneNumber: phoneNumber
        })
        if (response) {
            ElMessage.success("手机短信已发送，请查收")
        } else {
            ElMessage.error("手机短信发送失败")
        }
        return response
    } catch (e) {
        console.error('手机号验证码获取失败：', e)
        ElMessage.error("手机号验证码获取失败")
    }
}

/**
 * 手机号登录
 */
export const sendPhoneLogin = async (phoneNumber, captcha) => {
    try {
        const postURL = login_api_url + `phoneNumberLogin`
        const response = await axios.post(postURL, {
            phoneNumber: phoneNumber,
            captcha: captcha
        })
        console.log(`查看手机号结果：${JSON.stringify(response)}`)
        getAndRefreshToken(response)
        return response.data.data
    } catch (e) {
        console.error('手机号登录失败：', e)
        ElMessage.error("手机号登录失败")
    }
}

/**
 * 邮箱验证码
 */
export const emailCaptcha = async (mailNumber) => {
    try {
        const getURL = login_basic_url + `mailNumberCaptcha/` + mailNumber
        const response = await request.get(getURL, {
            mailNumber: mailNumber
        })
        if (response) {
            ElMessage.success("邮箱验证码发送成功")
        } else {
            ElMessage.error("邮箱验证码为空")
        }
        return response
    } catch (e) {
        ElMessage.error(`邮箱验证码发送失败：${e}`)
        console.error(`邮箱验证码发送失败：${e}`)
    }
}

/**
 * 邮箱登录
 */
export const emailLogin = async (mailNumber, captcha) => {
    try {
        const postURL = login_api_url + `mailLogin`
        const response = await axios.post(postURL, {
            mailNumber: mailNumber,
            captcha: captcha
        })
        console.log(`登录这里：${JSON.stringify(response)}`)
        getAndRefreshToken(response)
        return response.data.data
    } catch (e) {
        ElMessage.error(`邮箱登录失败：${e}`)
        console.error(`邮箱登录失败：${e}`)
    }
}

/**
 * token刷新：网关
 */
export const validateToken = async () => {
    try {
        const postURL = '/api/login/refreshToken'
        const refreshTokenStore = useRefreshToken()
        const short_token = refreshTokenStore.getData()
        const response = await axios.post(postURL, {}, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'shortauthorization': short_token,
                'laBiliBiliHeader': 'test_method_1'
            }
        })
        // 将headers设置到instance实例
        const authToken = response.headers['shortauthorization']
        if (authToken) {
            request.defaults.headers.common['shortauthorization'] = authToken
            refreshTokenStore.saveData(authToken)
        }
        return response.data

    } catch (e) {
        ElMessage.error("token刷新失败")
        console.error(`token刷新失败${e}`)
    }
}