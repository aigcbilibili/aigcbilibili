package ljl.bilibili.user_center.service.self_center.impl;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.user_center.user_info.PrivilegeMapper;
import ljl.bilibili.mapper.video.audience_reactions.like.LikeMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.user_center.service.self_center.SelfCenterService;
import ljl.bilibili.user_center.vo.request.self_center.EditUserCenterPrivilegeRequest;
import ljl.bilibili.user_center.vo.response.self_center.RemotelyLikeVideoResponse;
import ljl.bilibili.user_center.vo.response.self_center.SelfVideoResponse;
import ljl.bilibili.user_center.vo.response.self_center.UserPrivilegeResponse;
import ljl.bilibili.util.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SelfCenterServiceImpl implements SelfCenterService {
    String sql="limit 10";
    @Resource
    VideoMapper videoMapper;
    @Resource
    PrivilegeMapper privilegeMapper;

    @Resource
    LikeMapper likeMapper;
    @Override
    public Result<List<SelfVideoResponse>> getPersonalVideo(Integer userId) {
        MPJLambdaWrapper<Video> wrapper=new MPJLambdaWrapper<>();
        wrapper.eq(Video::getUserId,userId);
        wrapper.selectAs(Video::getId,SelfVideoResponse::getVideoId);
        wrapper.orderByDesc(Video::getCreateTime);
        wrapper.select(Video::getCreateTime,Video::getCover,Video::getLength,Video::getName);
        wrapper.select(VideoData::getPlayCount);
        wrapper.leftJoin(VideoData.class,VideoData::getVideoId,Video::getId);
        return Result.data(videoMapper.selectJoinList(SelfVideoResponse.class,wrapper));
    }
    @Override
    public Result<List<RemotelyLikeVideoResponse>> getRemotelyLikeVideo(Integer userId) {
        MPJLambdaWrapper<Like> wrapper=new MPJLambdaWrapper<>();
        wrapper.eq(Like::getUserId,userId);
        wrapper.isNull(Like::getCommentId);
        wrapper.orderByDesc(Like::getCreateTime);
        wrapper.selectAs(Video::getId,RemotelyLikeVideoResponse::getVideoId);
        wrapper.select(Video::getCreateTime,Video::getCover,Video::getLength,Video::getName);
        wrapper.select(VideoData::getPlayCount,VideoData::getDanmakuCount);
        wrapper.leftJoin(Video.class,Video::getId, Like::getVideoId);
        wrapper.last(sql);
        wrapper.leftJoin(VideoData.class,VideoData::getVideoId,Video::getId);
       return Result.data(likeMapper.selectJoinList(RemotelyLikeVideoResponse.class,wrapper));
    }
    @Override
    public Result<UserPrivilegeResponse> getUserPrivilege(int userId){
        return Result.data(new UserPrivilegeResponse(privilegeMapper.selectById(userId)));
    }
    @Override
    public Result<Boolean> editUserPrivilege(EditUserCenterPrivilegeRequest editUserCenterPrivilegeRequest){
        privilegeMapper.updateById(editUserCenterPrivilegeRequest.toEntity());
        return Result.success(true);
    }


}
