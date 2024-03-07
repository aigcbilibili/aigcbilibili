package ljl.bilibili.video.controller.audience_reactions.comment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.comment.CommentService;
import ljl.bilibili.video.service.audience_reactions.comment.impl.CommentServiceImpl;
import ljl.bilibili.video.vo.request.audience_reactions.comment.CommentRequest;
import ljl.bilibili.video.vo.response.audience_reactions.comment.VideoCommentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
@RestController
@Api(tags = "评论和查看视频评论")
@RequestMapping("/comment")
@CrossOrigin(value = "*")
@Slf4j
public class CommentController {
    @Resource
    CommentService commentService;
    @PostMapping("/comment")
    @ApiOperation("发送对视频或评论的评论")
    public Result<Boolean> comment(@RequestBody CommentRequest comment) {
        log.info("评论评论");
        log.info(comment.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return commentService.commentToComment(comment);
    }


    @GetMapping("/getComment/{videoId}/{userId}/{type}")
    @ApiOperation("获取视频下的评论")
    public Result<VideoCommentResponse> getComment(@PathVariable Integer videoId, @PathVariable Integer userId,
                                                   @PathVariable Integer type) {
        log.info("获取评论");
        log.info(userId.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return commentService.getComment(videoId,userId,type);
    }
}
