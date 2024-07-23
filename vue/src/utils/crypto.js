// 加密
import CryptoJS from 'crypto-js' // 使用AES对称加密
const secretKey = CryptoJS.enc.Utf8.parse('helloWorld') // 密钥
// 加密函数
export const encryptedMethod = (data) =>{
    data = data.replace(/\s/g, '')
    const ciphertext = CryptoJS.AES.encrypt(JSON.stringify(data), 
    secretKey,{ mode: CryptoJS.mode.CBC, 
        padding: CryptoJS.pad.Pkcs7 }).toString()
    return ciphertext
}
// 解密函数
export const decryptedMethod = (ciphertext) => {
    
    const res = CryptoJS.AES.decrypt(
        ciphertext.toString(),
        secretKey, { mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
    const decryptedData = res.toString(CryptoJS.enc.Utf8)
    return decryptedData
}