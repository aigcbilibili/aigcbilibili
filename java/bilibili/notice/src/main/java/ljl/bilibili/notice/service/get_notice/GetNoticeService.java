package ljl.bilibili.notice.service.get_notice;

import ljl.bilibili.notice.vo.response.get_notice.CommentNoticeResponse;
import ljl.bilibili.notice.vo.response.get_notice.DynamicVideoResponse;
import ljl.bilibili.notice.vo.response.get_notice.LikeNoticeResponse;
import ljl.bilibili.notice.vo.response.get_notice.UnReadNoticeCountResponse;
import ljl.bilibili.util.Result;

import java.util.List;

public interface GetNoticeService {
    Result<UnReadNoticeCountResponse> getNoticeCount(Integer userId);
    Result<List<LikeNoticeResponse>> getLikeNotice(Integer userId);
    Result<List<CommentNoticeResponse>> getCommentNotice(Integer userId);
    Result<List<DynamicVideoResponse>> getDynamicVideo(Integer userId);
}
