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
import ljl.bilibili.video.mapper.VideoServiceMapper;
import ljl.bilibili.video.service.audience_reactions.collect.CollectService;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectGroupRequest;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectRequest;
import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectGroupResponse;
import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectVideoResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 收藏
 */
@Service
public class CollectServiceImpl implements CollectService {
    @Resource
    CollectMapper collectMapper;
    @Resource
    CollectGroupMapper collectGroupMapper;
    @Resource
    SendNoticeClient client;
    @Resource
    VideoServiceMapper videoServiceMapper;
    /**
     *收藏视频
     */
    @Override
    public Result<Boolean> collect(List<CollectRequest> collects) {
        for(CollectRequest collectRequest : collects){
            //如果收藏夹的type值是true则为收藏否则是取消收藏
            if(collectRequest.getType()){
                Collect collect=new Collect();
                collect.setCollectGroupId(collectRequest.getCollectGroupId());
                collect.setVideoId(collectRequest.getVideoId());
                collectMapper.insert(collect);
            }else {
                LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Collect::getVideoId, collectRequest.getVideoId());
                wrapper.eq(Collect::getCollectGroupId, collectRequest.getCollectGroupId());
                collectMapper.delete(wrapper);
            }
        }
        return Result.success(true);
    }
    /**
     *获取视频详情页中收藏夹列表（标注了视频相对于收藏夹的状态是否已收藏）
     */
    @Override
    public Result<List<CollectGroupResponse>> getVideoToCollectGroup(Integer userId,Integer videoId){
        LambdaQueryWrapper<CollectGroup> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(CollectGroup::getUserId,userId);
        //获取当前用户的收藏夹列表
        List<CollectGroup> collectGroupList=collectGroupMapper.selectList(wrapper);
        if(collectGroupList.size()>0){
            List<Integer> ids=new ArrayList<>();
            Map<Integer, CollectGroupResponse> map=new HashMap<>();
            //获取收藏夹列表的id集合用于查询收藏记录，并封装成最后的响应类的一部分
            for(CollectGroup collectGroup : collectGroupList){
                ids.add(collectGroup.getId());
                map.put(collectGroup.getId(),new CollectGroupResponse().setCollectGroupId(collectGroup.getId()).setCollectGroupName(collectGroup.getName()).setCreateTime(collectGroup.getCreateTime()));
            }
            LambdaQueryWrapper<Collect> collectLambdaQueryWrapper=new LambdaQueryWrapper<>();
            collectLambdaQueryWrapper.in(Collect::getCollectGroupId,ids);
            collectLambdaQueryWrapper.eq(Collect::getVideoId,videoId);
            List<Collect> collects=collectMapper.selectList(collectLambdaQueryWrapper);
            //设置收藏夹的状态，可能有些收藏夹并没有收藏该视频则为默认值false
            for(Collect collect : collects){
                if(map.get(collect.getCollectGroupId())!=null){
                    map.get(collect.getCollectGroupId()).setHasCollect(true);
                }
            }
            //遍历收藏夹map并封装成集合
            List<CollectGroupResponse> collectGroupResponseList=new ArrayList<>();
            for(Map.Entry<Integer,CollectGroupResponse> entry : map.entrySet()){
                collectGroupResponseList.add(entry.getValue());
            }
            return Result.data(collectGroupResponseList);
        }
        return Result.data(new ArrayList<>());
    }
    /**
     *编辑收藏夹
     */
    @Override
    public Result<Boolean> editCollectGroup(CollectGroupRequest createCollectGroupRequest) {
        if(createCollectGroupRequest.getId()!=null){
            collectGroupMapper.updateById(createCollectGroupRequest.toEntity());
        }else {
            collectGroupMapper.insert(createCollectGroupRequest.toEntity());

        }

        return Result.success(true);
    }
    /**
     *删除收藏夹
     */
    @Override
    public Result<Boolean> deleteCollectGroup(CollectGroupRequest createCollectGroupRequest) {
        collectGroupMapper.deleteById(createCollectGroupRequest.toEntity());
        return Result.success(true);
    }
    /**
     *获取首页收藏夹列表
     */
    @Override
    public Result<List<CollectGroup>> getCollectGroup(Integer userId) {
        LambdaQueryWrapper<CollectGroup> collectGroupQueryWrapper = new LambdaQueryWrapper<>();
        collectGroupQueryWrapper.eq(CollectGroup::getUserId, userId);
        collectGroupQueryWrapper.orderByDesc(CollectGroup::getCreateTime);
        return Result.data(collectGroupMapper.selectList(collectGroupQueryWrapper));
    }
    /**
     *获取某个收藏夹下的视频
     */
    @Override
    public Result<List<CollectVideoResponse>> getCollectVideo(Integer collectGroupId) {
        return Result.data(videoServiceMapper.getCollectVideo(collectGroupId));
    }
}
