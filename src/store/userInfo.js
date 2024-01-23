// 用户信息
import { defineStore } from 'pinia'

export const useUserInfo = defineStore({
    id: 'userStore',
    state: () => ({
        token: '123', // TODO 存储用户token，待优化
        name: '咸鱼1号',
        id: '1',
        avatar: '@/asstes/img/avater.png', // 需要使用img标签
    }),
    actions: {
        setAll(name, token, id, avatar){
            this.setName(name)
            this.settoken(token)
            this.setId(id)
            this.setavatar(avatar)
        },
        setName(name){
            this.name = name
            localStorage.setItem('name', JSON.stringify(this.name))
        },
        getName(){
            return this.name
        },
        setToken(token){
            this.token = token
            localStorage.setItem('token', JSON.stringify(this.token))
        },
        getToken(){
            return this.token
        },
        setId(id){
            this.id = id
            localStorage.setItem('id', JSON.stringify(this.id))
        },
        getId(){
            return this.id
        },
        setAvatar(avatar){
            this.avatar = avatar
            localStorage.setItem('avatar', JSON.stringify(this.avatar))
        },
        getAvater(){
            return this.avatar
        },
    }
})