package ljl.bilibili.entity.video.audience_reactions.like;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("likes")
public class Like {

    @TableField("user_id")
    private Integer userId;
    @TableField("comment_id")
    private Integer commentId;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField("video_id")
    private Integer videoId;
    @TableId(type = IdType.AUTO)
    private Integer id;

}
