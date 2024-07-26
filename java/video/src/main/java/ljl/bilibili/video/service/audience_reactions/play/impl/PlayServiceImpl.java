package ljl.bilibili.video.service.audience_reactions.play.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.client.search.SearchClient;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.video.audience_reactions.collect.Collect;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.audience_reactions.play.Play;
import ljl.bilibili.client.pojo.RecommendVideo;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.video.audience_reactions.collect.CollectMapper;
import ljl.bilibili.mapper.video.audience_reactions.like.LikeMapper;
import ljl.bilibili.mapper.video.audience_reactions.play.PlayMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.mapper.VideoServiceMapper;
import ljl.bilibili.video.service.audience_reactions.play.PlayService;
import ljl.bilibili.video.vo.request.audience_reactions.play.DeleteHistoryVideoRequest;
import ljl.bilibili.video.vo.response.audience_reactions.play.CommendVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.DetailVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.FirstPageVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.HistoryVideoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ljl.bilibili.video.constant.Constant.*;
/**
 *播放相关
 */
@Service
@Slf4j
public class PlayServiceImpl implements PlayService {
    @Resource
    PlayMapper playMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    CollectMapper collectMapper;
    @Resource
    SearchClient searchClient;
    @Resource
    LikeMapper likeMapper;
    @Resource
    RedisTemplate objectRedisTemplate;
    @Resource
    VideoServiceMapper videoServiceMapper;
    /**
     *新增播放记录
     */
    @Override
    public Result<Boolean> addPlayRecord(int videoId, int userId){
        LambdaQueryWrapper<Play> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Play::getVideoId,videoId);
        wrapper.eq(Play::getUserId,userId);
        //如果已经增加过了则不新增播放记录
        if(playMapper.selectList(wrapper).size()>0){
            return Result.success(HAS_PLAY);
        }else {
            playMapper.insert(new Play().setVideoId(videoId).setUserId(userId));
            return Result.success(true);
        }
    }
    /**
     *删除历史视频
     */
    @Override
    public Result<Boolean> deleteHistoryVideo(DeleteHistoryVideoRequest deleteHistoryVideoRequest){
        LambdaQueryWrapper<Play> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Play::getUserId,deleteHistoryVideoRequest.getUserId());
        wrapper.eq(Play::getVideoId,deleteHistoryVideoRequest.getVideoId());
        playMapper.delete(wrapper);
        return Result.success(true);
    }
    /**
     *获取视频相关信息
     */
    @Override
    public Result<DetailVideoResponse> getDetailVideo(Integer videoId, Integer userId, String collectGroupId) {
        //前端将收藏夹id用，拼接起来从而不用传集合就可以传递收藏夹集合，所以后端需要拆解成集合
        List<Integer> collectGroupIds = Arrays.stream(collectGroupId.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        MPJLambdaWrapper<Video> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(Video::getName, Video::getIntro, Video::getCreateTime, Video::getId,Video::getUrl);
        wrapper.select(VideoData::getDanmakuCount, VideoData::getPlayCount, VideoData::getLikeCount, VideoData::getCollectCount);
        wrapper.leftJoin(VideoData.class, VideoData::getVideoId, Video::getId);
        wrapper.eq(Video::getId, videoId);
        LambdaQueryWrapper<Like> likeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper=new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.in(Collect::getCollectGroupId,collectGroupIds);
        collectLambdaQueryWrapper.eq(Collect::getVideoId,videoId);
        likeLambdaQueryWrapper.eq(Like::getVideoId,videoId);
        likeLambdaQueryWrapper.eq(Like::getUserId,userId);
        //获取视频相关信息和该视频自己是否已点赞与已收藏
        DetailVideoResponse response = videoMapper.selectJoinOne(DetailVideoResponse.class, wrapper);
        Like videoLike = likeMapper.selectOne(likeLambdaQueryWrapper);
        List<Collect> collects=collectMapper.selectList(collectLambdaQueryWrapper);
        if (videoLike != null) {
            response.setIsLiked(true);
        }
        if(collects.size()>0){
            response.setIsCollected(true);
        }
        return Result.data(response);
    }
    /**
     *获取推荐视频，远程调用es的推荐视频查询接口
     */
    @Override
    public Result<List<CommendVideoResponse>> getRecommendVideo(String videoId) {
        List<RecommendVideo> list=searchClient.getRecommendVideo(videoId);
        List<CommendVideoResponse> list1=new ArrayList<>();
        for(RecommendVideo recommendVideo : list){
            list1.add(new CommendVideoResponse(recommendVideo));
        }
        return Result.data(list1);

    }
    /**
     *获取首页视频
     */
    @Override
    public Result<List<FirstPageVideoResponse>> getFirstPageVideoResponse(Integer count) {
        //获取首页视频，并每次跳过前面十条防止刷出重复视频
        List<FirstPageVideoResponse> responses=videoServiceMapper.getFirstPageVideo(count-1);
        return Result.data(responses);
    }
    /**
     *获取历史视频
     */
    @Override
    public Result<List<HistoryVideoResponse>> getHistoryVideo(int userId) {
        return Result.data(videoServiceMapper.getHistoryVideo(userId));
    }
}
