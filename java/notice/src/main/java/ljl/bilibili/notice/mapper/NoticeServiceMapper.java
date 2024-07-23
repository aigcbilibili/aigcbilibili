package ljl.bilibili.notice.mapper;
import ljl.bilibili.notice.vo.response.get_notice.CommentNoticeResponse;
import ljl.bilibili.notice.vo.response.get_notice.DynamicVideoResponse;
import ljl.bilibili.notice.vo.response.get_notice.LikeNoticeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeServiceMapper {
    @Select("select u.nickname as nickName,u.id as userId,u.cover as userCover,v.cover as videoCover,v.id as videoId,ln.create_time as createTime,ln.status from video v inner join like_notice ln on ln.video_id = v.id inner join user u on u.id = ln.sender_id  where v.user_id = #{userId} and ln.comment_id is null order by ln.create_time desc")
     List<LikeNoticeResponse> getVideoLikeNotice(Integer userId);
    @Select("select u.nickname AS nickName,u.id AS userId,u.cover AS userCover,c1.video_id AS videoId,ln.comment_id AS commentId,ln.status AS status,c1.content  FROM comment c1    INNER JOIN like_notice ln ON (ln.comment_id = c1.id) INNER JOIN comment c2 ON (c2.id = ln.sender_id) INNER JOIN user u ON (u.id = c2.user_id) WHERE t.user_id = #{userId}")
    List<LikeNoticeResponse> getCommentLikeNotice(Integer userId);
    @Select("SELECT     cn.status,v.cover AS videoCover,v.id AS videoId,u.nickname AS nickName,u.cover AS userCover,c.content AS replyContent,c.id AS replyId,c.create_time AS createTime   FROM video  v    INNER JOIN comment c ON (c.video_id = v.id) INNER JOIN user u ON (u.id = c.user_id) INNER JOIN comment_notice cn ON (cn.sender_id = c.id)     WHERE   (v.user_id =#{userId}  AND c.parent_id IS NULL) ORDER BY cn.create_time DESC")
    List<CommentNoticeResponse> getCommentToVideoNotice(Integer userId);
    @Select("SELECT     t1.status,t3.cover AS userCover,t3.id AS userId,t.content AS replyToContent,comment.content AS replyContent,t1.sender_id AS replyId   FROM comment  t    INNER JOIN comment_notice t1 ON (t1.receiver_id = t.id) INNER JOIN comment comment ON (comment.id = t1.sender_id) INNER JOIN user t3 ON (t3.id = comment.user_id)     WHERE   (t.user_id = #{userId})")
    List<CommentNoticeResponse> getCommentToCommentNotice(Integer userId);
    @Select("SELECT     t.id,t.status,d.video_id,d.video_cover,d.video_name,d.create_time,u.cover AS authorCover,u.nickname AS authorName,u.id AS authorId   FROM dynamic_to_user  t    INNER JOIN dynamic d ON (d.id = t.dynamic_id) INNER JOIN user u ON (u.id = d.author_id)     WHERE   (t.user_id = #{userId}) ORDER BY d.create_time DESC")
    List<DynamicVideoResponse> getDynamicVideoNotice(Integer userId);
}
