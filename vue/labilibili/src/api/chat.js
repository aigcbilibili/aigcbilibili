import { ElMessage } from "element-plus"
import request from "./index.js"

// 私聊界面使用的接口
const chat = '/chat/'
/**
 * 获取所有用户列表
 */
export const fetchAllPeople = async (userId) => {
    try {
        const getURL = chat + `getHistoryChatSession/${userId}`
        const response = await request.get(getURL, {
            userId: userId
        })
        return response.map((item) => ({
            avatar: item.cover,
            createTime: item.updateTime,
            upName: item.nickname,
            upId: item.userId,
            leastMessage: item.updateContent,
            leastMessageFrom: 1, // 0是不存在，1是自己，2是对方
            type: 0,
            status: item.status,
            count: item.count
        }))
    } catch (e) {
        ElMessage.error(`获取所有用户列表错误：${e}`)
        console.error(`获取所有用户列表错误：${e}`)
    }
}

/**
 * 接入星火大模型
 */
export const chatToXingHuo = async () => {
    try {
        const getURL = ''
        const response = await request.get(getURL, {

        })
    } catch (e) {
        ElMessage.error(`大模型：${e}`)
        console.error(`获取所有用户列表错误：${e}`)
    }
}

/**
 * 获取当前用户和另一个人的历史聊天记录
 */
export const fetchChatHistory = async (userSelf, userOther) => {
    try {
        const getURL = chat + `getHistoryChat/${userSelf}/${userOther}`
        const response = await request.get(getURL, {
            userId: userSelf,
            receiverId: userOther
        })
        return response.map((item) => ({
            id: item.id,
            createTime: item.createTime,
            content: item.content,
            upId: item.senderId
        })).reverse()
    } catch (e) {
        ElMessage.error(`获得当前历史聊天失败：${e}`)
        console.error(`获得当前历史聊天失败：${e}`)
    }
}

/**
 * 发布回复
 */
export const addReply = async (content, senderId, receiverId, sessionId) => {
    try {
        const postURL = chat + `addHistoryChat`
        const response = request.post(postURL, {
            content: content,
            senderId: senderId,
            receiverId: receiverId,
            sessionId
        })
        return response
    } catch (e) {
        ElMessage.error(`发布回复接口错误：${e}`)
        console.error(`发布回复接口错误：${e}`)
    }
}

/**
 * 修改会话的最后聊天时间
 */
export const changeSessionTime = async (senderId, receiverId, content) => {
    try {
        const postURL = chat + 'changeChatSessionTime'
        const response = await request.post(postURL, {
            senderId: senderId,
            receiverId: receiverId,
            updateContent: content
        })
        return response
    } catch (e) {
        ElMessage.error(`修改会话聊天时间失败：${e}`)
        console.error(`修改会话聊天时间失败：${e}`)
    }
}

/**
 * 修改聊天记录为已读
 */
export const editChatToRead = async (senderId, receiverId) => {
    try {
        const postURL = chat + 'changeChatStatus'
        const response = await request.post(postURL, {
            userId: senderId,
            receiverId: receiverId
        })
        return response
    } catch (e) {
        ElMessage.error(`修改聊天记录为已读失败：${e}`)
        console.error(`修改聊天记录为已读失败：${e}`)
    }
}

/**
 * 新建聊天
 */
export const addNewChat = async (senderId, receiverId, updateContent) => {
    try {
        console.log("nirenne ")
        const postURL = chat + 'addChatSessionAndContent'
        console.log("nirenne2")
        const response = await request.post(postURL, {
            receiverId: receiverId,
            senderId: senderId,
            updateContent
        })
        return response
    } catch (e) {
        ElMessage.error(`新建聊天会话失败：${e}`)
        console.error(`新建聊天会话失败：${e}`)
    }
}


export const getImage = (url) => {
    console.log('333getImage');
    return request.get(url)
}


export const getPPT = (URL) => {
    return request.get(URL)
}