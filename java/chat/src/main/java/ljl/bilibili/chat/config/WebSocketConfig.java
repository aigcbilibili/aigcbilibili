package ljl.bilibili.chat.config;

import ljl.bilibili.chat.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

import static ljl.bilibili.chat.constant.Constant.WEBSOCKET_CONNECT_PATH;
/**
 *Websocket端点注册与设置跨域
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    WebSocketHandler webSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, WEBSOCKET_CONNECT_PATH).setAllowedOrigins("*");
    }

}

