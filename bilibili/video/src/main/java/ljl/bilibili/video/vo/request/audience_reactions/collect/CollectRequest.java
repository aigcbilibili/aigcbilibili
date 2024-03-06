package ljl.bilibili.video.vo.request.audience_reactions.collect;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.video.audience_reactions.collect.Collect;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CollectRequest {
    @ApiModelProperty("收藏的视频的id")
    private int videoId;
    @ApiModelProperty("该用户的收藏夹的id")
    private int collectGroupId;
    public Collect toEntity(){
        Collect danmaku=new Collect();
        BeanUtils.copyProperties(this,danmaku);
        return danmaku;
    }
}
