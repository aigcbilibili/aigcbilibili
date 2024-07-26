package ljl.bilibili.search.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.user_relationships.IdCount;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.mapper.user_center.user_relationships.FollowMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoDataMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.search.constant.Constant;
import ljl.bilibili.search.entity.UserEntity;
import ljl.bilibili.search.service.MysqlToEsService;
import ljl.bilibili.search.vo.response.VideoKeywordSearchResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *MySQL同步数据到ES
 */
@Service
public class MysqlToEsServiceImpl implements MysqlToEsService {
    @Resource
    VideoMapper videoMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ObjectMapper objectMapper;
    @Resource
    FollowMapper followMapper;
    @Resource
    VideoDataMapper videoDataMapper;
    @Resource
    RestHighLevelClient client;
    /**
     *用户全量同步
     */
@Override
    public Boolean userMysqlToEs() throws IOException {
    //查询出所有用户并转换成map插入es
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<>();
        List<Map<String, Object>> userMap = new ArrayList<>();
        wrapper.select(User::getCover, User::getNickname, User::getId, User::getIntro);
        List<UserEntity> userList = userMapper.selectJoinList(UserEntity.class, wrapper);
        for (UserEntity user : userList) {
            userMap.add(objectMapper.convertValue(user,Map.class));
        }
        for (Map<String, Object> map : userMap) {
            IndexRequest indexRequest = new IndexRequest(Constant.USER_INDEX_NAME);
            Integer id = (Integer) map.get(Constant.INDEX_ID);
            indexRequest.id(id.toString());
            indexRequest.source(map, XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);
        }
        return true;
    }
    /**
     *视频全量同步
     */
    @Override
    public Boolean videoMysqlToEs() throws IOException {
        //查询出所有视频并转换成map插入es
        MPJLambdaWrapper<Video> wrapper = new MPJLambdaWrapper<>();
        wrapper.leftJoin(User.class, User::getId, Video::getUserId);
        wrapper.leftJoin(VideoData.class, VideoData::getVideoId, Video::getId);
        wrapper.select(Video::getCover, Video::getIntro, Video::getCreateTime, Video::getLength, Video::getUrl);
        wrapper.select(VideoData::getDanmakuCount, VideoData::getPlayCount);
        wrapper.selectAs(Video::getName, VideoKeywordSearchResponse::getVideoName);
        wrapper.selectAs(User::getNickname, VideoKeywordSearchResponse::getAuthorName);
        wrapper.selectAs(Video::getId, VideoKeywordSearchResponse::getVideoId);
        wrapper.selectAs(User::getId, VideoKeywordSearchResponse::getAuthorId);
        List<VideoKeywordSearchResponse> list = videoMapper.selectJoinList(VideoKeywordSearchResponse.class, wrapper);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (VideoKeywordSearchResponse response : list) {
            mapList.add(objectMapper.convertValue(response, Map.class));
        }
        for (Map<String, Object> map : mapList) {
            IndexRequest indexRequest = new IndexRequest(Constant.VIDEO_INDEX_NAME);
            indexRequest.id(String.valueOf(map.get(Constant.VIDEO_INDEX_ID)));
            indexRequest.source(map, XContentType.JSON);
            client.index(indexRequest, RequestOptions.DEFAULT);
        }
        updateUserData();
        return true;
    }
    /**
     *视频数据全量同步
     */
    @Override
    public Boolean updateVideoData() throws IOException {
        //查询出所有视频绑定的相关数据比如播放数评论数弹幕数然后插入es
        BulkRequest bulkRequest = new BulkRequest();
        List<VideoData> videoDataList = videoDataMapper.selectList(null);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (VideoData videoData : videoDataList) {
            Map<String,Object> map=objectMapper.convertValue(videoData,Map.class);
            map.remove(Constant.INDEX_ID);
            map.remove(Constant.VIDEO_INDEX_REMOVE_COMMENT_COUNT);
            map.remove(Constant.VIDEO_INDEX_REMOVE_LIKE_COUNT);
            mapList.add(map);
        }
        for (Map<String, Object> map : mapList) {
            int intId = (Integer) map.get(Constant.VIDEO_INDEX_ID);
            String id = String.valueOf(intId);
            bulkRequest.add(new UpdateRequest(Constant.VIDEO_INDEX_NAME, id).doc(map));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return true;
    }
    /**
     *用户关注数、作品数全量同步
     */
    @Override
    public Boolean updateUserData() throws IOException {
        //查询出所有用户的相关数据比如关注数粉丝数然后插入es
        BulkRequest bulkRequest = new BulkRequest();
        List<User> userList = userMapper.selectList(null);
        List<Integer> ids = new ArrayList<>();
        for (User user : userList) {
            ids.add(user.getId());
        }
        Map<Integer, Integer> fansCountMap = new HashMap<>();
        Map<Integer, Integer> videoCountMap = new HashMap<>();
        List<IdCount> idCountList = followMapper.getIdolCount(ids);
        List<IdCount> videoCountList=followMapper.getVideoCount(ids);
        for (IdCount idCount : idCountList) {
            fansCountMap.put(idCount.getId(), idCount.getCount());
        }
        for(IdCount idCount : videoCountList){
            videoCountMap.put(idCount.getId(),idCount.getCount());
        }
        for (Integer id : ids) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constant.INDEX_ID, id.toString());
            map.put(Constant.USER_INDEX_PUT_FANS_COUNT, fansCountMap.getOrDefault(id, 0));
            map.put(Constant.USER_INDEX_PUT_VIDEO_COUNT, videoCountMap.getOrDefault(id, 0));
            bulkRequest.add(new UpdateRequest(Constant.USER_INDEX_NAME, id.toString()).doc(map));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return true;
    }
}
