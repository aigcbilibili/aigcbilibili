package ljl.bilibili.video.vo.request.video_production;

import lombok.Data;

@Data
public class DeleteVideoRequest {
    private int userId;
    private int videoId;
}
