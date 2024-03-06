// 长短token刷新
import { defineStore } from 'pinia'
import { encryptedMethod, decryptedMethod } from "@/utils/crypto.js"
import piniaPersistConfig from '@/utils/persist'
export const useRefreshToken = defineStore({
    id: 'refreshToken',
    state: () => ({
        isTokenPolling: false, // 是否能调用
        shortToken: '' // 短token
    }),
    actions: {
        changeTokenPolling(){
            this.isTokenPolling = !this.isTokenPolling
        },
        // 保存数据：shortToken是加密后的String类型
        saveData(shortTokenRaw){
            this.shortToken = shortTokenRaw // encryptedMethod(shortTokenRaw)
        },
        getData(){
            // return decryptedMethod(this.shortToken)
            return this.shortToken
        },
    },
    persist: piniaPersistConfig('token', ['isTokenPolling','shortToken'])
})