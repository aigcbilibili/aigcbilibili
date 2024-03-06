package ljl.bilibili.handler;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletResponse;

/**
 *全局异常处理器，通过读配置文件属性决定是否启用该处理器避免网关服务webflux与mvc冲突导致捕获异常时无法追踪异常位置问题
 */
@RestControllerAdvice
@Slf4j
@ConditionalOnProperty(name = "global-exception-handler.enabled", havingValue = "true", matchIfMissing = true)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception exception, HttpServletResponse httpServletResponse) {
        //获取绑定异常的结果
        httpServletResponse.setStatus(600);
        exception.printStackTrace();
        log.error("", exception);
        return Result.error("寄了," + exception.getMessage());
    }
}