package ljl.bilibili.notice.service.get_notice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.notice.comment.CommentNotice;
import ljl.bilibili.entity.notice.dynamic.Dynamic;
import ljl.bilibili.entity.notice.dynamic.DynamicToUser;
import ljl.bilibili.entity.notice.like.LikeNotice;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.video.audience_reactions.comment.Comment;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.mapper.chat.ChatMapper;
import ljl.bilibili.mapper.notice.dynamic.DynamicToUserMapper;
import ljl.bilibili.mapper.video.audience_reactions.comment.CommentMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.notice.mapper.NoticeServiceMapper;
import ljl.bilibili.notice.service.get_notice.GetNoticeService;
import ljl.bilibili.util.Result;
import ljl.bilibili.notice.vo.response.get_notice.DynamicVideoResponse;
import ljl.bilibili.notice.vo.response.get_notice.LikeNoticeResponse;
import ljl.bilibili.notice.vo.response.get_notice.CommentNoticeResponse;
import ljl.bilibili.notice.vo.response.get_notice.UnReadNoticeCountResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
 *获取消息通知
 */
@Service
public class GetNoticeServiceImpl implements GetNoticeService {
    @Resource
    DynamicToUserMapper dynamicToUserMapper;
    @Resource
    ChatMapper chatMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    NoticeServiceMapper noticeServiceMapper;
    /**
     *获取未读点赞、评论、私聊、动态视频数和未读消息总数
     */
    @Override
    public Result<UnReadNoticeCountResponse> getNoticeCount(Integer userId) {
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        MPJLambdaWrapper<Video> videoLikeLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Comment> commentLikeLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Video> videoLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Comment> commentLamdaQueryWrapper=new MPJLambdaWrapper<>();
        LambdaQueryWrapper<DynamicToUser> dynamicToUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getReceiverId, userId);
        videoLikeLambdaQueryWrapper.eq(Video::getUserId, userId);
        videoLikeLambdaQueryWrapper.leftJoin(LikeNotice.class,LikeNotice::getVideoId,Video::getId);
        videoLikeLambdaQueryWrapper.eq(LikeNotice::getStatus,0);
        videoLikeLambdaQueryWrapper.isNull(LikeNotice::getCommentId);
        //获取未读对视频的点赞数
        long videoLikeCount= videoMapper.selectJoinCount(videoLikeLambdaQueryWrapper);
        commentLikeLambdaQueryWrapper.eq(Comment::getUserId,userId);
        commentLikeLambdaQueryWrapper.eq(LikeNotice::getStatus,0);
        commentLikeLambdaQueryWrapper.leftJoin(LikeNotice.class,LikeNotice::getCommentId,Comment::getId);
        //获取未读对评论的点赞数
        long commentLikeCount=commentMapper.selectJoinCount(commentLikeLambdaQueryWrapper);
        commentLikeLambdaQueryWrapper.eq(Like::getUserId, userId);
        dynamicToUserLambdaQueryWrapper.eq(DynamicToUser::getUserId, userId);
        dynamicToUserLambdaQueryWrapper.eq(DynamicToUser::getStatus,0);
        //获取未读私聊消息数
        long chatCount = chatMapper.selectCount(chatLambdaQueryWrapper);
        //计算总的点赞未读点赞数
        long likeCount = videoLikeCount+commentLikeCount;
        //获取未读动态视频数
        long dynamicVideoCount = dynamicToUserMapper.selectCount(dynamicToUserLambdaQueryWrapper);
        videoLambdaQueryWrapper.eq(Video::getUserId,userId);
        videoLambdaQueryWrapper.leftJoin(CommentNotice.class,CommentNotice::getVideoId,Video::getId);
        videoLambdaQueryWrapper.isNull(CommentNotice::getReceiverId);
        videoLambdaQueryWrapper.eq(CommentNotice::getStatus,0);
        //获取未读对视频的评论数
        long videoCommentCount=videoMapper.selectJoinCount(videoLambdaQueryWrapper);
        commentLamdaQueryWrapper.eq(Comment::getUserId,userId);
        commentLamdaQueryWrapper.leftJoin(CommentNotice.class,CommentNotice::getReceiverId,Comment::getId);
        commentLamdaQueryWrapper.eq(CommentNotice::getStatus,0);
        //获取未读对评论的评论数
        long commentCommentCount=commentMapper.selectJoinCount(commentLamdaQueryWrapper);
        //计算未读的评论总数
        long commentCount=videoCommentCount+commentCommentCount;
        //计算未读消息总数
        long totalCount = chatCount + likeCount + commentCount;
        return Result.data(new UnReadNoticeCountResponse().setChatCount(chatCount).setLikeCount(likeCount)
                .setCommentCount(commentCount).setDynamicVideoCount(dynamicVideoCount).setTotalCount(totalCount));
    }
    /**
     *获取未读点赞消息详情
     */
    @Override
    public Result<List<LikeNoticeResponse>> getLikeNotice(Integer userId) {
        List<LikeNoticeResponse> videoLikeNoticeResponse=noticeServiceMapper.getVideoLikeNotice(userId);
        List<LikeNoticeResponse> commentLikeNoticeResponse=noticeServiceMapper.getCommentLikeNotice(userId);
        videoLikeNoticeResponse.addAll(commentLikeNoticeResponse);
        return Result.data(videoLikeNoticeResponse);
    }
    /**
     *获取未读评论消息详情
     */
    @Override
    public Result<List<CommentNoticeResponse>> getCommentNotice(Integer userId) {
        List<CommentNoticeResponse> videoCommentNoticeResponse=noticeServiceMapper.getCommentToVideoNotice(userId);
        List<CommentNoticeResponse> commentToCommentNoticeResponse=noticeServiceMapper.getCommentToCommentNotice(userId);
        videoCommentNoticeResponse.addAll(commentToCommentNoticeResponse);
        return Result.data(videoCommentNoticeResponse);
    }
    /**
     *获取未读动态视频消息详情
     */
    @Override
    public Result<List<DynamicVideoResponse>> getDynamicVideo(Integer userId) {
        return Result.data(noticeServiceMapper.getDynamicVideoNotice(userId));
    }


}
