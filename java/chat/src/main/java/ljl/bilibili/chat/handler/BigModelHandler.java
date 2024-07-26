package ljl.bilibili.chat.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ljl.bilibili.chat.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.TextMessage;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static ljl.bilibili.chat.constant.Constant.*;
import static ljl.bilibili.chat.handler.WebSocketHandler.USERID_TO_SESSIONID_MAP;
import static ljl.bilibili.chat.handler.WebSocketHandler.WEB_SOCKET_SESSION_CONCURRENT_MAP;

/**
 * 大模型
 */
@Slf4j
public class BigModelHandler extends WebSocketListener {
    public List<RoleContent> historyList = new ArrayList<>();

    public String totalAnswer = "";

    public String NewQuestion = "";
    public static final Gson gson = new Gson();
    private String userId;
    private ApplicationEventPublisher applicationEventPublisher;

    public BigModelHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    /**
     *将大模型的响应作为事件发布
     */
//    public void publishMessage(String message) {
//        log.info("publish");
//           MessageEvent messageEvent = new MessageEvent(this, message);
//           applicationEventPublisher.publishEvent(messageEvent);
//    }

    /**
     * 向大模型提问
     */
    public void send(String text, String id) throws Exception {
        userId = id;
        System.out.print("我：");
        NewQuestion = text;
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        totalAnswer = "";
        WebSocket webSocket = client.newWebSocket(request, this);
        run(webSocket);
    }

    /**
     * 若会话记录过长则删除前面一部分
     */
    public boolean canAddHistory() {
        int history_length = 0;
        for (RoleContent temp : historyList) {
            history_length = history_length + temp.content.length();
        }
        if (history_length > 12000) {
            historyList.remove(0);
            historyList.remove(1);
            historyList.remove(2);
            historyList.remove(3);
            historyList.remove(4);
            return false;
        } else {
            return true;
        }
    }


    /**
     * 将与大模型的连接传递给发送消息方法
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        System.out.print("大模型：");
    }

    /**
     * 监听大模型响应并发布事件与记录对应用户会话内容
     */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            log.info("线程1");
            JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
            Integer status = myJsonParse.header.status;
            if (myJsonParse.header.code != 0) {

                System.out.println("发生错误，错误码为：" + myJsonParse.header.code);
                System.out.println("本次请求的sid为：" + myJsonParse.header.sid);
                webSocket.close(1000, "");
            }
            List<Text> textList = myJsonParse.payload.choices.text;
            for (Text temp : textList) {
                ChatMessage chatMessage = new ChatMessage(temp.content, userId, status);
                log.info(temp.content);
                JsonObject jsonText = new JsonObject();
                jsonText.addProperty(MESSAGE_STATUS, chatMessage.getStatus());
                jsonText.addProperty(MESSAGE_TYPE, MESSAGE_TYPE_BIGMODEL);
                jsonText.addProperty(MESSAGE_CONTENT, chatMessage.getContent());
                try {
                    WEB_SOCKET_SESSION_CONCURRENT_MAP.get(USERID_TO_SESSIONID_MAP.get(chatMessage.getUserId())).sendMessage(new TextMessage(jsonText.toString()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                    publishMessage(jsonChat);
//                });
                totalAnswer = totalAnswer + temp.content;
            }
            if (myJsonParse.header.status == 2) {
                // 可以关闭连接，释放资源
                System.out.println();
                System.out.println("*************************************************************************************");
                if (canAddHistory()) {
                    RoleContent roleContent = new RoleContent();
                    roleContent.setRole(ASSISTANT_ROLE);
                    roleContent.setContent(totalAnswer);

                    historyList.add(roleContent);
                } else {
                    historyList.remove(0);
                    RoleContent roleContent = new RoleContent();
                    roleContent.setRole(ASSISTANT_ROLE);
                    roleContent.setContent(totalAnswer);
                    historyList.add(roleContent);
                }
                totalAnswer = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                System.out.println("onFailure code:" + code);
                System.out.println("onFailure body:" + response.body().string());
                if (101 != code) {
                    System.out.println("connection failed");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 鉴权
     */
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URI uri = new URI(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        String preStr = "host: " + uri.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + uri.getPath() + " HTTP/1.1";
        String encryptType = "hmacsha256";
        Mac mac = Mac.getInstance(encryptType);
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), encryptType);
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        String query = String.format("authorization=%s&date=%s&host=%s",
                URLEncoder.encode(Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)), "UTF-8"),
                URLEncoder.encode(date, "UTF-8"),
                URLEncoder.encode(uri.getHost(), "UTF-8"));

        return "wss://" + uri.getHost() + uri.getPath() + "?" + query;
    }

    class JsonParse {
        Header header;
        Payload payload;
    }

    class Header {
        int code;
        int status;
        String sid;

        public int getStatus() {
            return status;
        }
    }

    class Payload {
        Choices choices;
    }

    class Choices {
        List<Text> text;
    }

    class Text {
        String role;
        String content;
    }

    class RoleContent {
        String role;
        String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 封装请求json发送给大模型
     */
    public void run(WebSocket webSocket) {
        try {
            JSONObject requestJson = new JSONObject();

            JSONObject header = new JSONObject();
            header.put(MODEL_APP_ID, appId);
            header.put(SESSION_UID, UUID.randomUUID().toString().substring(0, 10));

            JSONObject parameter = new JSONObject();
            JSONObject chat = new JSONObject();
            chat.put(MODEL_DOMAIN, MODEL_DOMAIN_VALUE);
            chat.put(MODEL_TEMPERATURE, 1.0);
            chat.put(MODEL_MAX_TOKENS, 8000);
            parameter.put(MODEL_PARAMETER, chat);

            JSONObject payload = new JSONObject();
            JSONObject message = new JSONObject();
            JSONArray text = new JSONArray();

            if (historyList.size() > 0) {
                for (RoleContent tempRoleContent : historyList) {
                    text.add(JSON.toJSON(tempRoleContent));
                }
            }

            RoleContent roleContent = new RoleContent();
            roleContent.role = USER_ROLE;
            roleContent.content = NewQuestion;
            log.info(NewQuestion);
            text.add(JSON.toJSON(roleContent));
            historyList.add(roleContent);
            message.put(MESSAGE_TEXT, text);
            payload.put(PAYLOAD_MESSAGE, message);
            requestJson.put(REQUEST_HEADER, header);
            requestJson.put(REQUEST_PARAMTER, parameter);
            requestJson.put(REQUEST_PAYLOAD, payload);
            webSocket.send(requestJson.toString());

            while (true) {
                Thread.sleep(200);
                break;
            }
            webSocket.close(1000, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空用户会话内容
     */
    public Boolean removeSession() {
        for (int i = 0; i < historyList.size(); i++) {
            historyList.remove(i);
        }
        return true;
    }
}