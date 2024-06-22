package ljl.bilibili.user_center.controller.ensemble;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import ljl.bilibili.entity.user_center.ensemble.VideoEnsemble;
import ljl.bilibili.entity.video.audience_reactions.ensemble.AddToEnsemble;
import ljl.bilibili.user_center.service.ensemble.EnsembleService;
import ljl.bilibili.user_center.vo.request.ensemble.AddOrUpdateEnsembleRequest;
import ljl.bilibili.user_center.vo.request.ensemble.AddToEnsembleRequest;
import ljl.bilibili.user_center.vo.request.ensemble.DeleteFromEnsembleRequest;
import ljl.bilibili.user_center.vo.response.ensemble.EnsembleVideoResponse;
import ljl.bilibili.user_center.vo.response.ensemble.AllVideoInEnsembleResponse;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/ensemble")
@Api(tags = "获取和创建个人主页视频合集")
@CrossOrigin(value = "*")
@Slf4j
public class EnsembleController {
    @Resource
    EnsembleService ensembleService;

    @PostMapping("/getEnsembleList/{userId}")
    @ApiOperation("获取个人主页视频合集")
    public Result<List<VideoEnsemble>> getEnsembleList(@PathVariable Integer userId) {
        log.info("1");
        return ensembleService.getEnsembleList(userId);
    }

    @PostMapping("/addOrUpDaterEnsemble")
    @ApiOperation("创建或修改个人主页视频合集")
    public Result<Boolean> addOrUpDaterEnsemble(@RequestBody AddOrUpdateEnsembleRequest addOrUpdateEnsembleRequest) {
        log.info("1");
        return ensembleService.addOrUpdateEnsemble(addOrUpdateEnsembleRequest);
    }

    @GetMapping("/getEnsembleVideo/{userId}")

    @ApiOperation("获取个人主页视频合集中的视频")
    public Result<List<EnsembleVideoResponse>> getEnsembleVideo(@PathVariable Integer userId) {
        log.info("1");
        return ensembleService.getEnsembleVideo(userId);
    }

    @GetMapping("/getAllVideoInEnsemble/{userId}/{videoId}")
    @ApiOperation("获取视频详情页和该视频在同一up主的视频合集下的其他视频")
    public Result<AllVideoInEnsembleResponse> getAllVideoInEnsemble(@PathVariable Integer userId, @PathVariable Integer videoId) {
        return ensembleService.getAllVideoInEnsemble(userId, videoId);
    }

    @ApiOperation("把视频添加进视频合集")
    @PostMapping("/addToEnsemble")
    public Result<Boolean> addToEnsemble(@RequestBody AddToEnsembleRequest addToEnsembleRequest) {
        log.info("1");
        return ensembleService.addToEnsemble(addToEnsembleRequest);
    }

    @ApiOperation("把视频从合集中移除")
    @PostMapping("/deleteFromEnsemble")
    public Result<Boolean> deleteFromEnsemble(@RequestBody DeleteFromEnsembleRequest deleteFromEnsembleRequest) {
        log.info("1");
        return ensembleService.deleteFromEnsemble(deleteFromEnsembleRequest);
    }
}
