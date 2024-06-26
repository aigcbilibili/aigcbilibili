package ljl.bilibili.entity.user_center.user_info;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("user")
@Accessors(chain = true)
public class User implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String userName;
    @TableField("phone_number")
    private String phoneNumber;
    @TableField("mail_number")
    private String mailNumber;
    @TableField("password")
    private String password;

    @TableField(value = "cover",fill = FieldFill.INSERT)
    private String cover;

    @TableField(value = "nickname",fill = FieldFill.INSERT)
    private String nickname;

    @TableField("intro")
    private String intro;
}
