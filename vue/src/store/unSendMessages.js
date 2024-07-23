import { defineStore } from 'pinia'

export const useUnSendMessgaes = defineStore({
    id: 'messageStore',
    state: () => ({
        unSendMessgaes: [] // unsend messages
    }),
    actions: {
        addUnSendMessage(msg){
            this.unSendMessgaes.push(msg)
            this.saveUnsendMessageToLocalStorage()
            return true // 返回true表示调用成功
        },
        // 外部初始化时加载：TODO 快！进一步封装
        loadUnsentMessagesFromLocalStorage() {
            const unsentMessages = JSON.parse(localStorage.getItem('unsendMessages') || '[]')
            this.unsentMessages = unsentMessages
         },
        getUnSendMessages(){
            return this.unSendMessgaes
        },
        removeUnSendMessage(index){
            this.unSendMessgaes.splice(index, 1)
            this.saveUnsendMessageToLocalStorage()
            return true // 返回true表示调用成功
        },
        saveUnsendMessageToLocalStorage(){
            localStorage.setItem('unsendMessages', JSON.stringify(this.unsentMessages))
        }
    }
})