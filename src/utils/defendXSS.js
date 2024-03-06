// 防御XSS相关的配置和逻辑
import xss from "xss"
const option = { // 白名单，不在白名单上的标签将被过滤：
    a: ['href', 'target', 'download', 'type', 'style'],
    iframe: ['src', 'width', 'height', 'class', 'style', 'frameborder', 'allowfullscreen'],
    table: ['style', 'border', 'cellspacing', 'cellpadding'],
    img: ['src', 'width', 'alt', 'style'],
    video: ['src', 'autoplay', 'controls', 'height', 'loop', 'muted', 'poster', 'preload', 'width'],
    audio: ['src', 'autoplay', 'controls', 'loop', 'muted', 'preload'],
    blockquote: [],
    pre: [],
    code: []
    // ...
}

export const setDefendXSS = () => {

}