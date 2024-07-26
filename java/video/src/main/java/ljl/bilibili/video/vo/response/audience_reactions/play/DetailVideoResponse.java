package ljl.bilibili.video.vo.response.audience_reactions.play;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DetailVideoResponse {
    @ApiModelProperty("视频id")
    private int id;
    @ApiModelProperty("视频名")
    private String name;
    @ApiModelProperty("简介")
    private String intro;
    @ApiModelProperty("播放量")
    private int playCount;
    @ApiModelProperty("点赞量")
    private int likeCount;
    @ApiModelProperty("收藏量")
    private int collectCount;
    @ApiModelProperty("弹幕量")
    private int danmakuCount;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("点赞状态")
    private Boolean isLiked=false;
    @ApiModelProperty("收藏状态")
    private Boolean isCollected=false;
    @ApiModelProperty("视频播放地址")
    private String url;
}
