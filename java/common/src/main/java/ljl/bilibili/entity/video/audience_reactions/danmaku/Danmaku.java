package ljl.bilibili.entity.video.audience_reactions.danmaku;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("danmaku")
public class Danmaku {

    @TableField("user_id")
    private Integer userId;

    @TableField("video_id")
    private Integer videoId;

    @TableField("content")
    private String content;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("place")
    private Integer place;

}
