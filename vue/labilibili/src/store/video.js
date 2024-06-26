// 视频详细信息
import { defineStore } from 'pinia'
import CryptoJS from 'crypto-js' // 使用AES对称加密
import piniaPersistConfig from '@/utils/persist'

const secretKey = CryptoJS.enc.Utf8.parse('helloWorld') // 密钥
export const useVideoInfo = defineStore({
    id: 'videoStore',
    state: () => ({
        videoId: Number, // 视频id
        encryptedVideoUrl: String, // 加密后Url
    }),
    getters:{
        // 获取Url
        getVideoUrl(){
            const e_videoUrl = this.encryptedVideoUrl
            if(e_videoUrl){
                return this.decryptedMethod()
            }else{
                throw "不存在视频地址！"
            }
        }
    },
    actions: {
        // 加密
        encryptedMethod(data){
            data = CryptoJS.enc.Utf8.parse(data)
            const ciphertext = CryptoJS.AES.encrypt(data, secretKey,{ mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 }).toString()
            return ciphertext
        },
        // 保存数据：videoUrl是String类型
        saveData(videoId, videoUrl){
            this.videoId = videoId
            this.encryptedVideoUrl = this.encryptedMethod(videoUrl)
        },
        // 解密
        decryptedMethod(ciphertext){
            const res = CryptoJS.AES.decrypt(ciphertext, secretKey, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 });
            const decryptedData = CryptoJS.enc.Utf8.stringify(res).toString()
            return decryptedData
        },
    },
    persist: piniaPersistConfig('video', ['videoId','encryptedVideoUrl'])
})