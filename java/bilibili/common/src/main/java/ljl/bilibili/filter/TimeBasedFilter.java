package ljl.bilibili.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.extern.slf4j.Slf4j;
/**
 * 自定义日志过滤器，实现间隔时间较长的调用可以空行，方便日志定位
 */
@Slf4j
public class TimeBasedFilter extends Filter<ILoggingEvent> {

    private static final long FIVE_MINUTES_MILLIS = 5 * 60 * 1000;

    private static long lastLogTimestamp;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        long currentTime = event.getTimeStamp();
        if(lastLogTimestamp==0){
            lastLogTimestamp = currentTime;
        }
        if(currentTime - lastLogTimestamp > FIVE_MINUTES_MILLIS) {
            log.info("\n\n\n\n\n\n");
            lastLogTimestamp = currentTime;
        }
        return FilterReply.NEUTRAL;
    }
}
