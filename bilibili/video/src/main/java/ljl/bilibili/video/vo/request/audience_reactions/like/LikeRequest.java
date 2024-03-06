package ljl.bilibili.video.vo.request.audience_reactions.like;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.client.pojo.LikeNoticeAddOrDelete;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class LikeRequest {

    @ApiModelProperty("发评论的用户id") 
    private Integer userId;
    @ApiModelProperty("评论所在视频的id")
    private Integer videoId;
    @ApiModelProperty("点赞的评论的id")
    private Integer commentId;

    public Like toEntity(){
        Like danmaku=new Like();
        BeanUtils.copyProperties(this,danmaku);
        return danmaku;
    }
    public LikeNoticeAddOrDelete toAddOrDeleteNotice(){
        LikeNoticeAddOrDelete likeNotice=new LikeNoticeAddOrDelete();
        BeanUtils.copyProperties(this,likeNotice);
        likeNotice.setSenderId(userId);
        return likeNotice;
    }

}
