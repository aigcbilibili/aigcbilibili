package ljl.bilibili.video.controller.audience_reactions.like;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.like.LikeService;
import ljl.bilibili.video.service.audience_reactions.like.impl.LikeServiceImpl;
import ljl.bilibili.video.vo.request.audience_reactions.like.LikeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/like")
@Api(tags = "对视频和评论的点赞")
@CrossOrigin(value = "*")
@Slf4j
public class LikeController {
    @Resource
    LikeService likeService;

    @ApiOperation("点赞视频或评论")
    @PostMapping("/like")
    public Result<Boolean> like(@RequestBody LikeRequest likeRequest) {
        log.info("点赞评论");
        log.info(likeRequest.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return likeService.like(likeRequest);
    }

    @PostMapping("/reCallLike")
    @ApiOperation("取消点赞视频或评论")
    public Result<Boolean> recallLike(@RequestBody LikeRequest likeRequest) {
        log.info("取消点赞评论");
        log.info(likeRequest.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return likeService.recallLike(likeRequest);

    }
}
