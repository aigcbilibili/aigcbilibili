package ljl.bilibili.video.vo.request.video_production;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadPartRequest {
    MultipartFile file;
    Integer resumableTotalChunks;
    String resumableIdentifier;
    Integer resumableChunkNumber;
}
