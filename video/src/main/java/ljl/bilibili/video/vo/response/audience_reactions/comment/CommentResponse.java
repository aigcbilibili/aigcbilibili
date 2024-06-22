package ljl.bilibili.video.vo.response.audience_reactions.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CommentResponse {
    @ApiModelProperty("顶级评论")
    public TopCommentResponse topCommentResponse=new TopCommentResponse();
    @ApiModelProperty("后续层评论")
    public List<CommentDetailResponse> commentDetailResponses=new ArrayList<>();
}
