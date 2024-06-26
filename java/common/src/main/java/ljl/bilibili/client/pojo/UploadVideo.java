package ljl.bilibili.client.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploadVideo {
    private Integer videoId;
    private String url;
    private String videoName;
    private Boolean hasCover;
}
