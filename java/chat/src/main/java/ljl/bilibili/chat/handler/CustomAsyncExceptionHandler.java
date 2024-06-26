package ljl.bilibili.chat.handler;

import lombok.extern.slf4j.Slf4j;
/**
 *全局异步异常处理器
 */
@Slf4j
public class CustomAsyncExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.info("exception in thread '" + t.getName() + "': " + e.getMessage());
    }
}


