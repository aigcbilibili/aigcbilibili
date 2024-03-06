package ljl.bilibili.notice.vo.response.get_notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UnReadNoticeCountResponse {
    @ApiModelProperty("未读私聊数")
    private long chatCount;
    @ApiModelProperty("未读点赞数")
    private long likeCount;
    @ApiModelProperty("未读动态视频数")
    private long dynamicVideoCount;
    @ApiModelProperty("未读评论数")
    private long commentCount;
    @ApiModelProperty("未读回复、点赞、评论总数")
    private long totalCount;
}
