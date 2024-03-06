package ljl.bilibili.video.vo.response.audience_reactions.comment;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    @ApiModelProperty("评论的id")
    private int id;
    @ApiModelProperty("评论内容")
    private String content;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("顶层评论id")
    private int topId;
    @ApiModelProperty("点赞量")
    private int likeCount;
    @ApiModelProperty("是否已点赞该评论")
    private Boolean isLiked;
    @ApiModelProperty("评论所属用户的id")
    private int senderId;
    @ApiModelProperty("评论所属用户的昵称")
    private String senderName;
    @ApiModelProperty("评论所属用户的头像")
    private String senderCoverUrl;
    @ApiModelProperty("评论回复人的头像")
    private String receiverCoverUrl;
    @ApiModelProperty("评论所回复的评论的id")
    private int receiverId;
    @ApiModelProperty("评论所回复人的id")
    private int receiverUserId;
    @ApiModelProperty("评论所回复人的昵称")
    private String receiverName;


}
