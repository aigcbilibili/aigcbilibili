package ljl.bilibili.entity.notice.comment;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("comment_notice")
@Accessors(chain = true)
public class CommentNotice {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("sender_id")
    private Integer senderId;
    @TableField("video_id")
    private Integer videoId;
    @TableField("receiver_id")
    private Integer receiverId;
    @TableField(value = "status",fill = FieldFill.INSERT)
    private Integer status;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
