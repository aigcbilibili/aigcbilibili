// 用户信息
import { defineStore } from 'pinia'
import piniaPersistConfig from '@/utils/persist'

export const useUserInfo = defineStore({
    id: 'userStore',
    state: () => ({
        name: '咸鱼1号',
        id: 0,
        avatar: require('@/assets/img/avater.png'), // 需要使用img标签
        // followingNum: 100,
        // followersNum: 50
    }),
    actions: {
        setAll(name, id, avatar) {
            this.setName(name)
            this.setId(id)
            this.setAvatar(avatar)
        },
        setName(name) {
            this.name = name
            // localStorage.setItem('name', JSON.stringify(this.name))
        },
        getName() {
            return this.name
        },
        setId(id) {
            this.id = id
        },
        getId() {
            return this.id
        },
        setAvatar(avatar) {
            this.avatar = avatar
        },
        getAvatar() {
            return this.avatar
        },
    },
    persist: piniaPersistConfig('user', ['name', 'id', 'avatar'])
})