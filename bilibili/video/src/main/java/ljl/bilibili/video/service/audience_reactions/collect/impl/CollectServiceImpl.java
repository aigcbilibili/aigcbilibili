package ljl.bilibili.video.service.audience_reactions.collect.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.video.audience_reactions.collect.Collect;
import ljl.bilibili.entity.video.audience_reactions.collect.CollectGroup;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.mapper.video.audience_reactions.collect.CollectGroupMapper;
import ljl.bilibili.mapper.video.audience_reactions.collect.CollectMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoDataMapper;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.collect.CollectService;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectGroupRequest;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectRequest;
import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectVideoResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Resource
    CollectMapper collectMapper;
    @Resource
    CollectGroupMapper collectGroupMapper;
    @Resource
    SendNoticeClient client;
    @Resource
    VideoDataMapper videoDataMapper;
@Override
    public Result<Boolean> collect(CollectRequest collectRequest) {
        collectMapper.insert(collectRequest.toEntity());
        return Result.success(true);
    }
    @Override
    public Result<Boolean> recallCollect(CollectRequest collectRequest) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getVideoId, collectRequest.getVideoId());
        wrapper.eq(Collect::getCollectGroupId, collectRequest.getCollectGroupId());
        collectMapper.delete(wrapper);
        return Result.success(true);
    }
    @Override
    public Result<Boolean> editCollectGroup(CollectGroupRequest createCollectGroupRequest) {
            if(createCollectGroupRequest.getId()!=null){
                collectGroupMapper.updateById(createCollectGroupRequest.toEntity());
            }else {
                collectGroupMapper.insert(createCollectGroupRequest.toEntity());

            }

        return Result.success(true);
    }
    @Override
    public Result<Boolean> deleteCollectGroup(CollectGroupRequest createCollectGroupRequest) {
        collectGroupMapper.deleteById(createCollectGroupRequest.toEntity());
        return Result.success(true);
    }
    @Override
    public Result<List<CollectGroup>> getCollectGroup(Integer userId) {
        LambdaQueryWrapper<CollectGroup> collectGroupQueryWrapper = new LambdaQueryWrapper<>();
        collectGroupQueryWrapper.eq(CollectGroup::getUserId, userId);
        collectGroupQueryWrapper.orderByDesc(CollectGroup::getCreateTime);
        return Result.data(collectGroupMapper.selectList(collectGroupQueryWrapper));
    }
    @Override
    public Result<List<CollectVideoResponse>> getCollectVideo(int userId) {
        List<CollectVideoResponse> responses;
        MPJLambdaWrapper<Collect> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(Collect::getCollectGroupId, Collect::getVideoId);
        wrapper.orderByDesc(Collect::getCreateTime);
        wrapper.select(Video::getLength);
        wrapper.selectAs(Video::getName, CollectVideoResponse::getVideoName);
        wrapper.selectAs(Video::getCover, CollectVideoResponse::getVideoCover);
        wrapper.selectAs(User::getNickname, CollectVideoResponse::getAuthorName);
        wrapper.leftJoin(Video.class, Video::getId, Collect::getVideoId);
        wrapper.leftJoin(CollectGroup.class, CollectGroup::getId, Collect::getCollectGroupId);
        wrapper.leftJoin(User.class,User::getId,CollectGroup::getUserId);
        wrapper.eq(CollectGroup::getUserId,userId);
        responses = collectMapper.selectJoinList(CollectVideoResponse.class, wrapper);
        return Result.data(responses);
    }
}
