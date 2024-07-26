package ljl.bilibili.chat.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ljl.bilibili.chat.entity.ChatMessage;
import ljl.bilibili.chat.event.MessageEvent;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.mapper.chat.ChatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static ljl.bilibili.chat.constant.Constant.*;
/**
 *私聊与大模型处理器
 */
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    @Resource
    ApplicationEventPublisher applicationEventPublisher;
    public static final Gson gson = new Gson();
    public static volatile ConcurrentMap<String, WebSocketSession> WEB_SOCKET_SESSION_CONCURRENT_MAP = new ConcurrentHashMap<>();
    public static  Map<String, String> USERID_TO_SESSIONID_MAP = new ConcurrentHashMap<>();
    public static volatile ConcurrentMap<String, BigModelHandler> BIGMODEL_MAP = new ConcurrentHashMap<>();
    @Resource
    ChatMapper chatMapper;

    @EventListener
    @Async
    /**
     *监听大模型响应事件并返回相应客户端
     */
    public void handleMessageEvent(MessageEvent event) throws IOException {
        ChatMessage message = gson.fromJson(event.getMessage(), ChatMessage.class);
        JsonObject jsonText = new JsonObject();
        jsonText.addProperty(MESSAGE_STATUS, message.getStatus());
        jsonText.addProperty(MESSAGE_TYPE, MESSAGE_TYPE_BIGMODEL);
        jsonText.addProperty(MESSAGE_CONTENT, message.getContent());
        WEB_SOCKET_SESSION_CONCURRENT_MAP.get(USERID_TO_SESSIONID_MAP.get(message.getUserId())).sendMessage(new TextMessage(jsonText.toString()));
    }
    /**
     *根据type处理接收的私聊与大模型消息
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //将消息json化
        JsonObject json = JsonParser.parseString(message.getPayload()).getAsJsonObject();
        String type = json.get(MESSAGE_TYPE).getAsString();
        //接收消息后根据消息类型来处理
        switch (type) {
            //若是大模型则是将消息中的提问部分发送给大模型然后获取到大模型响应后返回给客户端
            case MESSAGE_TYPE_BIGMODEL:
                String question = json.get(MESSAGE_TYPE_BIGMODEL_QUESTION).getAsString();
                String id = json.get(USER_IDENTITY).getAsString();
                if (BIGMODEL_MAP.get(id) != null) {
                    BIGMODEL_MAP.get(id).send(question, id);
                } else {
                    BigModelHandler bigModelHandler = new BigModelHandler(applicationEventPublisher);
                    BIGMODEL_MAP.put(id, bigModelHandler);
                    bigModelHandler.send(question, id);
                }
                break;
                //若是初始化则是将userId和sessionId的映射存到map里
            case MESSAGE_TYPE_INIT:
                log.info("初始化");
                String sessionId = json.get(MESSAGE_TYPE_SESSIONID).getAsString();
                String userId = json.get(USER_IDENTITY).getAsString();
                USERID_TO_SESSIONID_MAP.put(userId, sessionId);
                WEB_SOCKET_SESSION_CONCURRENT_MAP.put(sessionId, session);
                break;
                //若是接收消息则根据两个映射map和传来的userId把消息转发给要接收消息的客户端
            case MESSAGE_TYPE_MESSAGE:
                String receiverId = json.get(RECEIVER_IDENTITY).getAsString();
                JsonElement jsonElement = json.get(MESSAGE_CONTENT);
                String content;
                if (jsonElement.isJsonPrimitive()) {
                    content = jsonElement.getAsString();
                    log.info("json");
                } else {
                    content = jsonElement.toString();
                    log.info("string");
                }
                log.info(String.valueOf(WEB_SOCKET_SESSION_CONCURRENT_MAP.size()));
                for (Map.Entry<String, WebSocketSession> entry : WEB_SOCKET_SESSION_CONCURRENT_MAP.entrySet()) {
                    log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
                for (Map.Entry<String, String> entry : USERID_TO_SESSIONID_MAP.entrySet()) {
                    log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
                log.info(String.valueOf(USERID_TO_SESSIONID_MAP.size()));
                //如果该用户当前在线则直接发送websocket消息
                if (USERID_TO_SESSIONID_MAP.get(receiverId) != null) {
                    JsonObject jsonText = new JsonObject();
                    jsonText.addProperty(MESSAGE_TYPE, MESSAGE_TYPE_MESSAGE);
                    jsonText.addProperty(MESSAGE_CONTENT, content);
                    jsonText.addProperty("senderId", json.get(USER_IDENTITY).getAsString());
                    try{
                        WEB_SOCKET_SESSION_CONCURRENT_MAP.get(USERID_TO_SESSIONID_MAP.get(receiverId)).sendMessage(new TextMessage(jsonText.toString()));
                    }catch (Exception e){
                    e.printStackTrace();
                    }
                    //若不在线则直接生成消息通知
                } else {
                    Chat chat = new Chat().setContent(content).setSenderId(Integer.valueOf(json.get(USER_IDENTITY).getAsString())).setReceiverId(Integer.valueOf(receiverId));
                    chatMapper.insert(chat);
                }
                break;
                //若是移除映射消息则移除之前存储的userId到sessionId的映射
            case MESSAGE_TYPE_REMOVE_SESSION:
                String chatToBigModelUserId = json.get(USER_IDENTITY).getAsString();
                BIGMODEL_MAP.get(chatToBigModelUserId).removeSession();
                break;
                    default:
                break;
        }
    }
    /**
     *后端返回给前端的第一条WebSocket消息，携带客户端sessionId
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //将客户端的sessionId和websocket对象绑定到一起
        WEB_SOCKET_SESSION_CONCURRENT_MAP.put(session.getId(), session);
        JsonObject json = new JsonObject();
        json.addProperty(MESSAGE_TYPE, MESSAGE_TYPE_SESSIONID);
        json.addProperty(MESSAGE_TYPE_SESSIONID, session.getId());
        session.sendMessage(new TextMessage(json.toString()));
        log.info("连接成功");
    }
    /**
     *关闭连接后及时从服务端移除会话
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        //将用户id和websocket对象的映射从map中移除
        log.info("连接关闭");
        for(Map.Entry<String,String> entry : USERID_TO_SESSIONID_MAP.entrySet()){
            if(entry.getValue().equals(session.getId())){
                USERID_TO_SESSIONID_MAP.remove(entry.getKey());
                log.info("已删除过期连接");
            }
        }
        WEB_SOCKET_SESSION_CONCURRENT_MAP.remove(session.getId());
        log.info("已删除过期连接");
    }
}
