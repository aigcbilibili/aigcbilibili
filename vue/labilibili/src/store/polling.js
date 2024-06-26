
// 本页面存放更新用户信息和消息的长轮询
import { defineStore } from 'pinia'

export const usePollingStore = defineStore('polling', {
    state: () => ({
      isPolling: false,
      timeStamp: ""
    }),
    actions: {
      startPolling() {
        this.isPolling = true
        // 发送长轮询请求的逻辑
        switch(loacation) {
            case '':

                break
            case '':
                break
        }
      },
      stopPolling() {
        this.isPolling = false;
        // 停止长轮询的逻辑
      }
    }
})