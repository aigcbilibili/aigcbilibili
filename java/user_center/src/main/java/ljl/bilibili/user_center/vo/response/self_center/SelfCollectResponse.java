package ljl.bilibili.user_center.vo.response.self_center;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SelfCollectResponse {
    private Integer videoId;
    private String videoCover;
    private Integer authorId;
    private String authorName;
    private String videoName;
    private LocalDateTime createTime;
}
