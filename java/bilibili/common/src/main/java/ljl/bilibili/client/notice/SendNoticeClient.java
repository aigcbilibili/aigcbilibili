package ljl.bilibili.client.notice;

import ljl.bilibili.client.pojo.LikeNoticeAddOrDelete;
import ljl.bilibili.client.pojo.UploadVideo;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.notice.comment.CommentNotice;
import ljl.bilibili.entity.notice.dynamic.Dynamic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notice",url="http://localhost:30000")
@Component
public interface SendNoticeClient {
    @PostMapping("/notice/sendDynamicNotice")
     Boolean dynamicNotice(@RequestBody Dynamic dynamic);
    @PostMapping("/DBChange/sendDBChangeNotice")
    Boolean sendDBChangeNotice(@RequestBody Map<String,Object> map);
    @PostMapping("/notice/sendLikeNotice")
    Boolean sendLikeNotice(@RequestBody LikeNoticeAddOrDelete likeNotice);
    @PostMapping("/notice/sendCommentNotice")
    Boolean sendCommentNotice(@RequestBody CommentNotice commentNotice);
    @PostMapping("/notice/sendChatNotice")
    Boolean sendChatNotice(@RequestBody Chat chat);
    @PostMapping("/notice/sendUploadNotice")
     Boolean sendUploadNotice(@RequestBody UploadVideo uploadVideo);
}
