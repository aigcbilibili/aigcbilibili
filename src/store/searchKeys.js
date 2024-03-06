// 搜索结果
import { defineStore } from 'pinia'
import piniaPersistConfig from '@/utils/persist'

export const useSearchKeys = defineStore({
    id: 'searchKeysStore',
    state: () => ({
        keywordsList: [] // 搜索keywords
    }),
    actions: { // NOTE getters属性和 actions 中的方法应该使用函数声明而不是箭头函数，以便在方法内部正确访问 store 的状态和其他方法。
        // 新增keywords
        setKeywords(keyword){
            this.keywordsList.push(keyword)
        },
        // 获取keywords
        getKeyWords(){
            return this.keywordsList
        },
        deleteOneKeyword(startId) {
            this.keywordsList.splice(startId, 1)
        },
        // 清空keywords
        deletAllKeywords(){
            this.keywordsList = []
        }
    },
    persist: piniaPersistConfig('searchKeys', 'keywordsList')  
})