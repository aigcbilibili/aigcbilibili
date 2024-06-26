package ljl.bilibili.notice.vo.response.get_notice;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RelativeTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DynamicVideoResponse {
    @ApiModelProperty("视频的名字")
    private String videoName;
    @JsonSerialize(using = RelativeTimeSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("封面的url")
    private String videoCover;
    @ApiModelProperty("作者头像")
    private String authorCover;
    @ApiModelProperty("视频id，方便跳转到视频详情页")
    private int videoId;
    @ApiModelProperty("作者名")
    private String authorName;
    @ApiModelProperty("是否已读")
    private int status;
    @ApiModelProperty("作者id")
    private int authorId;
    @ApiModelProperty("动态id")
    private int id;
}
