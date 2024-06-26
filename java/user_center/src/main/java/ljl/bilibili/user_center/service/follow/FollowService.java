package ljl.bilibili.user_center.service.follow;

import ljl.bilibili.user_center.vo.request.follow.FollowRequest;
import ljl.bilibili.user_center.vo.response.follow.IdolOrFansListResponse;
import ljl.bilibili.util.Result;

import java.util.List;

public interface FollowService {
    Result<Boolean> follow(FollowRequest follow);
    Result<Boolean> recallFollow( FollowRequest follow);
    Result<List<IdolOrFansListResponse>> getIdolOrFansListResponse(int userId, int type);
}
