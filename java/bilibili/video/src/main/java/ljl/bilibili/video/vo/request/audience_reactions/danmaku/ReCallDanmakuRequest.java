package ljl.bilibili.video.vo.request.audience_reactions.danmaku;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReCallDanmakuRequest {
    @ApiModelProperty("发弹幕的用户id")
    private int userId;
    @ApiModelProperty("弹幕所在视频的id")
    private int videoId;
    @ApiModelProperty("弹幕所在位置")
    private String place;
}
