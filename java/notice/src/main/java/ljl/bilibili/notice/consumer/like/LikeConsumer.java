package ljl.bilibili.notice.consumer.like;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.client.pojo.LikeNoticeAddOrDelete;
import ljl.bilibili.entity.notice.like.LikeNotice;
import ljl.bilibili.mapper.notice.like.LikeNoticeMapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
/**
 *点赞与取消点赞消费者，按顺序消费避免出现取消点赞消息在点赞消息之前触发导致异常的情况，但设置为并发执行其实也可以，rocketmq有重试机制，总会等到点赞消息生成的时刻
 */
@Service
@RocketMQMessageListener(
        topic = "like",
        consumerGroup = "like-group",
        consumeMode = ConsumeMode.ORDERLY
)
public class LikeConsumer implements RocketMQListener<MessageExt> {
    @Resource
    LikeNoticeMapper likeNoticeMapper;
    @Resource
    ObjectMapper objectMapper;
    /**
     *生成点赞消息与取消点赞消息
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        String json = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        LikeNoticeAddOrDelete likeNotice;
        LikeNotice notice;
        try {
            likeNotice = objectMapper.readValue(json, LikeNoticeAddOrDelete.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        notice=likeNotice.toNotice();
        //根据类型做判断
        if(likeNotice.getType()==0){
            likeNoticeMapper.insert(notice);
        }else {
            LambdaQueryWrapper<LikeNotice> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(LikeNotice::getVideoId,notice.getVideoId());
            wrapper.eq(LikeNotice::getSenderId,notice.getSenderId());
            if(notice.getCommentId()!=null){
                wrapper.eq(LikeNotice::getCommentId,notice.getCommentId());
            }else {
                wrapper.isNull(LikeNotice::getCommentId);
            }
            likeNoticeMapper.delete(wrapper);
        }

    }
}