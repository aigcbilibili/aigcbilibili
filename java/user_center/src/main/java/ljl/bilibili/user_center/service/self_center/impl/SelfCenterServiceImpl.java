package ljl.bilibili.user_center.service.self_center.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import ljl.bilibili.entity.user_center.user_relationships.IdCount;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.user_center.user_info.PrivilegeMapper;
import ljl.bilibili.mapper.user_center.user_relationships.FollowMapper;
import ljl.bilibili.mapper.video.audience_reactions.like.LikeMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.user_center.mapper.UserCenterServiceMapper;
import ljl.bilibili.user_center.service.follow.FollowService;
import ljl.bilibili.user_center.service.self_center.SelfCenterService;
import ljl.bilibili.user_center.vo.request.self_center.EditUserCenterPrivilegeRequest;
import ljl.bilibili.user_center.vo.response.follow.IdolOrFansListResponse;
import ljl.bilibili.user_center.vo.response.self_center.*;
import ljl.bilibili.util.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *个人主页
 */
@Service
public class SelfCenterServiceImpl implements SelfCenterService {
    String sql="limit 10";
    @Resource
    VideoMapper videoMapper;
    @Resource
    PrivilegeMapper privilegeMapper;

    @Resource
    LikeMapper likeMapper;
    @Resource
    UserCenterServiceMapper userCenterServiceMapper;
    @Resource
    FollowService followService;
    @Resource
    FollowMapper followMapper;
    /**
     *获取某用户个人主页权限，并根据个人主页权限决定是否要返回对应模块内容
     */
    @Override
    public Result<SelfCenterContentResponse> getPersonalCenterContent(Integer selfId, Integer visitedId) {
        LambdaQueryWrapper<Privilege> privilegeWrapper=new LambdaQueryWrapper<>();
        privilegeWrapper.eq(Privilege::getUserId,visitedId);
        Privilege privilege=privilegeMapper.selectOne(privilegeWrapper);
        if(selfId.equals(visitedId)){
            privilege.setCollectGroup(0).setRemotelyLike(0).setFansList(0).setIdolList(0);
        }
        //投稿视频都公开，因此不需要查询权限后再确认是否需要返回
        SelfCenterContentResponse selfCenterContentResponse=new SelfCenterContentResponse();
        MPJLambdaWrapper<Video> wrapper=new MPJLambdaWrapper<>();
        wrapper.eq(Video::getUserId,visitedId);
        wrapper.selectAs(Video::getId,SelfVideoResponse::getVideoId);
        wrapper.orderByDesc(Video::getCreateTime);
        wrapper.select(Video::getCreateTime,Video::getCover,Video::getLength,Video::getName);
        wrapper.select(VideoData::getPlayCount);
        wrapper.leftJoin(VideoData.class,VideoData::getVideoId,Video::getId);
        List<SelfVideoResponse> selfVideoResponses=videoMapper.selectJoinList(SelfVideoResponse.class,wrapper);
        selfCenterContentResponse.setSelfVideoResponse(selfVideoResponses);
        //收藏夹
        if(privilege.getCollectGroup()==0){
            List<SelfCollectResponse> selfCollectResponses= userCenterServiceMapper.getSelfCollect(visitedId);
            selfCenterContentResponse.setSelfCollectResponse(selfCollectResponses);
        }
        //粉丝列表
        if(privilege.getFansList()==0){
            MPJLambdaWrapper<Follow> fansWrapper=new MPJLambdaWrapper<>();
            fansWrapper.eq(Follow::getIdolId,visitedId);
            fansWrapper.innerJoin(User.class, User::getId,Follow::getFansId);
            fansWrapper.selectAs(User::getNickname, IdolOrFansListResponse::getNickname);
            fansWrapper.selectAs(User::getCover,IdolOrFansListResponse::getCover);
            fansWrapper.selectAs(User::getId, IdolOrFansListResponse::getUserId);
            fansWrapper.select(Follow::getCreateTime);
            fansWrapper.orderByDesc(Follow::getCreateTime);
            List<IdolOrFansListResponse> fansResponses= followMapper.selectJoinList(IdolOrFansListResponse.class,fansWrapper);
            List<Integer> ids=new ArrayList<>();
            for(IdolOrFansListResponse response : fansResponses){
                ids.add(response.getUserId());
            }
            if(ids.size()>0){
                List<IdCount> fansCountList= followMapper.getFansCount(ids);
                List<IdCount> idolCountList= followMapper.getIdolCount(ids);
                Map<Integer,Integer> fansCountMap=new HashMap<>(10);
                Map<Integer,Integer> idolCountMap=new HashMap<>(10);
                for(IdCount response : fansCountList){
                    fansCountMap.put(response.getId(),response.getCount());
                }
                for(IdCount response : idolCountList){
                    idolCountMap.put(response.getId(),response.getCount());
                }
                for(IdolOrFansListResponse response : fansResponses){
                    if(idolCountMap.get(response.getUserId())!=null){
                        response.setIdolCount(idolCountMap.get(response.getUserId()));
                    }
                    if(fansCountMap.get(response.getUserId())!=null){
                        response.setFansCount(fansCountMap.get(response.getUserId()));
                    }
                }
                selfCenterContentResponse.setFansListResponse(fansResponses);
            }
        }
        //关注列表
        if(privilege.getIdolList()==0){
            MPJLambdaWrapper<Follow> idolWrapper=new MPJLambdaWrapper<>();
            idolWrapper.eq(Follow::getFansId,visitedId);
            idolWrapper.innerJoin(User.class, User::getId,Follow::getIdolId);
            idolWrapper.selectAs(User::getNickname, IdolOrFansListResponse::getNickname);
            idolWrapper.selectAs(User::getCover,IdolOrFansListResponse::getCover);
            idolWrapper.selectAs(User::getId, IdolOrFansListResponse::getUserId);
            idolWrapper.select(Follow::getCreateTime);
            idolWrapper.orderByDesc(Follow::getCreateTime);
            List<IdolOrFansListResponse> idolListResponses= followMapper.selectJoinList(IdolOrFansListResponse.class,idolWrapper);
            List<Integer> ids=new ArrayList<>();
            for(IdolOrFansListResponse response : idolListResponses){
                ids.add(response.getUserId());
            }
            if(ids.size()>0){
                List<IdCount> fansCountList= followMapper.getFansCount(ids);
                List<IdCount> idolCountList= followMapper.getIdolCount(ids);
                Map<Integer,Integer> fansCountMap=new HashMap<>(10);
                Map<Integer,Integer> idolCountMap=new HashMap<>(10);
                for(IdCount response : fansCountList){
                    fansCountMap.put(response.getId(),response.getCount());
                }
                for(IdCount response : idolCountList){
                    idolCountMap.put(response.getId(),response.getCount());
                }
                for(IdolOrFansListResponse response : idolListResponses){
                    response.setIdolCount(idolCountMap.get(response.getUserId()))
                            .setFansCount(fansCountMap.get(response.getUserId()));
                }
                selfCenterContentResponse.setIdolListResponse(idolListResponses);
            }
        }
        //最近点赞
        if(privilege.getRemotelyLike()==0){
            MPJLambdaWrapper<Like> remotelyLikeWrapper=new MPJLambdaWrapper<>();
            remotelyLikeWrapper.eq(Like::getUserId,visitedId);
            remotelyLikeWrapper.isNull(Like::getCommentId);
            remotelyLikeWrapper.orderByDesc(Like::getCreateTime);
            remotelyLikeWrapper.selectAs(Video::getId,RemotelyLikeVideoResponse::getVideoId);
            remotelyLikeWrapper.select(Video::getCreateTime,Video::getCover,Video::getLength,Video::getName);
            remotelyLikeWrapper.select(VideoData::getPlayCount,VideoData::getDanmakuCount);
            remotelyLikeWrapper.leftJoin(Video.class,Video::getId, Like::getVideoId);
            remotelyLikeWrapper.last(sql);
            remotelyLikeWrapper.leftJoin(VideoData.class,VideoData::getVideoId,Video::getId);
            List<RemotelyLikeVideoResponse> remotelyLikeVideoResponses=likeMapper.selectJoinList(RemotelyLikeVideoResponse.class,remotelyLikeWrapper);
            selfCenterContentResponse.setRemotelyLikeVideoResponse(remotelyLikeVideoResponses);
        }
        return Result.data(selfCenterContentResponse);
    }
    /**
     *获取个人主页权限
     */
    @Override
    public Result<PrivilegeResponse> getUserPrivilege(Integer userId){
        LambdaQueryWrapper<Privilege> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Privilege::getUserId,userId);
        Privilege privilege=privilegeMapper.selectOne(wrapper);
        return Result.data(new PrivilegeResponse().setCollectGroup(privilege.getCollectGroup()).setFansList(privilege.getFansList()).setIdolList(privilege.getIdolList()).setRemotelyLike(privilege.getRemotelyLike()));
    }
    /**
     *编辑个人主页权限
     */
    @Override
    public Result<Boolean> editUserPrivilege(EditUserCenterPrivilegeRequest editUserCenterPrivilegeRequest){
        LambdaUpdateWrapper<Privilege> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(Privilege::getUserId,editUserCenterPrivilegeRequest.getUserId());
        privilegeMapper.update(editUserCenterPrivilegeRequest.toEntity(),wrapper);
        return Result.success(true);
    }


}
