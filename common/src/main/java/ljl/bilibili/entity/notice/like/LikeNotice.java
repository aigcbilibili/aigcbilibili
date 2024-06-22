package ljl.bilibili.entity.notice.like;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("like_notice")
public class LikeNotice {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("sender_id")
    private Integer senderId;

    @TableField("video_id")
    private Integer videoId;

    @TableField("comment_id")
    private Integer commentId;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "status",fill = FieldFill.INSERT)
    private Integer status;
}
