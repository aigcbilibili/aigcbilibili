package ljl.bilibili.notice.consumer.mysql_es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static ljl.bilibili.notice.constant.Constant.*;
/**
 *mysql与es数据同步消费者
 */
@Service
@Slf4j
@RocketMQMessageListener(
        topic = "mysqlToEs",
        consumerGroup = "mysql-es-group",
        consumeMode = ConsumeMode.CONCURRENTLY
)
public class MysqlToEsConsumer implements RocketMQListener<MessageExt> {
    @Resource
    ObjectMapper objectMapper;
    @Resource
    RedisTemplate objectRedisTemplate;
    /**
     *根据map的键值对不同将其存入特定redis键值对
     */
    @Override
    public void onMessage(MessageExt messageExt){
        String json = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        HashMap<String,Object> map ;
        try {
            //将json消息转成map
            map = objectMapper.readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //如果操作类型是新增的话
        if(map.get(OPERATION_TYPE).equals(OPERATION_TYPE_ADD)){
            log.info("add");
            map.remove(OPERATION_TYPE);
            //如果表是视频表的话
            if(map.get(TABLE_NAME).equals(VIDEO_TABLE_NAME)){
                log.info("video");
                map.remove(TABLE_NAME);
                objectRedisTemplate.opsForList().rightPush(VIDEO_ADD_KEY,map);
            }else {
                map.remove(TABLE_NAME);
                objectRedisTemplate.opsForList().rightPush(USER_ADD_KEY,map);
            }

        }

        else if(map.get(OPERATION_TYPE).equals(OPERATION_TYPE_DELETE)){
            log.info("delete");
            map.remove(OPERATION_TYPE);
                map.remove(TABLE_NAME);
                objectRedisTemplate.opsForList().rightPush(VIDEO_DELETE_KEY,map);

        }

        else{
            log.info("update");
            map.remove(OPERATION_TYPE);
            if(map.get(TABLE_NAME).equals(VIDEO_TABLE_NAME)){
                log.info("video");
                map.remove(OPERATION_TYPE);
                objectRedisTemplate.opsForList().rightPush(VIDEO_UPDATE_KEY,map);
            }
            else{
                map.remove(TABLE_NAME);
                objectRedisTemplate.opsForList().rightPush(USER_UPDATE_KEY,map);
            }
        }

    }
}
