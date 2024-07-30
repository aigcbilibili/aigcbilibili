package ljl.bilibili.video.controller.video_production;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.video_production.MinioService;
import ljl.bilibili.video.service.video_production.UploadAndEditService;
import ljl.bilibili.video.vo.request.video_production.DeleteVideoRequest;
import ljl.bilibili.video.vo.request.video_production.EditVideoRequest;
import ljl.bilibili.video.vo.request.video_production.UploadPartRequest;
import ljl.bilibili.video.vo.request.video_production.UploadVideoRequest;
import ljl.bilibili.video.vo.response.video_production.UploadProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/createCenter")
@Api(tags = "上传视频和编辑视频")
@CrossOrigin(value = "*")
@Slf4j
public class UploadAndEditController {
    @Resource
    UploadAndEditService uploadAndEditService;

    @ApiOperation("上传视频")
    @PostMapping("/uploadTotal")
    public Result<Boolean> uploadTotal(@RequestBody UploadVideoRequest uploadVideoRequest) throws IOException {
        log.info("上传视频");
        return uploadAndEditService.uploadTotal(uploadVideoRequest);
    }
    @ApiOperation("上传分片并获取视频第一帧图片和合并后的路径")
    @PostMapping("/uploadPart")
    public Result<List<String>> uploadPart(@ModelAttribute UploadPartRequest uploadPartRequest) throws EncoderException, IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return uploadAndEditService.uploadPart(uploadPartRequest);
    }
    @ApiOperation("编辑视频")
    @PostMapping("edit")
    public Result<Boolean> edit(@ModelAttribute EditVideoRequest editVideoRequest){
        log.info("1");
        return uploadAndEditService.edit(editVideoRequest);
    }
    @ApiOperation("删除视频")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody DeleteVideoRequest deleteVideoRequest){
        log.info("1");
        return uploadAndEditService.delete(deleteVideoRequest);
    }

    @ApiOperation("查询进度")
    @GetMapping("/getProcessor")
    public ResponseEntity<Result<Boolean>> getProcessor(@RequestParam("resumableIdentifier") String resumableIdentifier,@RequestParam("resumableChunkNumber")Integer resumableChunkNumber) {
        return uploadAndEditService.getProcessor(resumableIdentifier,resumableChunkNumber);
    }
}
