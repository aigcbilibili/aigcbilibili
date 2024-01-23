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

/**
 * 1. 单个接口封装 request
 */
var baseURL = '/api'
const request = axios.create({
    // url: 会加在baseURL后面（e.g./comment），除非是绝对地址
    baseURL: baseURL, // TODO: 后端服务器地址，待改
    timeout: 5000, // 请求超过1000ms未取得结果，提示超时
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'authorization': 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTEiLCJyb2xlIjoidXNlciJ9.ybuiJ87Nq7xgcWrM50P_VgAF1P74fnEN8jCSH5daqR2re4hVTMYgkzMHWZlK104guM75RGWgVxNrtfnhinjR-g'
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
    // 处理请求后的错误状态码
    if(res.status === 200){
        return res.data // XXX如果后端除了string和number有其他类型，再改
    }else if(res.status === 403){
        router.push('./403')
    }else if(res.status === 404){
        router.push('./404')
    }else{
        console.log('错误！状态码', res.status)
    }
},err => {
    return Promise.reject('接受响应后的错误：',err)
})

/**
 * 2. 多个接口，先不封装
 */

// axios.all([
// 	axios.get(),
// 	axios.get(),
// 	axios.post()
// ]).then(
// 	axios.spread((res1,res2,res3)=>{
// 		console.log(res1,res2,res3); //依次打印每一个响应
// 	})
// )
// axios.all([
// 	axios.get(),
// 	axios.get(),
// 	axios.post()
// ]).then(res=>{
// 	console.log(res) //打印一个数组，包含上述三个请求的响应结果
// })

export default request