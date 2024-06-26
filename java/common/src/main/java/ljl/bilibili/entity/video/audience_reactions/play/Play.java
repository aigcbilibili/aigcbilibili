package ljl.bilibili.entity.video.audience_reactions.play;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author liao
 */
@TableName("play")
@Data
@Accessors(chain = true)
public class Play {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("video_id")
    int videoId;
    @TableField("user_id")
    int userId;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    LocalDateTime createTime;
}
