package ljl.bilibili.video.mapper;

import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.comment.CommentDetailResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.FirstPageVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.HistoryVideoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoServiceMapper {
    @Select("select c.id,c.content,c.create_time as createTime,c.top_id as topId,u.id as senderId,u.nickname as senderName,u.cover as senderCoverUrl,parent.id as receiverId,parentUser.cover as receiverCoverUrl,parentUser.nickname as receiverName,parentUser.id as receiverUserId from comment c inner join comment parent on c.parent_id=parent.id inner join user u on c.user_id=u.id " +
            "inner join user parentUser on parent.user_id=parentUser.id where c.video_id=#{videoId} and c.parent_id is not null")
    List<CommentDetailResponse> getCommentToVideo(Integer videoId);

    @Select("select c.id,c.content,c.create_time as createTime,u.id as senderId,u.nickname as senderName,u.cover as senderCoverUrl from comment c  inner join user u on c.user_id=u.id " +
            "where c.video_id=#{videoId} and c.parent_id is null")
    List<CommentDetailResponse> getCommentToComment(Integer videoId);
    @Select("SELECT c.collect_group_id,c.video_id,v.length,v.name AS videoName,v.cover AS videoCover,u.nickname AS authorName FROM collect c LEFT JOIN video v ON (v.id = c.video_id) LEFT JOIN collect_group cg ON (cg.id = c.collect_group_id) LEFT JOIN user u ON (u.id = cg.user_id)     WHERE   (cg.id = #{collectGroupId}) ORDER BY c.create_time DESC")
    List<CollectVideoResponse> getCollectVideo(Integer collectGroupId);
    @Select("SELECT     v.name,v.length,v.create_time,v.cover,v.url,u.nickname AS authorName,v.id AS videoId,u.id AS authorId,vd.play_count,vd.danmaku_count   FROM video  v    LEFT JOIN user u ON (u.id = v.user_id) LEFT JOIN video_data vd ON (vd.video_id = v.id)          ORDER BY v.id DESC limit #{count},12")
    List<FirstPageVideoResponse> getFirstPageVideo(Integer count);
    @Select("select p.create_time as createTime,v.name AS videoName,v.length,v.id AS videoId,v.cover AS videoCover,u.id AS authorId,u.cover AS authorCover,u.nickname AS authorName   FROM play  p    INNER JOIN video v ON (v.id = p.video_id) INNER JOIN user u ON (u.id = v.user_id)     WHERE   (p.user_id = #{userId}) ORDER BY p.create_time DESC")
    List<HistoryVideoResponse> getHistoryVideo(Integer userId);
}
