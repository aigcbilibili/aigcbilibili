package ljl.bilibili.chat.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ljl.bilibili.chat.entity.ChatMessage;
import ljl.bilibili.chat.event.MessageEvent;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.chat.Chat;
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
    public static volatile ConcurrentMap<String, String> USERID_TO_SESSIONID_MAP = new ConcurrentHashMap<>();
    public static volatile ConcurrentMap<String, BigModelHandler> BIGMODEL_MAP = new ConcurrentHashMap<>();
    @Resource
    public SendNoticeClient client;

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
        JsonObject json = JsonParser.parseString(message.getPayload()).getAsJsonObject();
        String type = json.get(MESSAGE_TYPE).getAsString();
        switch (type) {
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
            case MESSAGE_TYPE_INIT:
                log.info("初始化");
                String sessionId = json.get(MESSAGE_TYPE_SESSIONID).getAsString();
                String userId = json.get(USER_IDENTITY).getAsString();
                USERID_TO_SESSIONID_MAP.put(userId, sessionId);
                WEB_SOCKET_SESSION_CONCURRENT_MAP.put(sessionId, session);
                break;
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
                WebSocketSession receiverSession = WEB_SOCKET_SESSION_CONCURRENT_MAP.get(USERID_TO_SESSIONID_MAP.get(receiverId));
                if (receiverSession != null) {
                    JsonObject jsonText = new JsonObject();
                    jsonText.addProperty(MESSAGE_TYPE, MESSAGE_TYPE_MESSAGE);
                    jsonText.addProperty(MESSAGE_CONTENT, content);
                    receiverSession.sendMessage(new TextMessage(jsonText.toString()));
                } else {
                    Chat chat = new Chat().setContent(content).setSenderId(Integer.valueOf(json.get(USER_IDENTITY).getAsString())).setReceiverId(Integer.valueOf(receiverId));
                    client.sendChatNotice(chat);
                }
                break;
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
        WEB_SOCKET_SESSION_CONCURRENT_MAP.remove(session.getId());
    }
}
