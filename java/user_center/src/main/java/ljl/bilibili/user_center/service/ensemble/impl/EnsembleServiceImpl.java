package ljl.bilibili.user_center.service.ensemble.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.entity.user_center.ensemble.VideoEnsemble;
import ljl.bilibili.entity.video.audience_reactions.ensemble.AddToEnsemble;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.user_center.ensemble.VideoEnsembleMapper;
import ljl.bilibili.mapper.video.audience_reactions.ensemble.AddToEnsembleMapper;
import ljl.bilibili.user_center.service.ensemble.EnsembleService;
import ljl.bilibili.user_center.vo.request.ensemble.AddOrUpdateEnsembleRequest;
import ljl.bilibili.user_center.vo.request.ensemble.AddToEnsembleRequest;
import ljl.bilibili.user_center.vo.request.ensemble.DeleteFromEnsembleRequest;
import ljl.bilibili.user_center.vo.response.ensemble.EnsembleVideoResponse;
import ljl.bilibili.user_center.vo.response.ensemble.AllVideoInEnsembleResponse;
import ljl.bilibili.user_center.vo.response.ensemble.VideoInEnsembleResponse;
import ljl.bilibili.util.Result;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//该类后续会补上对应前端
@Service
public class EnsembleServiceImpl implements EnsembleService {
    @Resource
    VideoEnsembleMapper videoEnsembleMapper;
    @Resource
    AddToEnsembleMapper addToEnsembleMapper;

@Override
    public Result<List<VideoEnsemble>> getEnsembleList(Integer userId) {
        LambdaQueryWrapper<VideoEnsemble> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoEnsemble::getUserId, userId);
        wrapper.orderByDesc(VideoEnsemble::getCreateTime);
        return Result.data(videoEnsembleMapper.selectList(wrapper));
    }
    @Override
    public Result<Boolean> addOrUpdateEnsemble(AddOrUpdateEnsembleRequest addOrUpdateEnsembleRequest) {
        if (addOrUpdateEnsembleRequest.getId() != null) {
            videoEnsembleMapper.updateById(addOrUpdateEnsembleRequest.toEntity());

        } else {
            videoEnsembleMapper.insert(addOrUpdateEnsembleRequest.toEntity());
        }
        return Result.success(true);
    }
    @Override
    public Result<List<EnsembleVideoResponse>> getEnsembleVideo(int userId) {
        MPJLambdaWrapper<VideoEnsemble> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(VideoEnsemble::getUserId, userId);
        wrapper.orderByDesc(AddToEnsemble::getCreateTime);
        wrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
        wrapper.innerJoin(Video.class, Video::getId, AddToEnsemble::getVideoId);
        wrapper.innerJoin(VideoData.class, VideoData::getVideoId, Video::getId);
        wrapper.selectAs(Video::getId, EnsembleVideoResponse::getVideoId);
        wrapper.select(Video::getCreateTime, Video::getCover, Video::getLength, Video::getName);
        wrapper.select(VideoData::getPlayCount);
        wrapper.selectAs(VideoEnsemble::getId, EnsembleVideoResponse::getEnsembleId);
        return Result.data(videoEnsembleMapper.selectJoinList(EnsembleVideoResponse.class, wrapper));
    }
    @Override
    public Result<AllVideoInEnsembleResponse> getAllVideoInEnsemble(Integer userId, Integer videoId) {
        AllVideoInEnsembleResponse allVideoInEnsembleResponse = new AllVideoInEnsembleResponse();
        MPJLambdaWrapper<VideoEnsemble> getEnsembleIdMPJLambdaWrapper = new MPJLambdaWrapper<>();
        getEnsembleIdMPJLambdaWrapper.eq(VideoEnsemble::getUserId, userId);
        getEnsembleIdMPJLambdaWrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
        getEnsembleIdMPJLambdaWrapper.eq(AddToEnsemble::getVideoId, videoId);
        Map<String, Object> map = videoEnsembleMapper.selectJoinMap(getEnsembleIdMPJLambdaWrapper);
      if(map!=null){
          Integer ensembleId = (Integer) map.get("id");
          MPJLambdaWrapper<VideoEnsemble> videoEnsembleMPJLambdaWrapper = new MPJLambdaWrapper<>();
          videoEnsembleMPJLambdaWrapper.eq(VideoEnsemble::getId, ensembleId);
          videoEnsembleMPJLambdaWrapper.orderByDesc(AddToEnsemble::getCreateTime);
          videoEnsembleMPJLambdaWrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
          videoEnsembleMPJLambdaWrapper.innerJoin(Video.class, Video::getId, AddToEnsemble::getVideoId);
          videoEnsembleMPJLambdaWrapper.selectAs(Video::getId, VideoInEnsembleResponse::getVideoId);
          videoEnsembleMPJLambdaWrapper.selectAs(Video::getName, VideoInEnsembleResponse::getVideoName);
          videoEnsembleMPJLambdaWrapper.select(Video::getLength);
          videoEnsembleMPJLambdaWrapper.selectAs(VideoEnsemble::getName, VideoInEnsembleResponse::getEnsembleName);
          videoEnsembleMPJLambdaWrapper.selectAs(VideoEnsemble::getId, VideoInEnsembleResponse::getEnsembleId);
          List<VideoInEnsembleResponse> list = videoEnsembleMapper.selectJoinList(VideoInEnsembleResponse.class, videoEnsembleMPJLambdaWrapper);
          MPJLambdaWrapper<VideoEnsemble> totalVideoCountWrapper = new MPJLambdaWrapper<>();
          totalVideoCountWrapper.eq(VideoEnsemble::getId, ensembleId);
          totalVideoCountWrapper.innerJoin(AddToEnsemble.class, AddToEnsemble::getEnsembleId, VideoEnsemble::getId);
          totalVideoCountWrapper.innerJoin(Video.class, Video::getId, AddToEnsemble::getVideoId);
          allVideoInEnsembleResponse.setTotalVideoCount(videoEnsembleMapper.selectJoinCount(totalVideoCountWrapper));
          List<Integer> ids = new ArrayList<>();
          for (int i = 0; i < list.size(); i++) {
              int id = list.get(i).getVideoId();
              if (id == videoId) {
                  allVideoInEnsembleResponse.setPlaceInTotal(i + 1);
              }
              ids.add(id);
          }
          allVideoInEnsembleResponse.setTotalPlayCount(videoEnsembleMapper.getTotalPlayCount(ids));
          allVideoInEnsembleResponse.setVideoInEnsembleResponses(list);
          return Result.data(allVideoInEnsembleResponse);
      }
      return Result.data(null);
    }
    @Override
    public Result<Boolean> addToEnsemble(AddToEnsembleRequest addToEnsembleRequest){
    LambdaQueryWrapper<AddToEnsemble> wrapper=new LambdaQueryWrapper<>();
    wrapper.eq(AddToEnsemble::getEnsembleId,addToEnsembleRequest.getEnsembleId());
    wrapper.eq(AddToEnsemble::getVideoId,addToEnsembleRequest.getVideoId());
    if(addToEnsembleMapper.selectOne(wrapper)!=null){
        return Result.data(false);
    }
        addToEnsembleMapper.insert(addToEnsembleRequest.toEntity());
        return Result.data(true);
    }
    @Override
    public Result<Boolean> deleteFromEnsemble(DeleteFromEnsembleRequest deleteFromEnsembleRequest){
        LambdaQueryWrapper<AddToEnsemble> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(AddToEnsemble::getEnsembleId,deleteFromEnsembleRequest.getEnsembleId());
        wrapper.eq(AddToEnsemble::getVideoId,deleteFromEnsembleRequest.getVideoId());
        addToEnsembleMapper.delete(wrapper);
        return Result.data(true);
    }
}
