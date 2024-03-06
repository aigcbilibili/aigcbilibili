package ljl.bilibili.video.service.video_production;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.vo.request.video_production.DeleteVideoRequest;
import ljl.bilibili.video.vo.request.video_production.EditVideoRequest;
import ljl.bilibili.video.vo.request.video_production.UploadVideoRequest;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;

public interface UploadAndEditService {
Result<Boolean> upload(UploadVideoRequest uploadVideoRequest) throws IOException;

Result<Boolean> edit(EditVideoRequest editVideoRequest);
Result<Boolean> delete(DeleteVideoRequest deleteVideoRequest);

Result<String> getVideoCover(MultipartFile multipartFile) throws IOException, EncoderException;
}
