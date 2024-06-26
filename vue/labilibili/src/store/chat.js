// 保存聊天
import { defineStore } from 'pinia'
import piniaPersistConfig from '@/utils/persist'
export const useChat = defineStore({
    id: 'chatStore',
    state: () => ({
        sessionId: 0, 
        currentUpId: 0,
        currentUpName: '',
        isUserChat: false
    }),
    actions: {
        setIsUserChat(boolean_value){ // 当前是否有用户聊天
            this.isUserChat = boolean_value
        },
        getIsUserChat() {
            return this.isUserChat
        },
        setCurrentUp(currentUpId, currentUpName) {
            this.currentUpId = currentUpId
            this.currentUpName = currentUpName
        },
        getCurrentUp() {
            return [this.currentUpId, this.currentUpName]
        },
        setSessionId(sessionId) {
            this.sessionId = sessionId
        },
        getSessionId() {
            return this.sessionId
        },
        setUpId(upId) {
            this.currentUpId = upId
        },
        getUpId() {
            return this.currentUpId
        },
        setUpName(upName) {
            this.currentUpName = upName
        },
        getUpName() {
            return this.currentUpName
        }
    },
    persist: [
        piniaPersistConfig('sessionId', ['sessionId'], 'sessionStorage'),
        piniaPersistConfig('currentUpName', ['currentUpName'],'sessionStorage'),
        piniaPersistConfig('currentUpId',['currentUpId'],'sessionStorage'),
        piniaPersistConfig('isUserChat', ['isUserChat'],'sessionStorage')
    ]
})