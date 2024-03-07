import { ElMessage } from "element-plus"

/**
 * 正则表达式防不规范输入
 */
const captchaRegStr = /^\d{6}$/
const regStr = [{
    name: "phone", // 手机号
    string: /^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\d{8}$/
},{
    name: "messageCaptcha", // 手机登录：6位数手机短信
    string:/^\d{6}$/
},{
    name: "Captcha", // 账密登录：4位数数字验证码
    string: /^\d{4}$/
},{
    name: "Captcha", // 账密登录：昵称
    string: /^\d{4}$/
},{
    name: "Captcha", // 账密登录：密码
    string: /^\d{4}$/
},{
    name: "Captcha", // 账密登录：4位数数字
    string: /^\d{4}$/
},{
    
}]
// 使用正则表达式限制字符串格式
export const isMeetReg = (typeId, inputStr, errorMessage ) => {
    // 特别地，手机号需要控制长度
    if(typeId===0 && phoneNumber.length !== 11) {
        turn
        return false
    }
    if(regStr[typeId].string.test(inputStr)) {
        return true
    } else{
        ElMessage.error(errorMessage)
        return false
    }   
}