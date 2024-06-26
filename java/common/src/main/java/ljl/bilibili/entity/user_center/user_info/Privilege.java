package ljl.bilibili.entity.user_center.user_info;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableField("collect_group")
    Integer collectGroup;
    @TableField("remotely_like")
    Integer remotelyLike;
    @TableField("fans_list")
    Integer fansList;
    @TableField("idol_list")
    Integer idolList;
}
