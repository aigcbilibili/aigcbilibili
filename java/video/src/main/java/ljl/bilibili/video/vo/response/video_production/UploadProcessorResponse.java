package ljl.bilibili.video.vo.response.video_production;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UploadProcessorResponse {
    private double percent;
    private List<Integer> uploadedIndexList;
}
