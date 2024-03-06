package ljl.bilibili.video.vo.response.audience_reactions.play;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RelativeTimeSerializer;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class CommendVideoResponse {
    @ApiModelProperty("视频id")
    private String videoId;
    @ApiModelProperty("作者名")
    private String authorName;
    @JsonSerialize(using = RelativeTimeSerializer.class)
    @ApiModelProperty("播放量")
    private int playCount;
    @ApiModelProperty("弹幕量")
    private int danmakuCount;
    @ApiModelProperty("视频的封面url")
    private String url;
    @ApiModelProperty("视频时长(两分半)")
    private String length;
    @ApiModelProperty("作者id")
    private int userId;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
