package ljl.bilibili.notice.service.change_notice;

import ljl.bilibili.notice.vo.request.change_notice.CommentNoticeStatusChangeRequest;
import ljl.bilibili.notice.vo.request.change_notice.DynamicVideoStatusChangeRequest;
import ljl.bilibili.notice.vo.request.change_notice.LikeNoticeStatusChangeRequest;
import ljl.bilibili.util.Result;
import org.springframework.web.bind.annotation.RequestBody;

public interface ChangeNoticeStatusService {
    Result<Boolean> changeLikeNoticeStatus(LikeNoticeStatusChangeRequest likeNoticeStatusChangeRequest);
    Result<Boolean> changeCommentNoticeStatus(CommentNoticeStatusChangeRequest commentNoticeStatusChangeRequest);
    Result<Boolean> changeDynamicVideoStatus(DynamicVideoStatusChangeRequest videoStatusChangeRequest);
}
