package ljl.bilibili.client.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendVideo {
    @JsonProperty("video_id")
    Integer videoId;
    @JsonProperty("author_id")
    Integer authorId;
    @JsonProperty("video_name")
    String videoName;
    @JsonProperty("author_name")
    String authorName;
    String intro;
    @JsonProperty("create_time")
    LocalDateTime createTime;
    String url;
    String length;
    String cover;
    @JsonProperty("play_count")
    Integer playCount;
    @JsonProperty("danmaku_count")
    Integer danmakuCount;
    @JsonProperty("collect_count")
    Integer collectCount;
}
