package ljl.bilibili.video.vo.request.audience_reactions.danmaku;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.video.audience_reactions.danmaku.Danmaku;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddDanmakuRequest {
    @ApiModelProperty("发布评论的用户id,如果要删除弹幕的话可以用到")
    private int userId;
    @ApiModelProperty("弹幕内容")
    private String content;
    @ApiModelProperty("弹幕所在位置")
    private String place;
    @ApiModelProperty("所在视频")
    public int videoId;
    public Danmaku toEntity(){
        Danmaku danmaku=new Danmaku();
        BeanUtils.copyProperties(this,danmaku);
        return danmaku;
    }
}
