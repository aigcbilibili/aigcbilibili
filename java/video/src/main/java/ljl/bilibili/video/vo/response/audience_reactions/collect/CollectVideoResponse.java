package ljl.bilibili.video.vo.response.audience_reactions.collect;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectVideoResponse {
    @ApiModelProperty("视频的名字")
    private String videoName;
    @ApiModelProperty("创建时间")
    @JsonSerialize(using = RemoveTSerializer.class)
    private LocalDateTime createTime;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("封面的url")
    private String videoCover;
    @ApiModelProperty("视频id，方便跳转到视频详情页")
    private int videoId;
    @ApiModelProperty("作者名")
    private String authorName;
    @ApiModelProperty("所属收藏夹的id,动态、历史这个值为null")
    private int collectGroupId;
}
