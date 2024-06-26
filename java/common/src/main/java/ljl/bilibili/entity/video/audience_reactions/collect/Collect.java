package ljl.bilibili.entity.video.audience_reactions.collect;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("collect")
public class Collect {


    @TableField("video_id")
    private Integer videoId;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("collect_group_id")
    private int collectGroupId;

    @TableId(type = IdType.AUTO)
    private Integer id;

}
