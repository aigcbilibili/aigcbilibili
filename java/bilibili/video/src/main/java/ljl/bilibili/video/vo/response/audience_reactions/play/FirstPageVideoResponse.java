package ljl.bilibili.video.vo.response.audience_reactions.play;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FirstPageVideoResponse {
    @ApiModelProperty("封面url")
    private String cover;
    @ApiModelProperty("视频存储在minio中的访问路径")
    private String url;
    @ApiModelProperty("作者名")
    private String authorName;
    @ApiModelProperty("作者id")
    private int authorId;
    @ApiModelProperty("视频名")
    private String name;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("播放量")
    private int playCount;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("弹幕量")
    private int danmakuCount;
@ApiModelProperty("视频id")
    private int videoId;

}
