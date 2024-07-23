package ljl.bilibili.entity.user_center.user_info;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("privilege")
@Accessors(chain = true)
public class Privilege {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("user_id")
    Integer userId;
    @TableField(value = "collect_group",fill = FieldFill.INSERT)
    Integer collectGroup;
    @TableField(value = "remotely_like",fill = FieldFill.INSERT)
    Integer remotelyLike;
    @TableField(value = "fans_list",fill = FieldFill.INSERT)
    Integer fansList;
    @TableField(value = "idol_list",fill = FieldFill.INSERT)
    Integer idolList;
}
