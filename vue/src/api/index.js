/**
 * NOTE
 * 基于promise可以用于浏览器和node.js的网络请求库。在服务端它使用原生node.js http 模块, 而在客户端 (浏览端) 则使用 XMLHttpRequests。
 * 简而言之，axios本质属于XMLHttpRequest，其制作需要实现ajax或基于http、需一个promise对象处理结果
 * 
 * 特点：
 *     从浏览器创建 XMLHttpRequests，
 *     从 node.js 创建 http 请求
 *     支持 Promise API
 *     拦截请求和响应
 *     转换请求和响应数据
 *     取消请求
 *     自动转换JSON数据
 *     客户端支持防御XSRF
 */

import axios from 'axios' 
import router from '../router' 
import { ElMessage } from 'element-plus'
import { useRefreshToken } from '../store/token'
/**
 * 1. 单个接口封装 request
 */
var baseURL = '/api' // /api
const request = axios.create({
    // url: 会加在baseURL后面（e.g./comment），除非是绝对地址
    baseURL: baseURL, 
    timeout: 5000, // 请求超过1000ms未取得结果，提示超时
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'shortauthorization': '',
        'laBiliBiliHeader': 'test_method_1'
    },
    withCredentials: true, // 跨域请求时是否需要使用cookies发送cookie
}) 

/**
 * 请求拦截处理
 * interceptors axios的拦截器对象
 * @param config 请求的信息
 * @param 
 * 
 * @return config
 */
request.interceptors.request.use(config => {
    console.log("断点111111", config)
    return config 
},err => {
    return Promise.reject('请求发出后的错误：', err)
})

/**
 * 响应拦截处理
 * interceptors axios的拦截器对象
 * @param none 无参数
 *
 * @return config
 */
request.interceptors.response.use(res => {
    console.log("断点222222222", res.status,"data", res)
    // 处理请求后的错误状态码
    if(res.status === 200){
        return res.data.data
    }else if(res.status === 403){
        router.push('./403')
    }else if(res.status === 404){
        router.push('./404')
    }else if(res.status === 401) {
        ElMessage.error("登录状态过期")
        const refreshTokenStore = useRefreshToken()
        refreshTokenStore.shortToken = ''
        refreshTokenStore.isTokenPolling = false
        request.defaults.headers.common['shortauthorization'] = ''
        router.push('/login')
    }else{
        console.log('错误！状态码', res.status, "\n具体错误信息", res.data.msg)
    }
},err => {
    return Promise.reject('接受响应后的错误：',err)
})

export default request