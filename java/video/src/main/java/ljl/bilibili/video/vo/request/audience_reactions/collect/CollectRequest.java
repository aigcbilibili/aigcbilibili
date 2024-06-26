package ljl.bilibili.video.vo.request.audience_reactions.collect;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.video.audience_reactions.collect.Collect;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class CollectRequest {
    @ApiModelProperty("收藏的视频的id")
    private Integer videoId;
    @ApiModelProperty("该用户的收藏夹的id")
    private Integer collectGroupId;
    @ApiModelProperty("操作类型,true是收藏，false是取消收藏")
    private Boolean type;
}
