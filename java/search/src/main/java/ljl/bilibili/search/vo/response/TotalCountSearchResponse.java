package ljl.bilibili.search.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class TotalCountSearchResponse {
    @ApiModelProperty("查出来视频的总页数")
    private long totalVideoPage;
    @ApiModelProperty("查出来视频的总量")
    private long totalVideoNum;
    @ApiModelProperty("查出来用户的总页数")
    private long totalUserPage;
    @ApiModelProperty("查出来用户的总量")
    private long totalUserNum;
}
