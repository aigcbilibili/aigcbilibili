package ljl.bilibili.chat.event;

import org.springframework.context.ApplicationEvent;
/**
 *继承ApplicationEvent用于封装消息
 */

public class MessageEvent extends ApplicationEvent {
    private String message;

    public MessageEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
