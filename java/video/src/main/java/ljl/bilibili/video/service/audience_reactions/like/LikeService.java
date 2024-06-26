package ljl.bilibili.video.service.audience_reactions.like;

import ljl.bilibili.util.Result;
import ljl.bilibili.video.vo.request.audience_reactions.like.LikeRequest;

public interface LikeService {
    Result<Boolean> like(LikeRequest likeRequest);
    Result<Boolean> recallLike(LikeRequest likeRequest);
}
