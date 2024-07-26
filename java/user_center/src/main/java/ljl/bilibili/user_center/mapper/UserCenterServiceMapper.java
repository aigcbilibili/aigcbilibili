package ljl.bilibili.user_center.mapper;

import ljl.bilibili.user_center.vo.response.self_center.SelfCollectResponse;
import ljl.bilibili.user_center.vo.response.self_center.UserInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserCenterServiceMapper {
    @Select("select v.id as videoId,v.name as videoName,v.cover,u.id as authorId,u.nickname as authorName,c.create_time as createTime from collect_group cg inner join collect c on cg.id=c.collect_group_id inner join video v on c.video_id=v.id inner join user u on u.id=v.user_id inner join user u1 on cg.user_id=u1.id where u1.id=#{userId}")
    List<SelfCollectResponse> getSelfCollect(Integer userId);

    @Select("SELECT t.cover,t.intro,t.id,t.nickname,SUM(t2.like_count) AS likeCount,SUM(t2.play_count) AS playCount   FROM user t LEFT JOIN video t1 ON (t1.user_id = t.id) LEFT JOIN video_data t2 ON (t2.video_id = t1.id) WHERE   (t.id = #{visitedId})")
    UserInfoResponse getUserInfo(Integer visitedId);
}
