package ljl.bilibili.video.controller.audience_reactions.danmaku;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.danmaku.DanmakuService;
import ljl.bilibili.video.service.audience_reactions.danmaku.impl.DanmakuServiceImpl;
import ljl.bilibili.video.vo.request.audience_reactions.danmaku.AddDanmakuRequest;
import ljl.bilibili.video.vo.response.audience_reactions.danmaku.DanmakuResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "发送和获取视频弹幕")
@RequestMapping("/danmaku")
@CrossOrigin(value = "*")
@Slf4j
public class DanmakuController {
    @Resource
    DanmakuService danmakuService;
    @GetMapping("/getDanmaku/{videoId}")
    @ApiOperation("获取视频弹幕")
    public Result<List<DanmakuResponse>> getDanmaku(@PathVariable Integer videoId) {
        log.info("获取视频弹幕");
        log.info(videoId.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return danmakuService.getDanmaku(videoId);
    }
    @ApiOperation("发送弹幕")
    @PostMapping("/addDanmaku")
    public Result<Boolean> addDanmaku(@RequestBody AddDanmakuRequest addDanmakuRequest) {
        log.info("发送视频弹幕");
        log.info(addDanmakuRequest.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
      return danmakuService.addDanmaku(addDanmakuRequest);

    }
}
