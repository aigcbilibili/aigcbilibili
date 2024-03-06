package ljl.bilibili.search.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserEntity {
    private String nickname;
    private Integer id;
    private String intro;
    private String cover;

}
