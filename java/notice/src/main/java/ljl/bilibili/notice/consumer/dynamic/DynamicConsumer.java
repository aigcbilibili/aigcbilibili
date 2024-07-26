package ljl.bilibili.notice.consumer.dynamic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.entity.notice.dynamic.Dynamic;
import ljl.bilibili.entity.notice.dynamic.DynamicToUser;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import ljl.bilibili.mapper.notice.dynamic.DynamicMapper;
import ljl.bilibili.mapper.notice.dynamic.DynamicToUserMapper;
import ljl.bilibili.mapper.user_center.user_relationships.FollowMapper;
import ljl.bilibili.notice.service.send_notice.DynamicToUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/**
 *动态消息消费者
 */
@Slf4j

@Service
@RocketMQMessageListener(
        topic = "dynamic",
        consumerGroup = "dynamic-group",
        consumeMode = ConsumeMode.CONCURRENTLY
)
public class DynamicConsumer implements RocketMQListener<MessageExt> {
    @Resource
    ObjectMapper objectMapper;
    @Resource
    DynamicMapper dynamicMapper;
    @Resource
    FollowMapper followMapper;
    @Autowired
    DynamicToUserMapper dynamicToUserMapper;
    @Resource
    DynamicToUserService dynamicToUserService;
    /**
     *生成动态与推送至用户的动态
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        String json = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        Dynamic dynamic;
        try {
            dynamic = objectMapper.readValue(json, Dynamic.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info(dynamic.getVideoId().toString());
        dynamicMapper.insert(dynamic);
        //查询出该动态会推送给哪些人然后插入记录到中间联系表中
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getIdolId, dynamic.getAuthorId());
        List<Follow> list = followMapper.selectList(wrapper);
        List<DynamicToUser> dynamicToUserList=new ArrayList<>();
        for (Follow follow : list) {
            dynamicToUserList.add(new DynamicToUser().setUserId(follow.getFansId()).setDynamicId(dynamic.getId()));
        }
        dynamicToUserService.saveBatch(dynamicToUserList);
    }
}
