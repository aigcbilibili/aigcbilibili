package ljl.bilibili.video.vo.response.audience_reactions.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CollectGroupResponse {
    @ApiModelProperty("收藏夹名字")
    private String collectGroupName;
    @ApiModelProperty("收藏夹id，便于用户收藏时绑定到收藏夹中，同时收藏夹id是唯一且绑定了用户，所以可以通过该id和唯一用户绑定")
    private int collectGroupId;
    @ApiModelProperty("收藏夹是否已收藏了当前视频")
    private Boolean hasCollect=false;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
