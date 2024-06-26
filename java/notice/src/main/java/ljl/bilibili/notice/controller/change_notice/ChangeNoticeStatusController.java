package ljl.bilibili.notice.controller.change_notice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.notice.service.change_notice.ChangeNoticeStatusService;
import ljl.bilibili.notice.service.change_notice.impl.ChangeNoticeStatusServiceImpl;
import ljl.bilibili.util.Result;
import ljl.bilibili.notice.vo.request.change_notice.CommentNoticeStatusChangeRequest;
import ljl.bilibili.notice.vo.request.change_notice.DynamicVideoStatusChangeRequest;
import ljl.bilibili.notice.vo.request.change_notice.LikeNoticeStatusChangeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "修改未读评论、点赞状态")
@RestController
@RequestMapping("/changeNoticeStatus")
public class ChangeNoticeStatusController {
    @Resource
    ChangeNoticeStatusService changeNoticeStatusService;
    @PostMapping("/changeLikeNoticeStatus")
    @ApiOperation("修改点赞未读状态为已读")
    public Result<Boolean> changeLikeNoticeStatus(@RequestBody LikeNoticeStatusChangeRequest likeNoticeStatusChangeRequest){
        return changeNoticeStatusService.changeLikeNoticeStatus(likeNoticeStatusChangeRequest);
    }
    @PostMapping("/changeCommentNoticeStatus")
    @ApiOperation("修改评论未读状态为已读")
    public Result<Boolean> changeCommentNoticeStatus(@RequestBody CommentNoticeStatusChangeRequest commentNoticeStatusChangeRequest){
        return changeNoticeStatusService.changeCommentNoticeStatus(commentNoticeStatusChangeRequest);
    }

    @ApiOperation("修改动态视频状态为已读")
    @PostMapping("/changeDynamicVideoStatus")
    public Result<Boolean> changeDynamicVideoStatus(@RequestBody DynamicVideoStatusChangeRequest dynamicVideoStatusChangeRequest) {
        log.info("增加已读动态视频");
        log.info(dynamicVideoStatusChangeRequest.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return changeNoticeStatusService.changeDynamicVideoStatus(dynamicVideoStatusChangeRequest);
    }
}
