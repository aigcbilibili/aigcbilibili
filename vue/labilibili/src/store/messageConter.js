
import { defineStore } from 'pinia'

export const usemessageConter = defineStore('messageConter', {
  state: () => ({
    data: {
      chatCount: 0,
      commentCount: 0,
      dynamicVideoCount: 0,
      likeCount: 0,
      totalCount: 0,
    }
  }),
  actions: {
    setData(val) {
      this.data = val
    },


  }
})