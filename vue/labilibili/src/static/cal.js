/**
 * 放些计算的函数
 */

// 将字符串和date类型间进行转化
export const convertDate = ()=>{
    
}

// 计算是几月之前
export const calBeforeTime = ()=>{
    let date = new Date()
    let year = date.getFullYear()
    let month = date.getMonth() + 1
    let day = date.getDate()
    let hour = date.getHours()
    let minute = date.getMinutes()
    let second = date.getSeconds()
    return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}