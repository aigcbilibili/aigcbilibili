package ljl.bilibili.chat.mapper;

import ljl.bilibili.chat.entity.NoticeCount;
import ljl.bilibili.chat.vo.response.ChatSessionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChatServiceMapper {
    @Select("select u.cover,u.nickname,u.id as userId,s.id as sessionId,s.update_time as updateTime,s.update_content as updateContent from chat_session s inner join user u on s.receiver_id=u.id where s.sender_id=#{userId}")
    List<ChatSessionResponse> getSelfSession(Integer userId);
    @Select("select u.cover,u.nickname,u.id as userId,s.id as sessionId,s.update_time as updateTime,s.update_content as updateContent from chat_session s inner join user u on s.sender_id=u.id where s.receiver_id=#{userId}")
    List<ChatSessionResponse> getOtherSession(Integer userId);
    @Select({
            "<script>",
            "SELECT",
            "    s.id    AS sessionId,",
            "    COUNT(*) AS noticeCount",
            "FROM",
            "    chat_notice n",
            "INNER JOIN",
            "    chat_session s",
            "ON",
            "    (n.sender_id = s.sender_id AND n.receiver_id = s.receiver_id)",
            "    OR",
            "    (n.sender_id = s.receiver_id AND n.receiver_id = s.sender_id)",
            "WHERE",
            "    n.status = 0",
            "    AND n.receiver_id = #{userId}",
            "    AND s.id IN",
            "    <foreach item='id' collection='sessionIds' open='(' separator=',' close=')'>",
            "        #{id}",
            "    </foreach>",
            "GROUP BY",
            "    sessionId",
            "</script>"
    })
    List<NoticeCount> getNoticeCounts(List<Integer> sessionIds,Integer userId);
    @Update("update chat_session s set update_content=#{content},update_time=#{updateTime} where " +
            "(s.sender_id=#{senderId} and s.receiver_id=#{receiverId}) or (s.sender_id=#{receiverId} and s.receiver_id=#{senderId}) ")
    void updateChatSession(String content, LocalDateTime updateTime, Integer senderId, Integer receiverId);
}
