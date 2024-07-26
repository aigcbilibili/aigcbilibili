package ljl.bilibili.user_center.service.follow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import ljl.bilibili.entity.user_center.user_relationships.IdCount;
import ljl.bilibili.mapper.user_center.user_relationships.FollowMapper;
import ljl.bilibili.user_center.service.follow.FollowService;
import ljl.bilibili.user_center.vo.response.follow.IdolOrFansListResponse;
import ljl.bilibili.util.Result;
import org.springframework.stereotype.Service;
import ljl.bilibili.user_center.vo.request.follow.FollowRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//关注
@Service
public class FollowServiceImpl implements FollowService {
    @Resource
    FollowMapper followMapper;
    /**
     *关注用户
     */
@Override
    public Result<Boolean> follow(FollowRequest follow) {
        followMapper.insert(follow.toEntity());
        return Result.success(true);
    }
    /**
     *撤销关注
     */
    @Override
    public Result<Boolean> recallFollow( FollowRequest follow) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getIdolId, follow.getIdolId());
        wrapper.eq(Follow::getFansId, follow.getFansId());
        followMapper.delete(wrapper);
        return Result.success(true);
    }
    /**
     *获取关注或粉丝列表
     */
    @Override
    public Result<List<IdolOrFansListResponse>> getIdolOrFansListResponse(int userId, int type){
      MPJLambdaWrapper<Follow> wrapper=new MPJLambdaWrapper<>();
      //根据类型判断是要获取关注还是粉丝列表
      if(type==0){
          wrapper.eq(Follow::getFansId,userId);
          wrapper.innerJoin(User.class, User::getId,Follow::getIdolId);
      }else {
          wrapper.eq(Follow::getIdolId,userId);
          wrapper.innerJoin(User.class, User::getId,Follow::getFansId);
      }
      wrapper.selectAs(User::getNickname,IdolOrFansListResponse::getNickname);
      wrapper.selectAs(User::getCover,IdolOrFansListResponse::getCover);
      wrapper.selectAs(User::getId, IdolOrFansListResponse::getUserId);
      wrapper.select(Follow::getCreateTime);
      wrapper.orderByDesc(Follow::getCreateTime);
      List<IdolOrFansListResponse> responses= followMapper.selectJoinList(IdolOrFansListResponse.class,wrapper);
      List<Integer> ids=new ArrayList<>();
      for(IdolOrFansListResponse response : responses){
          ids.add(response.getUserId());
      }
        //获取关注列表和粉丝列表的关注数和粉丝数并封装到响应集合里
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
     for(IdolOrFansListResponse response : responses){
         response.setIdolCount(idolCountMap.get(response.getUserId()))
                 .setFansCount(fansCountMap.get(response.getUserId()));
     }
      return Result.data(responses);
    }

}
