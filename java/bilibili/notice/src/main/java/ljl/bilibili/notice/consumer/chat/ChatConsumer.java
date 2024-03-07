package ljl.bilibili.notice.consumer.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.mapper.chat.ChatMapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 *私聊消息消费者
 */
@Service
@RocketMQMessageListener(
        topic = "chat",
        consumerGroup = "chat-group",
        consumeMode = ConsumeMode.CONCURRENTLY
)
public class ChatConsumer implements RocketMQListener<MessageExt> {
    @Resource
    ChatMapper chatMapper;
    @Resource
    ObjectMapper objectMapper;

    @Override
    public void onMessage(MessageExt messageExt){
        System.out.println("get");
        String jsonMessage = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        Chat chat;
        try {
            chat = objectMapper.readValue(jsonMessage, Chat.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        chatMapper.insert(chat);
    }
}
