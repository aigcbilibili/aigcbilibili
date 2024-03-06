package ljl.bilibili.chat.entity;

import lombok.Data;
@Data
public class ChatMessage {
    String content;
    String userId;
    Integer status;
    public ChatMessage(String content,String userId,Integer status){
        this.content=content;
        this.userId=userId;
        this.status=status;
    }
}
