package ljl.bilibili.mapper;
import ljl.bilibili.video.vo.response.audience_reactions.comment.CommentDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface GetCommentMapper {
    @Select("select c.id,c.content,c.create_time as createTime,c.top_id as topId,u.id as senderId,u.nickname as senderName,u.cover as senderCoverUrl,parent.id as receiverId,parentUser.cover as receiverCoverUrl,parentUser.nickname as receiverName,parentUser.id as receiverUserId from comment c inner join comment parent on c.parent_id=parent.id inner join user u on c.user_id=u.id " +
            "inner join user parentUser on parent.user_id=parentUser.id where c.video_id=#{videoId} and c.parent_id is not null")
    List<CommentDetailResponse> getComment(Integer videoId);

    @Select("select c.id,c.content,c.create_time as createTime,u.id as senderId,u.nickname as senderName,u.cover as senderCoverUrl from comment c  inner join user u on c.user_id=u.id " +
            "where c.video_id=#{videoId} and c.parent_id is null")
    List<CommentDetailResponse> getComment2(Integer videoId);
}
