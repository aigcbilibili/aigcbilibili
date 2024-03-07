package ljl.bilibili.video.vo.request.audience_reactions.collect;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.video.audience_reactions.collect.CollectGroup;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CollectGroupRequest {
    @ApiModelProperty("收藏夹名")
    private String name;
    @ApiModelProperty("所属用户的id")
    private int userId;
    @ApiModelProperty("收藏夹id")
    private Integer id;
    public CollectGroup toEntity(){
        CollectGroup danmaku=new CollectGroup();
        BeanUtils.copyProperties(this,danmaku);
        return danmaku;
    }
}
