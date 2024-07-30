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
    @Override
    public Result<UnReadNoticeCountResponse> getNoticeCount(Integer userId) {
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        MPJLambdaWrapper<Video> videoLikeLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Comment> commentLikeLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Video> videoLambdaQueryWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Comment> commentLamdaQueryWrapper=new MPJLambdaWrapper<>();
        LambdaQueryWrapper<DynamicToUser> dynamicToUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getReceiverId, userId);
        chatLambdaQueryWrapper.eq(Chat::getStatus,0);
        videoLikeLambdaQueryWrapper.eq(Video::getUserId, userId);
        videoLikeLambdaQueryWrapper.leftJoin(LikeNotice.class,LikeNotice::getVideoId,Video::getId);
        videoLikeLambdaQueryWrapper.eq(LikeNotice::getStatus,0);
        videoLikeLambdaQueryWrapper.isNull(LikeNotice::getCommentId);
        long videoLikeCount= videoMapper.selectJoinCount(videoLikeLambdaQueryWrapper);
        commentLikeLambdaQueryWrapper.eq(Comment::getUserId,userId);
        commentLikeLambdaQueryWrapper.eq(LikeNotice::getStatus,0);
        commentLikeLambdaQueryWrapper.leftJoin(LikeNotice.class,LikeNotice::getCommentId,Comment::getId);
        long commentLikeCount=commentMapper.selectJoinCount(commentLikeLambdaQueryWrapper);
        commentLikeLambdaQueryWrapper.eq(Like::getUserId, userId);
        dynamicToUserLambdaQueryWrapper.eq(DynamicToUser::getUserId, userId);
        dynamicToUserLambdaQueryWrapper.eq(DynamicToUser::getStatus,0);
        long chatCount = chatMapper.selectCount(chatLambdaQueryWrapper);
        long likeCount = videoLikeCount+commentLikeCount;
        long dynamicVideoCount = dynamicToUserMapper.selectCount(dynamicToUserLambdaQueryWrapper);
        videoLambdaQueryWrapper.eq(Video::getUserId,userId);
        videoLambdaQueryWrapper.leftJoin(CommentNotice.class,CommentNotice::getVideoId,Video::getId);
        videoLambdaQueryWrapper.isNull(CommentNotice::getReceiverId);
        videoLambdaQueryWrapper.eq(CommentNotice::getStatus,0);
        long videoCommentCount=videoMapper.selectJoinCount(videoLambdaQueryWrapper);
        commentLamdaQueryWrapper.eq(Comment::getUserId,userId);
        commentLamdaQueryWrapper.leftJoin(CommentNotice.class,CommentNotice::getReceiverId,Comment::getId);
        commentLamdaQueryWrapper.eq(CommentNotice::getStatus,0);
        long commentCommentCount=commentMapper.selectJoinCount(commentLamdaQueryWrapper);
        long commentCount=videoCommentCount+commentCommentCount;
        long totalCount = chatCount + likeCount + commentCount;
        return Result.data(new UnReadNoticeCountResponse().setChatCount(chatCount).setLikeCount(likeCount)
                .setCommentCount(commentCount).setDynamicVideoCount(dynamicVideoCount).setTotalCount(totalCount));
    }
    @Override
    public Result<List<LikeNoticeResponse>> getLikeNotice(Integer userId) {
        List<LikeNoticeResponse> videoLikeNoticeResponse=noticeServiceMapper.getVideoLikeNotice(userId);
        List<LikeNoticeResponse> commentLikeNoticeResponse=noticeServiceMapper.getCommentLikeNotice(userId);
        videoLikeNoticeResponse.addAll(commentLikeNoticeResponse);
        return Result.data(videoLikeNoticeResponse);
    }
    @Override
    public Result<List<CommentNoticeResponse>> getCommentNotice(Integer userId) {
        List<CommentNoticeResponse> videoCommentNoticeResponse=noticeServiceMapper.getCommentToVideoNotice(userId);
        List<CommentNoticeResponse> commentToCommentNoticeResponse=noticeServiceMapper.getCommentToCommentNotice(userId);
        videoCommentNoticeResponse.addAll(commentToCommentNoticeResponse);
        return Result.data(videoCommentNoticeResponse);
    }
    @Override
    public Result<List<DynamicVideoResponse>> getDynamicVideo(Integer userId) {
        return Result.data(noticeServiceMapper.getDynamicVideoNotice(userId));
    }


}
