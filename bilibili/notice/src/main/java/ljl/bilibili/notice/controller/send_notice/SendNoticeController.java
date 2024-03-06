package ljl.bilibili.notice.controller.send_notice;
import com.fasterxml.jackson.core.JsonProcessingException;
import ljl.bilibili.client.pojo.LikeNoticeAddOrDelete;
import ljl.bilibili.client.pojo.UploadVideo;
import ljl.bilibili.notice.service.send_notice.SendNoticeService;
import ljl.bilibili.notice.service.send_notice.impl.SendNoticeServiceImpl;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.notice.dynamic.Dynamic;
import ljl.bilibili.entity.notice.comment.CommentNotice;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@RestController
@RequestMapping("/notice")

public class SendNoticeController {
    @Resource
    SendNoticeService sendNoticeService;
    @PostMapping("/sendDynamicNotice")
    @ApiIgnore
    public Boolean dynamicNotice(@RequestBody Dynamic dynamic) throws JsonProcessingException {
        sendNoticeService.sendVideoUpdateMessageWithCallback(dynamic);
        return true;
    }
    @PostMapping("/sendLikeNotice")
    @ApiIgnore
    public void videolikeNotice(@RequestBody LikeNoticeAddOrDelete likeNotice) throws JsonProcessingException {
        sendNoticeService.sendVideoLikeMessageWithCallback(likeNotice);
    }
    @ApiIgnore
    @PostMapping("/sendCommentNotice")
    public void commentNotice(@RequestBody CommentNotice commentNotice) throws JsonProcessingException {
        sendNoticeService.sendCommentMessageWithCallback(commentNotice);
    }
    @ApiIgnore
    @PostMapping("/sendChatNotice")
    public void chatNotice(@RequestBody Chat chat) throws JsonProcessingException {
        sendNoticeService.sendChatMessageWithCallback(chat);
    }
    @ApiIgnore
    @PostMapping("/sendUploadNotice")
    public Boolean sendUploadNotice(@RequestBody UploadVideo uploadVideo) throws JsonProcessingException {
        return sendNoticeService.sendUploadNotice(uploadVideo);
    }

}
