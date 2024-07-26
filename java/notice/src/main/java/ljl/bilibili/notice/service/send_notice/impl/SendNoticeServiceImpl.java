package ljl.bilibili.notice.service.send_notice.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.client.pojo.LikeNoticeAddOrDelete;
import ljl.bilibili.client.pojo.UploadVideo;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.notice.dynamic.Dynamic;
import ljl.bilibili.entity.notice.comment.CommentNotice;
import ljl.bilibili.notice.service.send_notice.SendNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 *发送异步消息
 */
@Service
@Slf4j
public class SendNoticeServiceImpl implements SendNoticeService {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    ObjectMapper objectMapper;
@Override
    public Boolean sendVideoUpdateMessageWithCallback(Dynamic dynamic) throws JsonProcessingException {
        String topic = "dynamic";
        String jsonMessage = objectMapper.writeValueAsString(dynamic);
        rocketMQTemplate.
                asyncSend(topic,jsonMessage, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("Rocket发射成功" );
            }
            @Override
            public void onException(Throwable e) {
                e.getMessage();
               log.error("坠机: " + e.getMessage());
            }
        });
        return true;
    }
    @Override
    public Boolean sendVideoLikeMessageWithCallback(LikeNoticeAddOrDelete likeNotice) throws JsonProcessingException {
        String topic = "like";
        String jsonMessage = objectMapper.writeValueAsString(likeNotice);
        rocketMQTemplate.
        asyncSend(topic,jsonMessage, new SendCallback() {
    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("Rocket发射成功" );
    }

    @Override
    public void onException(Throwable e) {
        e.getMessage();
        log.error("坠机: " + e.getMessage());
    }
});
        return true;
    }
    @Override
    public Boolean sendCommentMessageWithCallback(CommentNotice commentNotice) throws JsonProcessingException {
        String topic = "comment";
        String jsonMessage = objectMapper.writeValueAsString(commentNotice);
        rocketMQTemplate.
        asyncSend(topic,jsonMessage, new SendCallback() {
    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("Rocket发射成功" );
    }

    @Override
    public void onException(Throwable e) {
        e.getMessage();
       log.error("坠机: " + e.getMessage());
    }
});
        return true;
    }
    @Override
    public Boolean sendChatMessageWithCallback(Chat chat) throws JsonProcessingException {
        String topic = "chat";

        String jsonMessage = objectMapper.writeValueAsString(chat);
        rocketMQTemplate.
        asyncSend(topic,jsonMessage, new SendCallback() {
    @Override
    public void onSuccess(SendResult sendResult) {
       log.info("Rocket发射成功" );
    }

    @Override
    public void onException(Throwable e) {
        e.getMessage();
        log.error("坠机: " + e.getMessage());
    }
});
        return true;
    }
    @Override
    public Boolean sendUploadNotice(UploadVideo uploadVideo)throws JsonProcessingException {
        String topic="video-encode";
        String jsonMessage=objectMapper.writeValueAsString(uploadVideo);
        rocketMQTemplate.asyncSend(topic, jsonMessage, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("Rocket发射成功");
            }

            @Override
            public void onException(Throwable throwable) {
                throwable.getMessage();
            log.error("坠机:");
            }
        });
        return true;
    }
}
