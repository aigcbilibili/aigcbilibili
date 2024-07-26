package ljl.bilibili.video.service.audience_reactions.comment.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.user_center.user_relationships.IdCount;
import ljl.bilibili.entity.video.audience_reactions.comment.Comment;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.video.mapper.VideoServiceMapper;
import ljl.bilibili.mapper.video.audience_reactions.like.LikeMapper;
import ljl.bilibili.mapper.video.audience_reactions.comment.CommentMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoDataMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.comment.CommentService;
import ljl.bilibili.video.vo.request.audience_reactions.comment.CommentRequest;
import ljl.bilibili.video.vo.response.audience_reactions.comment.CommentDetailResponse;
import ljl.bilibili.video.vo.response.audience_reactions.comment.CommentResponse;
import ljl.bilibili.video.vo.response.audience_reactions.comment.TopCommentResponse;
import ljl.bilibili.video.vo.response.audience_reactions.comment.VideoCommentResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
 *评论
 */
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
    @Resource
    VideoServiceMapper videoServiceMapper;
    /**
     *发送评论
     */
    @Override
    public Result<Boolean> commentToComment(CommentRequest commentRequest) {
        Comment comment = commentRequest.toEntity();
        commentMapper.insert(comment);
        LambdaQueryWrapper<Video> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Video::getId,comment.getVideoId());
        //只有该评论不是自己对自己发送的才会生成评论通知
        if(!videoMapper.selectOne(wrapper).getUserId().equals(commentRequest.getUserId())){
            client.sendCommentNotice(commentRequest.toNotice().setSenderId(comment.getId()));
        }
        return Result.success(true);
    }
    /**
     *获取评论
     */
    @Override
    public Result<VideoCommentResponse> getComment(Integer videoId, Integer userId, Integer type) {
        LambdaQueryWrapper<VideoData> commentCountWrapper = new LambdaQueryWrapper<>();
        commentCountWrapper.eq(VideoData::getVideoId,videoId);
        //查询对视频的评论和对评论的评论并合并到一起
        List<CommentDetailResponse> commentToVideoResponses = videoServiceMapper.getCommentToVideo(videoId);
        List<CommentDetailResponse> commentToCommentResponses= videoServiceMapper.getCommentToComment(videoId);
        commentToVideoResponses.addAll(commentToCommentResponses);
        //如果评论数大于0
        if (commentToVideoResponses.size() > 0) {
            Set<Integer> commentIds = commentToVideoResponses.stream()
                    .map(CommentDetailResponse::getId)
                    .collect(Collectors.toSet());
            //查询点赞记录并封装到对应的评论
            LambdaQueryWrapper<Like> likeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            likeLambdaQueryWrapper.in(Like::getCommentId, commentIds);
            likeLambdaQueryWrapper.eq(Like::getVideoId, videoId);
            likeLambdaQueryWrapper.eq(Like::getUserId, userId);
            List<Like> likes = likeMapper.selectList(likeLambdaQueryWrapper);
            Map<Integer, Boolean> likeMap = likes.stream()
                    .collect(Collectors.toMap(Like::getCommentId, like -> true, (existing, replacement) -> existing));
            commentToVideoResponses.forEach(response -> {
                response.setIsLiked(likeMap.getOrDefault(response.getId(), false));
            });
            List<IdCount> ids= commentMapper.getCommentCount(commentIds);
            Map<Integer, Integer> idCount=new HashMap<>(10);
            for(IdCount id : ids){
                idCount.put(id.getId(),id.getCount());
            }
            commentToVideoResponses.forEach(response -> {
                response.setLikeCount(idCount.getOrDefault(response.getId(), 0));
            });
        }
        //排序方式，根据值设置按点赞排序还是按创建时间排序，默认创建时间排序
        if(type==1){
            commentToVideoResponses.sort(Comparator.comparingInt(CommentDetailResponse::getLikeCount).reversed());
//            Collections.sort(responses, (p1, p2) -> p2.getLikeCount() - p1.getLikeCount());
        }
        long count= videoDataMapper.selectOne(commentCountWrapper).getCommentCount();
        Map<Integer, CommentResponse> responseMap=new HashMap<>();
        for(CommentDetailResponse response : commentToVideoResponses){
            //如果是对视频的评论，即topId为null，封装更多内容
            if(response.getTopId()==null){
                CommentResponse commentResponse=new CommentResponse();
                commentResponse.setTopCommentResponse(new TopCommentResponse().setCreateTime(response.getCreateTime()).setId(response.getId()).setContent(response.getContent()).setSenderId(response.getSenderId()).setLikeCount(response.getLikeCount()).setIsLiked(response.getIsLiked()).setSenderName(response.getSenderName()).setSenderCoverUrl(response.getSenderCoverUrl()));
                responseMap.put(response.getId(),commentResponse);
            }
            //如果是对评论的评论
            else{
                CommentResponse commentResponse=responseMap.getOrDefault(response.getTopId(),new CommentResponse());
                commentResponse.getCommentDetailResponses().add(response);
            }
        }
        List<CommentResponse> commentResponseList=new ArrayList<>();
        for(Map.Entry<Integer,CommentResponse> responseEntry : responseMap.entrySet()){
            commentResponseList.add(responseEntry.getValue());
        }
        return Result.data(new VideoCommentResponse().setCommentResponseList(commentResponseList).setCommentCount(count
        ));
    }
}
