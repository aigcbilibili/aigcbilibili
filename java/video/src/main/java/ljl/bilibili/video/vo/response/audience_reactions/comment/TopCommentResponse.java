package ljl.bilibili.video.vo.response.audience_reactions.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
public class TopCommentResponse {
    @ApiModelProperty("评论内容")
    private String content;
    @ApiModelProperty("评论的id")
    private Integer id;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("点赞量")
    private Integer likeCount;
    @ApiModelProperty("是否已点赞该评论")
    private Boolean isLiked;
    @ApiModelProperty("评论所属用户的id")
    private int senderId;
    @ApiModelProperty("评论所属用户的昵称")
    private String senderName;
    @ApiModelProperty("评论所属用户的头像")
    private String senderCoverUrl;
}
