package ljl.bilibili.user_center.vo.response.self_center;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SelfVideoResponse {
    @ApiModelProperty("视频id")
    private int videoId;
    @ApiModelProperty("视频名字")
    private String name;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("视频播放量")
    private int playCount;
    @ApiModelProperty("视频封面")
    private String cover;
}
