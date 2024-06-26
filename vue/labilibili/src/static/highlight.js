// 高亮工具
export const highlight = (keyword, suggtion) => {
    // 转义正则表达式中的特殊字符
    const escapedKeyword = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
    const reg = new RegExp(escapedKeyword, 'ig') // 全局g，不区分大小写i
    // 提取出高亮的字
    const newSrt = String(suggtion).replace(reg,(p)=>{
        return '<span style="color:#79b1ff;font-weight:600;">' + p + '</span>'
    })
    return newSrt
}