package ljl.bilibili.search.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserKeyWordSearchResponse {
    @ApiModelProperty("用户昵称")
    private String nickname;
    @ApiModelProperty("用户id")
    private int id;
    @ApiModelProperty("简介")
    private String intro;
    @ApiModelProperty("头像")
    private String cover;
    @ApiModelProperty("粉丝数")
    private int fansCount;
    @ApiModelProperty("作品数")
    private int videoCount;
    @ApiModelProperty("是否已关注该用户")
    private Boolean isFollow;
}
