import { ElMessage } from "element-plus"
import request from "./index"

/**
 * 本部分获取特点用户（分为up, user, other）的权限信息
 * api如下
 *  1. getPermiss
 *  2. editPermiss: 统一修改完再返回，刷新后他人能看到
 *  */
const basic_url = 'selfCenter/'
export const fetchPermiss = async (userId) => {
    const getURL = basic_url + `getUserPrivilege/${userId}`
    try {
        const response = await request.get(getURL)
        return response
    } catch (e) {
        ElMessage.error("获取用户权限失败")
        console.log(`获取用户权限失败（${getURL})`, e)
    }
}

export const editPermiss = async (obj) => {
    const postURL = basic_url + 'editUserPrivilege'
    try {
        const response = await request.post(postURL, obj)
    } catch (e) {
        ElMessage.error("编辑用户权限失败")
        console.log(`编辑用户权限失败（${postURL})`, e)
    }
}
