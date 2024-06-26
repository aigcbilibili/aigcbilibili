package ljl.bilibili.notice.service.send_notice;

import com.fasterxml.jackson.core.JsonProcessingException;
import ljl.bilibili.client.pojo.LikeNoticeAddOrDelete;
import ljl.bilibili.client.pojo.UploadVideo;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.notice.comment.CommentNotice;
import ljl.bilibili.entity.notice.dynamic.Dynamic;

public interface SendNoticeService {
    Boolean sendVideoUpdateMessageWithCallback(Dynamic dynamic) throws JsonProcessingException;
    Boolean sendVideoLikeMessageWithCallback(LikeNoticeAddOrDelete likeNotice) throws JsonProcessingException;
    Boolean sendCommentMessageWithCallback(CommentNotice commentNotice) throws JsonProcessingException;
    Boolean sendChatMessageWithCallback(Chat chat) throws JsonProcessingException;
    Boolean sendUploadNotice(UploadVideo uploadVideo)throws JsonProcessingException;
}
