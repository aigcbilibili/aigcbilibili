package ljl.bilibili.video.controller.video_production;
import ljl.bilibili.client.pojo.UploadVideo;
import ljl.bilibili.video.service.video_production.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.Resource;

import static ljl.bilibili.video.constant.Constant.*;

@RequestMapping("/videoEncode")
@Controller
public class VideoEncodeController {
    @Autowired
    MinioService minioService;

    @ApiIgnore
    @PostMapping("/getVideoInputStream")
    public ResponseEntity<Resource>  getVideoInputStream(@RequestBody UploadVideo uploadVideo){
        String url=uploadVideo.getUrl();
        InputStream inputStream = minioService.getObject(url.substring(url.lastIndexOf("/")+1));
        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, HEADERS_VALUES)
                .body(resource);
    }
    @ApiIgnore
    @PostMapping(value = "/uploadVideo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public void uploadVideo(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
         minioService.uploadVideoFile(multipartFile.getOriginalFilename(),multipartFile.getInputStream(),VIDEO_TYPE);
    }
    @ApiIgnore
    @PostMapping(value = "/uploadVideoCover",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public void uploadVideoCover(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        minioService.uploadImgFile(multipartFile.getOriginalFilename(),multipartFile.getInputStream(),IMAGE_TYPE);
    }

}
