package ljl.bilibili.video.service.audience_reactions.like.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.video.audience_reactions.like.LikeMapper;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.like.LikeService;
import ljl.bilibili.video.vo.request.audience_reactions.like.LikeRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
/**
 *点赞
 */
@Service
public class LikeServiceImpl implements LikeService {


    @Resource
    LikeMapper likeMapper;
    @Resource
    SendNoticeClient client;
    /**
     *对视频或评论的点赞
     */
    @Override
    public Result<Boolean> like(LikeRequest likeRequest) {
        likeMapper.insert(likeRequest.toEntity());
        LambdaQueryWrapper<Video> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Video::getId,likeRequest.getVideoId());
        CompletableFuture<Void> completableFuture=CompletableFuture.runAsync(()->{
            LambdaQueryWrapper<VideoData> videoDataLambdaQueryWrapper=new LambdaQueryWrapper<>();
            videoDataLambdaQueryWrapper.eq(VideoData::getId,likeRequest.getVideoId());
            //根据type值在点赞消息消费者中做出新增或删除操作
            client.sendLikeNotice(likeRequest.toAddOrDeleteNotice().setType(0));
        });
        return Result.success(true);
    }
    /**
     *撤销点赞
     */
    @Override
    public Result<Boolean> recallLike(LikeRequest likeRequest) {

        LambdaQueryWrapper<Like> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, likeRequest.getUserId());
        wrapper.eq(Like::getVideoId, likeRequest.getVideoId());
        //根据评论id是否为空决定删除评论时的删除细节，因为一个人可以同时在一个视频下发出对视频的评论和对评论的评论，因此需要作出区分
        if(likeRequest.getCommentId()!=null){
            wrapper.eq(Like::getCommentId, likeRequest.getCommentId());
        }else {
            wrapper.isNull(Like::getCommentId);
        }
        client.sendLikeNotice(likeRequest.toAddOrDeleteNotice().setType(1));
        likeMapper.delete(wrapper);
        return Result.success(true);
    }
}
