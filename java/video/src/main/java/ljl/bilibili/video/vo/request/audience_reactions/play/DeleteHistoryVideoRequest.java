package ljl.bilibili.video.vo.request.audience_reactions.play;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteHistoryVideoRequest {
    @ApiModelProperty("删除一条播放记录对应的视频id")
    Integer videoId;
    @ApiModelProperty("该播放记录对应用户id")
    Integer userId;

}
