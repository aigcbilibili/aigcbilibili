package ljl.bilibili.user_center.service.user_info.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.mapper.user_center.user_relationships.FollowMapper;
import ljl.bilibili.user_center.mapper.UserCenterServiceMapper;
import ljl.bilibili.user_center.service.user_info.UserInfoService;
import ljl.bilibili.util.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ljl.bilibili.user_center.vo.response.self_center.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static ljl.bilibili.user_center.constant.Constant.*;
/**
 *用户信息
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    UserMapper userMapper;
    @Resource
    FollowMapper followMapper;
    @Resource
    MinioClient minioClient;
    @Value("${minio.bucket.name}")
    private String bucketName;
    String filePath="https://labilibili.com/";
    @Resource
    SendNoticeClient sendNoticeClient;
    @Resource
    UserCenterServiceMapper userCenterServiceMapper;
    /**
     *获取用户信息
     */
    @Override
    public Result<UserInfoResponse> getUserInfo(Integer selfId,Integer visitedId){
        //获取粉丝数、关注数
        MPJLambdaWrapper<User> fansCountWrapper=new MPJLambdaWrapper<>();
        MPJLambdaWrapper<User> idolCountWrapper=new MPJLambdaWrapper<>();
        fansCountWrapper.eq(User::getId,visitedId);
        fansCountWrapper.leftJoin(Follow.class,Follow::getIdolId, User::getId);
        idolCountWrapper.eq(User::getId,visitedId);
        idolCountWrapper.leftJoin(Follow.class,Follow::getFansId, User::getId);
        //获取用户信息并将粉丝数关注数封装进去
        UserInfoResponse userInfoResponse=userCenterServiceMapper.getUserInfo(visitedId)
                .setFansCount(Math.toIntExact(userMapper.selectJoinCount(fansCountWrapper)))
                .setIdolCount(Math.toIntExact(userMapper.selectJoinCount(idolCountWrapper)));
        //如果自己是已登录状态才会查询该用户是否已关注，未登录状态访问个人主页是不会查询是否该用户已关注的
        if(selfId>0){
            LambdaQueryWrapper<Follow> followLambdaQueryWrapper=new LambdaQueryWrapper<>();
            followLambdaQueryWrapper.eq(Follow::getFansId,selfId);
            followLambdaQueryWrapper.eq(Follow::getIdolId,visitedId);
            if(followMapper.selectList(followLambdaQueryWrapper).size()>0){
                userInfoResponse.setIsFollowing(true);
            }else {
                userInfoResponse.setIsFollowing(false);
            }
        }
        return Result.data(userInfoResponse);
    }
    /**
     *修改用户信息并发送数据同步消息
     */
    @Override
    public Result<Boolean> editSelfInfo(MultipartFile file, Integer userId, String nickname, String intro) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //创建数据同步发送消息要到的map并填充值
        Map<String,Object> map=new HashMap<>();
        map.put(TABLE_ID,userId);
        map.put(OPERATION_TYPE,OPERATION_TYPE_UPDATE);
        map.put(TABLE_NAME,USER_TABLE_NAME);
        User user=new User();
        user.setId(userId);
        //如果上传的修改个人信息请求有封面就上传封面
        if(file!=null){
            String coverName=UUID.randomUUID().toString().substring(0,10)+file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder().contentType(file.getContentType()).stream(file.getInputStream(),-1,10485760).bucket(bucketName).object(coverName).build());
            String url= filePath+bucketName+"/"+coverName;
            map.put(USER_COVER,url);
            user.setCover(url);
        }
        //如果有昵称就修改昵称
        if(nickname!=null){
            map.put(USER_NICKNAME,nickname);
            user.setNickname(nickname);
        }
        //有介绍就修改介绍
        if(intro!=null){
            map.put(USER_INTRO,intro);
            user.setIntro(intro);
        }
        userMapper.updateById(user);
        sendNoticeClient.sendDBChangeNotice(map);
        return Result.success(true);
    }
}
