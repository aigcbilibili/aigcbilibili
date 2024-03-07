package ljl.bilibili.user_center.vo.response.ensemble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoInEnsembleResponse {
    @ApiModelProperty("视频id")
    private Integer videoId;
    @ApiModelProperty("视频名")
    private String videoName;
    @ApiModelProperty("视频时长")
    private String length;
    @ApiModelProperty("合集名称")
    private String ensembleName;
    @ApiModelProperty("合集id，方便跳转")
    private Integer ensembleId;
}
