package ljl.bilibili.search.vo.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class VideoKeywordSearchResponse {
    Integer videoId;
    Integer authorId;
    String videoName;
    String authorName;
    String intro;
    String cover;
    LocalDateTime createTime;
    String url;
    String length;
    Integer playCount;
    Integer danmakuCount;
    Integer collectCount;
}
