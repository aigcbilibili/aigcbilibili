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
import ljl.bilibili.video.service.audience_reactions.play.PlayService;
import ljl.bilibili.video.vo.request.audience_reactions.play.DeleteHistoryVideoRequest;
import ljl.bilibili.video.vo.response.audience_reactions.play.DetailVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.FirstPageVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.HistoryVideoResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ljl.bilibili.video.constant.Constant.*;

@Service
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
    @Override
    public Result<Boolean> addPlayRecord(int videoId, int userId){
        LambdaQueryWrapper<Play> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Play::getVideoId,videoId);
        wrapper.eq(Play::getUserId,userId);

        if(playMapper.selectList(wrapper).size()>0){
            return Result.success(HAS_PLAY);
        }else {
            playMapper.insert(new Play().setVideoId(videoId).setUserId(userId));
            return Result.success(true);
        }
    }
    @Override
    public Result<Boolean> deleteHistoryVideo(DeleteHistoryVideoRequest deleteHistoryVideoRequest){
        LambdaQueryWrapper<Play> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Play::getUserId,deleteHistoryVideoRequest.getUserId());
        wrapper.eq(Play::getVideoId,deleteHistoryVideoRequest.getVideoId());
        playMapper.delete(wrapper);
        return Result.success(true);
    }
    @Override
    public Result<DetailVideoResponse> getDetailVideo(Integer videoId, Integer userId, String collectGroupId) {
        List<Integer> collectGroupIds = Arrays.stream(collectGroupId.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        MPJLambdaWrapper<Video> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(Video::getName, Video::getIntro, Video::getCreateTime, Video::getId);
        wrapper.select(VideoData::getDanmakuCount, VideoData::getPlayCount, VideoData::getLikeCount, VideoData::getCollectCount);
        wrapper.leftJoin(VideoData.class, VideoData::getVideoId, Video::getId);
        wrapper.eq(Video::getId, videoId);
        LambdaQueryWrapper<Like> likeLambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper=new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.in(Collect::getCollectGroupId,collectGroupIds);
        collectLambdaQueryWrapper.eq(Collect::getVideoId,videoId);
        likeLambdaQueryWrapper.eq(Like::getVideoId,videoId);
        likeLambdaQueryWrapper.eq(Like::getUserId,userId);
        DetailVideoResponse response = videoMapper.selectJoinOne(DetailVideoResponse.class, wrapper);
        Like videoLike = likeMapper.selectOne(likeLambdaQueryWrapper);
        List<Collect> collects=collectMapper.selectList(collectLambdaQueryWrapper);
        response.setIsCollected(false);
        response.setIsLiked(false);
        if (videoLike != null) {
            response.setIsLiked(true);
        }
        if(collects.size()>0){
            response.setIsCollected(true);
        }
        return Result.data(response);
    }
    @Override
    public Result<List<RecommendVideo>> getRecommendVideo(String videoId) {
        List<RecommendVideo> list=searchClient.getRecommendVideo(videoId);
        return Result.data(list);

    }
    @Override
    public Result<List<FirstPageVideoResponse>> getFirstPageVideoResponse(Integer count) {
        MPJLambdaWrapper<Video> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(Video::getName, Video::getLength, Video::getCreateTime, Video::getCover, Video::getUrl);
        wrapper.selectAs(User::getUserName, FirstPageVideoResponse::getAuthorName);
        wrapper.selectAs(Video::getId, FirstPageVideoResponse::getVideoId);
        wrapper.selectAs(User::getId, FirstPageVideoResponse::getAuthorId);
        wrapper.select(VideoData::getPlayCount, VideoData::getDanmakuCount);
        wrapper.leftJoin(User.class, User::getId, Video::getUserId);
        wrapper.leftJoin(VideoData.class, VideoData::getVideoId, Video::getId);
        wrapper.orderByDesc(VideoData::getPlayCount);
        wrapper.last(SQL+" "+ (count-1)+","+LIMIT);
        List<FirstPageVideoResponse> responses = videoMapper.selectJoinList(FirstPageVideoResponse.class, wrapper);
        return Result.data(responses);
    }
    @Override
    public Result<List<HistoryVideoResponse>> getHistoryVideo(int userId) {
        MPJLambdaWrapper<Play> wrapper=new MPJLambdaWrapper<>();
        wrapper.eq(Play::getUserId,userId);
        wrapper.orderByDesc(Play::getCreateTime);
        wrapper.select(Play::getCreateTime);
        wrapper.selectAs(Video::getName, HistoryVideoResponse::getVideoName);
        wrapper.select(Video::getLength,Video::getId,Video::getCover);
        wrapper.selectAs(User::getNickname, HistoryVideoResponse::getAuthorName);
        wrapper.innerJoin(User.class,User::getId,Play::getUserId);
        wrapper.innerJoin(Video.class,Video::getId,Play::getVideoId);
        List<HistoryVideoResponse> responses= playMapper.selectJoinList(HistoryVideoResponse.class,wrapper);
        return Result.data(responses);
    }
}
