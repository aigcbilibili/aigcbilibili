package ljl.bilibili.notice.vo.response.get_notice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentNoticeResponse {
 @ApiModelProperty("视频封面，放到右侧")
 private String videoCover;
 @ApiModelProperty("回复人的头像")
 private String userCover;
 @ApiModelProperty("昵称")
 private String nickName;
 @ApiModelProperty("别人回复你的内容")
 private String replyContent;
 @ApiModelProperty("别人回复你的评论情况下这就是你的评论内容")
 private String replyToContent;
 @ApiModelProperty("创建时间")
 private LocalDateTime createTime;
 @ApiModelProperty("某条回复的id")
 private int replyId;
 @ApiModelProperty("视频id，方便跳转到视频页面")
 private int videoId;
 @ApiModelProperty("该条回复的用户id")
 private Integer userId;
 @ApiModelProperty("消息状态")
 private Integer status;
}
