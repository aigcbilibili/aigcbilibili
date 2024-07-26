package ljl.bilibili.chat.entity;

import lombok.Data;
@Data
//大模型回复类
public class ChatMessage {
    //大模型回复内容
    String content;
    //接收大模型消息的用户id
    String userId;
    //该消息状态
    Integer status;
    public ChatMessage(String content,String userId,Integer status){
        this.content=content;
        this.userId=userId;
        this.status=status;
    }
}
