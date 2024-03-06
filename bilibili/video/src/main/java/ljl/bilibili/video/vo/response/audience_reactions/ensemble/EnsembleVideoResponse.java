package ljl.bilibili.video.vo.response.audience_reactions.ensemble;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnsembleVideoResponse {
    @ApiModelProperty("视频id")
    private int videoId;
    @ApiModelProperty("视频名字")
    private String name;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("视频播放量")
    private int playCount;
    @ApiModelProperty("所属合集id")
    private int ensembleId;
    @ApiModelProperty("视频封面")
    private String cover;
}
