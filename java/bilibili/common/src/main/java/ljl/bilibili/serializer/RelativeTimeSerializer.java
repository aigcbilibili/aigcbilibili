package ljl.bilibili.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
/**
 *自定义时间格式序列化器，将localdatetime格式转成几小时前、几天前
 */
public class RelativeTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formatted = formatRelativeTime(value);
        gen.writeString(formatted);
    }

    private String formatRelativeTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        if (days > 2) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return dateTime.format(formatter).replace("T"," ");
        } else if (days >= 1) {
            return days + "天前";
        } else if (hours >= 1) {
            return hours + "小时前";
        } else if (minutes >= 1) {
            return minutes + "分钟前";
        } else {
            return "刚刚";
        }
    }
}
