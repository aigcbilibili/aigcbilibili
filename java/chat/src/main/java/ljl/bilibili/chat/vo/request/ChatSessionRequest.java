package ljl.bilibili.chat.vo.request;

import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.chat.ChatSession;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ChatSessionRequest {
    Integer senderId;
    Integer receiverId;
    String updateContent;
    public ChatSession toSessionEntity(){
        ChatSession chatSession=new ChatSession();
        BeanUtils.copyProperties(this,chatSession);
        return chatSession;
    }
    public Chat toChatEntity(){
        Chat chat=new Chat();
        BeanUtils.copyProperties(this,chat);
        return chat;
    }
}
