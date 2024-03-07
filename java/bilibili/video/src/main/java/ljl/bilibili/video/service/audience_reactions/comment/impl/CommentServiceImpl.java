package ljl.bilibili.video.service.audience_reactions.comment.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.user_center.user_relationships.IdCount;
import ljl.bilibili.entity.video.audience_reactions.comment.Comment;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.video.audience_reactions.like.LikeMapper;
import ljl.bilibili.mapper.video.audience_reactions.comment.CommentMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoDataMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.comment.CommentService;
import ljl.bilibili.video.vo.request.audience_reactions.comment.CommentRequest;
import ljl.bilibili.video.vo.response.audience_reactions.comment.CommentResponse;
import ljl.bilibili.video.vo.response.audience_reactions.comment.VideoCommentResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    CommentMapper commentMapper;
    @Resource
    LikeMapper likeMapper;
    @Resource
    SendNoticeClient client;
    @Resource
    VideoMapper videoMapper;
    @Resource
    VideoDataMapper videoDataMapper;
@Override
    public Result<Boolean> commentToComment(CommentRequest commentRequest) {
        Comment comment = commentRequest.toEntity();
        commentMapper.insert(comment);
        LambdaQueryWrapper<Video> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Video::getId,comment.getVideoId());
        if(!videoMapper.selectOne(wrapper).getUserId().equals(commentRequest.getUserId())){
            client.sendCommentNotice(commentRequest.toNotice().setSenderId(comment.getId()));
        }
        return Result.success(true);
    }
    @Override
    public Result<VideoCommentResponse> getComment(Integer videoId, Integer userId, Integer type) {
        MPJLambdaWrapper<Comment> wrapper = new MPJLambdaWrapper<>();
        LambdaQueryWrapper<VideoData> commentCountWrapper = new LambdaQueryWrapper<>();
        commentCountWrapper.eq(VideoData::getVideoId,videoId);
        wrapper.eq(Comment::getVideoId, videoId);
        wrapper.leftJoin(Comment.class, "parentComment", Comment::getParentId, Comment::getId);
        wrapper.leftJoin(User.class, "parentUser", User::getId, "parentComment", Comment::getUserId);
        wrapper.leftJoin(User.class, "u", User::getId, "t", Comment::getUserId);
        wrapper.selectAs("t", Comment::getId, CommentResponse::getId);
        wrapper.orderByDesc(Comment::getCreateTime);
        wrapper.selectAs("t", Comment::getContent, CommentResponse::getContent);
        wrapper.selectAs("t", Comment::getCreateTime, CommentResponse::getCreateTime);
        wrapper.selectAs("t", Comment::getTopId, CommentResponse::getTopId);
        wrapper.selectAs("u", User::getId, CommentResponse::getSenderId);
        wrapper.selectAs("u", User::getNickname, CommentResponse::getSenderName);
        wrapper.selectAs("u", User::getCover, CommentResponse::getSenderCoverUrl);
        wrapper.selectAs("parentUser", User::getCover, CommentResponse::getReceiverCoverUrl);
        wrapper.selectAs("parentUser", User::getId, CommentResponse::getReceiverUserId);
        wrapper.selectAs("parentUser", User::getNickname, CommentResponse::getReceiverName);
        List<CommentResponse> responses = commentMapper.selectJoinList(CommentResponse.class, wrapper);
        if (responses.size() > 0) {
            Set<Integer> commentIds = responses.stream()
                    .map(CommentResponse::getId)
                    .collect(Collectors.toSet());
            LambdaQueryWrapper<Like> likeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            likeLambdaQueryWrapper.in(Like::getCommentId, commentIds);
            likeLambdaQueryWrapper.eq(Like::getVideoId, videoId);
            likeLambdaQueryWrapper.eq(Like::getUserId, userId);
            List<Like> likes = likeMapper.selectList(likeLambdaQueryWrapper);
            Map<Integer, Boolean> likeMap = likes.stream()
                    .collect(Collectors.toMap(Like::getCommentId, like -> true, (existing, replacement) -> existing));
            responses.forEach(response -> {
                response.setIsLiked(likeMap.getOrDefault(response.getId(), false));
            });
           List<IdCount> ids= commentMapper.getCommentCount(commentIds);
           Map<Integer, Integer> idCount=new HashMap<>(10);
           for(IdCount id : ids){
               idCount.put(id.getId(),id.getCount());
           }
            responses.forEach(response -> {
                response.setLikeCount(idCount.getOrDefault(response.getId(), 0));
            });
        }
        if(type==1){
            responses.sort(Comparator.comparingInt(CommentResponse::getLikeCount).reversed());
//            Collections.sort(responses, (p1, p2) -> p2.getLikeCount() - p1.getLikeCount());
        }
        return Result.data(new VideoCommentResponse().setCommentResponseList(responses).setCommentCount(        videoDataMapper.selectOne(commentCountWrapper).getCommentCount()
));
    }
}
