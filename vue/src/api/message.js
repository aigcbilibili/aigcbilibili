// 消息整合
import request from "./index.js"


// 请求接口的详细写法
// export const xxxx = (params) => requests({
//     url: '', // 请求地址
//     method: 'post', // 请求方式
//     // data: params, // (一般post请求，我们习惯使用 data属性来传参)
//     params: params //(一般get请求，我们习惯使用params属性来传参）
//     // 注意：data，和 params 两个属性传参使用，并不是固定的，也可以调换使用。
// })

// 请求接口的简写形式
export const getMessage = () => request.get('/')