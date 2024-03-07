package ljl.bilibili.client.video;

import ljl.bilibili.client.pojo.UploadVideo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(name = "video",url = "http://localhost:10201")
public interface VideoClient {
    @PostMapping(value = "/videoEncode/uploadVideo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void uploadVideo(@RequestPart("multipartFile") MultipartFile multipartFile);
    @PostMapping("/videoEncode/getVideoInputStream")
    ResponseEntity<Resource> getVideo(@RequestBody UploadVideo uploadVideo);
    @PostMapping(value = "/videoEncode/uploadVideoCover",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void uploadVideoCover(@RequestPart("multipartFile") MultipartFile multipartFile);
}
