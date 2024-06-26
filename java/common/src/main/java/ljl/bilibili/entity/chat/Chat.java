package ljl.bilibili.entity.chat;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("chat_notice")
public class Chat {

    @TableField("sender_id")
    private Integer senderId;

    @TableField("content")
    private String content;

    @TableField("receiver_id")
    private Integer receiverId;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "status",fill = FieldFill.INSERT)
    private Integer status;
}
