import axios from 'axios' // NOTE 不是vue.js的插件不能用app.use()

/**  
* 对评论操作的封装
* @param
*/
export const operateComment = (commentId, type, [num])=>{
    switch(type){
        // 用户权限
        case "addSingle":{
            addSingle()
        } 
        // UP主对用户，用户权限
        case "deleteSingle":{
            deleteSingle()
        } 
        // UP主权限
        case "deleteAll":{
            
        } 
        // 用户权限
        case "editSingle": {

        } 
        // 用户权限
        case "":{

        }
        // UP主，用户权限
        case "":{

        }
        default: alert("类型错误，调用失败")
    }
}

/**  
* 获取单个评论
* @param
*/
export const fetchSingleComment = () => {
    const data = axios.get('localhost:3000/comment', function (req, res){
        console.log('读取成功')
    })
    return data
}

export const fetchAllComments = () => {
    const data = axios.get('localhost:3000/comment', function (req, res){
        console.log('读取成功')
    })
    return data
}