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
        LambdaQueryWrapper<VideoData> commentCountWrapper = new LambdaQueryWrapper<>();
        commentCountWrapper.eq(VideoData::getVideoId,videoId);
        List<CommentDetailResponse> commentToVideoResponses = videoServiceMapper.getCommentToVideo(videoId);
        List<CommentDetailResponse> commentToCommentResponses= videoServiceMapper.getCommentToComment(videoId);
        commentToVideoResponses.addAll(commentToCommentResponses);
        if (commentToVideoResponses.size() > 0) {
            Set<Integer> commentIds = commentToVideoResponses.stream()
                    .map(CommentDetailResponse::getId)
                    .collect(Collectors.toSet());
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
        if(type==1){
            commentToVideoResponses.sort(Comparator.comparingInt(CommentDetailResponse::getLikeCount).reversed());
//            Collections.sort(responses, (p1, p2) -> p2.getLikeCount() - p1.getLikeCount());
        }
        long count= videoDataMapper.selectOne(commentCountWrapper).getCommentCount();
        Map<Integer, CommentResponse> responseMap=new HashMap<>();
        for(CommentDetailResponse response : commentToVideoResponses){
            if(response.getTopId()==null){
                CommentResponse commentResponse=new CommentResponse();
                commentResponse.setTopCommentResponse(new TopCommentResponse().setCreateTime(response.getCreateTime()).setId(response.getId()).setContent(response.getContent()).setSenderId(response.getSenderId()).setLikeCount(response.getLikeCount()).setIsLiked(response.getIsLiked()).setSenderName(response.getSenderName()).setSenderCoverUrl(response.getSenderCoverUrl()));
                responseMap.put(response.getId(),commentResponse);
            }
        }
        for(CommentDetailResponse response : commentToVideoResponses){
            if(response.getTopId()!=null){
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
