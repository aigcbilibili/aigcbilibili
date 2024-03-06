package ljl.bilibili.video.controller.video_production;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.video_production.UploadAndEditService;
import ljl.bilibili.video.vo.request.video_production.DeleteVideoRequest;
import ljl.bilibili.video.vo.request.video_production.EditVideoRequest;
import ljl.bilibili.video.vo.request.video_production.UploadVideoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/createCenter")
@Api(tags = "上传视频和编辑视频")
@CrossOrigin(value = "*")
@Slf4j
public class UploadAndEditController {
    @Resource
    UploadAndEditService uploadAndEditService;
    @ApiOperation("上传视频")
    @PostMapping("upload")
    public Result<Boolean> upload(@ModelAttribute UploadVideoRequest uploadVideoRequest) throws IOException {
        log.info("上传视频");
        return uploadAndEditService.upload(uploadVideoRequest);
    }
    @ApiOperation("获取视频前三帧图片")
    @PostMapping("/getVideoCover")
    public Result<String> getVideoCover(@RequestParam("file")MultipartFile multipartFile) throws EncoderException, IOException {
        return uploadAndEditService.getVideoCover(multipartFile);
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
}
