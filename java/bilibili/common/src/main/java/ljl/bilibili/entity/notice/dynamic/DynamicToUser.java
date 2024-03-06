package ljl.bilibili.entity.notice.dynamic;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("dynamic_to_user")
@Accessors(chain = true)
public class DynamicToUser {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("user_id")
    Integer userId;
    @TableField("dynamic_id")
    Integer dynamicId;
    @TableField(value = "status",fill = FieldFill.INSERT)
    Integer status;
}
