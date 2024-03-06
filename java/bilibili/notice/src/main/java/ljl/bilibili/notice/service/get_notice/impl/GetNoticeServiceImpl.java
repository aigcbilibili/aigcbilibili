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
        MPJLambdaWrapper<Video> videoLikeNoticeMPJLambdaWrapper=new MPJLambdaWrapper<>();
        MPJLambdaWrapper<Comment> commentLikeNoticeMPJLambdaWrapper = new MPJLambdaWrapper<>();
        videoLikeNoticeMPJLambdaWrapper.innerJoin(LikeNotice.class,LikeNotice::getVideoId,Video::getId);
        videoLikeNoticeMPJLambdaWrapper.eq(Video::getUserId,userId);
        videoLikeNoticeMPJLambdaWrapper.isNull(LikeNotice::getCommentId);
        videoLikeNoticeMPJLambdaWrapper.orderByDesc(LikeNotice::getCreateTime);
        videoLikeNoticeMPJLambdaWrapper.innerJoin(User.class,User::getId,LikeNotice::getSenderId);
        videoLikeNoticeMPJLambdaWrapper.selectAs(User::getNickname,LikeNoticeResponse::getNickName);
        videoLikeNoticeMPJLambdaWrapper.selectAs(User::getId,LikeNoticeResponse::getUserId);
        videoLikeNoticeMPJLambdaWrapper.selectAs(User::getCover,LikeNoticeResponse::getUserCover);
        videoLikeNoticeMPJLambdaWrapper.selectAs(Video::getCover,LikeNoticeResponse::getVideoCover);
        videoLikeNoticeMPJLambdaWrapper.selectAs(Video::getId,LikeNoticeResponse::getVideoId);
        videoLikeNoticeMPJLambdaWrapper.selectAs(LikeNotice::getCreateTime,LikeNoticeResponse::getCreateTime);
        videoLikeNoticeMPJLambdaWrapper.selectAs(LikeNotice::getStatus,LikeNoticeResponse::getStatus);
        List<LikeNoticeResponse> videoLikeNoticeResponse=videoMapper.selectJoinList(LikeNoticeResponse.class,videoLikeNoticeMPJLambdaWrapper);
        commentLikeNoticeMPJLambdaWrapper.eq(Comment::getUserId,userId);
        commentLikeNoticeMPJLambdaWrapper.innerJoin(LikeNotice.class, LikeNotice::getCommentId,Comment::getId);
        commentLikeNoticeMPJLambdaWrapper.innerJoin(Comment.class,"comment",Comment::getId,LikeNotice::getSenderId);
        commentLikeNoticeMPJLambdaWrapper.innerJoin(User.class,User::getId,"comment",Comment::getUserId);
        commentLikeNoticeMPJLambdaWrapper.selectAs(User::getNickname,LikeNoticeResponse::getNickName);
        commentLikeNoticeMPJLambdaWrapper.selectAs(User::getId,LikeNoticeResponse::getUserId);
        commentLikeNoticeMPJLambdaWrapper.selectAs(User::getCover,LikeNoticeResponse::getUserCover);
        commentLikeNoticeMPJLambdaWrapper.selectAs(Comment::getVideoId,LikeNoticeResponse::getVideoId);
        commentLikeNoticeMPJLambdaWrapper.selectAs(LikeNotice::getCommentId,LikeNoticeResponse::getCommentId);
        commentLikeNoticeMPJLambdaWrapper.selectAs( LikeNotice::getStatus,LikeNoticeResponse::getStatus);
        commentLikeNoticeMPJLambdaWrapper.selectAs(Comment::getContent,LikeNoticeResponse::getContent);
        List<LikeNoticeResponse> commentLikeNoticeResponse=commentMapper.selectJoinList(LikeNoticeResponse.class,commentLikeNoticeMPJLambdaWrapper);
        videoLikeNoticeResponse.addAll(commentLikeNoticeResponse);
        return Result.data(videoLikeNoticeResponse);
    }
    @Override
    public Result<List<CommentNoticeResponse>> getCommentNotice(Integer userId) {
        MPJLambdaWrapper<Video> videoCommentNoticeWrapper=new MPJLambdaWrapper<>();
        videoCommentNoticeWrapper.eq(Video::getUserId,userId);
        videoCommentNoticeWrapper.isNull(Comment::getParentId);
        videoCommentNoticeWrapper.orderByDesc(CommentNotice::getCreateTime);
        videoCommentNoticeWrapper.innerJoin(Comment.class,Comment::getVideoId,Video::getId);
        videoCommentNoticeWrapper.innerJoin(User.class,User::getId,Comment::getUserId);
        videoCommentNoticeWrapper.innerJoin(CommentNotice.class,CommentNotice::getSenderId,Comment::getId);
        videoCommentNoticeWrapper.select(CommentNotice::getStatus);
        videoCommentNoticeWrapper.selectAs(Video::getCover,CommentNoticeResponse::getVideoCover);
        videoCommentNoticeWrapper.selectAs(Video::getId,CommentNoticeResponse::getVideoId);
        videoCommentNoticeWrapper.selectAs(User::getNickname,CommentNoticeResponse::getNickName);
        videoCommentNoticeWrapper.selectAs(User::getCover,CommentNoticeResponse::getUserCover);
        videoCommentNoticeWrapper.selectAs(Comment::getContent,CommentNoticeResponse::getReplyContent);
        videoCommentNoticeWrapper.selectAs(Comment::getId,CommentNoticeResponse::getReplyId);
        videoCommentNoticeWrapper.selectAs(Comment::getCreateTime,CommentNoticeResponse::getCreateTime);
        List<CommentNoticeResponse> videoCommentNoticeResponse=videoMapper.selectJoinList(CommentNoticeResponse.class,videoCommentNoticeWrapper);
        MPJLambdaWrapper<Comment> commentToCommentNoticeWrapper=new MPJLambdaWrapper<>();
        commentToCommentNoticeWrapper.eq(Comment::getUserId,userId);
        commentToCommentNoticeWrapper.select(CommentNotice::getStatus);
        commentToCommentNoticeWrapper.innerJoin(CommentNotice.class,CommentNotice::getReceiverId,Comment::getId);
        commentToCommentNoticeWrapper.innerJoin(Comment.class,"comment",Comment::getId,CommentNotice::getSenderId);
        commentToCommentNoticeWrapper.innerJoin(User.class,User::getId,"comment",Comment::getUserId);
        commentToCommentNoticeWrapper.selectAs(User::getCover,CommentNoticeResponse::getUserCover);
        commentToCommentNoticeWrapper.selectAs(User::getId,CommentNoticeResponse::getUserId);
        videoCommentNoticeWrapper.selectAs(User::getNickname,CommentNoticeResponse::getNickName);
        commentToCommentNoticeWrapper.selectAs(Comment::getContent,CommentNoticeResponse::getReplyToContent);
        commentToCommentNoticeWrapper.selectAs("comment",Comment::getContent,CommentNoticeResponse::getReplyContent);
        videoCommentNoticeWrapper.selectAs(Comment::getVideoId,CommentNoticeResponse::getVideoId);
        videoCommentNoticeWrapper.selectAs(CommentNotice::getCreateTime,CommentNoticeResponse::getCreateTime);
        commentToCommentNoticeWrapper.selectAs(CommentNotice::getSenderId,CommentNoticeResponse::getReplyId);
        List<CommentNoticeResponse> commentToCommentNoticeResponse=commentMapper.selectJoinList(CommentNoticeResponse.class,commentToCommentNoticeWrapper);
        videoCommentNoticeResponse.addAll(commentToCommentNoticeResponse);
        return Result.data(videoCommentNoticeResponse);
    }
    @Override
    public Result<List<DynamicVideoResponse>> getDynamicVideo(Integer userId) {
        MPJLambdaWrapper<DynamicToUser> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(DynamicToUser::getUserId, userId);
        wrapper.orderByDesc(Dynamic::getCreateTime);
        wrapper.innerJoin(Dynamic.class, Dynamic::getId, DynamicToUser::getDynamicId);
        wrapper.innerJoin(User.class, User::getId, DynamicToUser::getUserId);
        wrapper.select(DynamicToUser::getId, DynamicToUser::getStatus);
        wrapper.select(Dynamic::getVideoId, Dynamic::getVideoCover, Dynamic::getVideoName, Dynamic::getCreateTime);
        wrapper.selectAs(User::getCover, DynamicVideoResponse::getAuthorCover);
        wrapper.selectAs(User::getNickname, DynamicVideoResponse::getAuthorName);
        wrapper.selectAs(User::getId, DynamicVideoResponse::getAuthorId);
        return Result.data(dynamicToUserMapper.selectJoinList(DynamicVideoResponse.class, wrapper));
    }


}
