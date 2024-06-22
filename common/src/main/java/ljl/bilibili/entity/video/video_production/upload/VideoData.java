package ljl.bilibili.entity.video.video_production.upload;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("video_data")
@Accessors(chain = true)
public class VideoData {

    @TableField("video_id")
    private Integer videoId;

    @TableField(value = "like_count",fill = FieldFill.INSERT)
    private Integer likeCount;

    @TableField(value = "collect_count",fill = FieldFill.INSERT)
    private Integer collectCount;

    @TableField(value = "play_count",fill = FieldFill.INSERT)
    private Integer playCount;

    @TableField(value = "danmaku_count",fill = FieldFill.INSERT)
    private Integer danmakuCount;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value="comment_count",fill = FieldFill.INSERT)
    private Integer commentCount;
}
