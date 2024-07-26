package ljl.bilibili.notice.consumer.comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.entity.notice.comment.CommentNotice;
import ljl.bilibili.mapper.notice.comment.CommentNoticeMapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 评论消息消费者
 */
@Service
@RocketMQMessageListener(
        topic = "comment",
        consumerGroup = "comment-group",
        consumeMode = ConsumeMode.CONCURRENTLY
)
public class CommentConsumer implements RocketMQListener<MessageExt> {
    @Resource
    CommentNoticeMapper commentNoticeMapper;
    @Resource
    ObjectMapper objectMapper;
    /**
     *将评论消息通知插入数据库
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        String json = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        CommentNotice commentNotice;
        try {
            commentNotice = objectMapper.readValue(json, CommentNotice.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
            commentNoticeMapper.insert(commentNotice);



    }
}