import { ElMessage } from "element-plus"
import { useUserInfo } from '@/store/userInfo'
import { fetchUserInfo } from '@/api/user'
const userInfo = useUserInfo() // 保存登录信息

/**
 * 正则表达式防不规范输入
 */
const regStr = [{
    name: "phone", // 手机号登录
    string: /^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\d{8}$/
}, {
    name: "messageCaptcha", // 手机登录：6位数短信验证码
    string: /^\d{6}$/
}, {
    name: "nickname", // 账密登录：昵称
    string: /^\d{4}$/
}, {
    name: "password", // 账密登录：密码
    string: /^\d{4}$/
}, {
    name: "imgCaptcha", // 账密登录：4位数数字验证码
    string: /^\d{4}$/
}, {
    name: "emailAccount", // 邮箱登录：账号
    string: /\w+@\w+(\.\w+)+/
}, {
    name: "emailCaptcha", // 邮箱登录：验证码
    string: /^\d{6}$/
}]
// 使用正则表达式限制字符串格式
export const isMeetReg = (typeId, inputStr, errorMessage) => {
    // 特别地，手机号需要控制长度
    if (typeId === 0 && inputStr.length !== 11) {
        ElMessage.error("手机号格式错误！")
        return false
    }

    if (regStr[typeId].string.test(inputStr)) {
        return true
    } else {
        ElMessage.error(errorMessage)
        return false
    }
}

// 保存用户信息
export const setUser = async (loginRes) => {
    console.log('setUser', loginRes);
    const userId = loginRes.userId
    if (userId > 0 && typeof userId === 'number' && loginRes.status) {
        let res = await fetchUserInfo(userId, userId);
        console.log('userInfo', res);
        console.log(`登录的结果${userId}`)
        ElMessage.success('登录成功')

        // userInfo.setId(userId) // 保存本地信息
        userInfo.setAll(res.name, res.id, res.avatar)
        return true
    } else {
        ElMessage.error(`登录失败，无法获取userId(${loginRes})。请检查用户名/账号、密码或验证码`)
        return false
    }
}