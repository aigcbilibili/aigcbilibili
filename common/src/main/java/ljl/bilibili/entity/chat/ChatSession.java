package ljl.bilibili.entity.chat;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("chat_session")
@Data
public class ChatSession {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("sender_id")
    Integer senderId;
    @TableField("receiver_id")
    Integer receiverId;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    LocalDateTime createTime;
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    LocalDateTime updateTime;
    @TableField("update_content")
    String updateContent;
}
