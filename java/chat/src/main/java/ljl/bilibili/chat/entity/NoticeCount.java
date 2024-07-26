package ljl.bilibili.chat.entity;

import lombok.Data;

@Data
//未读会话消息数量类
public class NoticeCount {
    //私聊会话id
    private Integer sessionId;
    //私聊数
    private Integer noticeCount;
}
