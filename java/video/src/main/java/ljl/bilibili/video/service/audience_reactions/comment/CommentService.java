package ljl.bilibili.video.service.audience_reactions.comment;

import ljl.bilibili.util.Result;
import ljl.bilibili.video.vo.request.audience_reactions.comment.CommentRequest;
import ljl.bilibili.video.vo.response.audience_reactions.comment.VideoCommentResponse;

public interface CommentService {
    Result<Boolean> commentToComment(CommentRequest commentRequest);
    Result<VideoCommentResponse> getComment(Integer videoId, Integer userId, Integer type);
}
