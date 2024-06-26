package ljl.bilibili.notice.vo.response.get_notice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("对评论的点赞没有videoCoverUrl,对视频的点赞没有commentId、content、replyName")
@Data
public class LikeNoticeResponse {
    @ApiModelProperty("方便跳转到个人主页")
    private Integer userId;
    @ApiModelProperty("点赞评论的人的昵称")
    private String nickName;
    @ApiModelProperty("该用户头像")
    private String userCover;
    @ApiModelProperty("视频封面，若点赞的是评论则右侧放评论内容，否则右侧放视频封面")
    private String videoCover;
    @ApiModelProperty("视频id，方便跳转到视频详情页")
    private Integer videoId;
    @ApiModelProperty("评论id，若点赞的是评论则可以根据这个id跳转到该评论在评论所属视频的位置")
    private Integer commentId;
    @ApiModelProperty("评论内容，若点赞的是评论则右侧放评论内容，否则右侧放视频封面")
    private String content;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("是否已读")
    private Integer status;
}
