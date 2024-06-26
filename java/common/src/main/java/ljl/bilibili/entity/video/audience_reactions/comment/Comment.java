package ljl.bilibili.entity.video.audience_reactions.comment;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {

    @TableField("user_id")
    private Integer userId;

    @TableField("video_id")
    private Integer videoId;

    @TableField("parent_id")
    private Integer parentId;

    @TableField("content")
    private String content;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("top_id")
    private Integer topId;
    @TableId(type = IdType.AUTO)
    private Integer id;

}
