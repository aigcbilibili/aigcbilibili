package ljl.bilibili.video.service.audience_reactions.collect;

import ljl.bilibili.entity.video.audience_reactions.collect.CollectGroup;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectGroupRequest;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectRequest;
import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectGroupResponse;
import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectVideoResponse;

import java.util.List;

public interface CollectService {
    Result<Boolean> collect(List<CollectRequest> collectRequest);
    Result<Boolean> editCollectGroup(CollectGroupRequest createCollectGroupRequest);
    Result<Boolean> deleteCollectGroup(CollectGroupRequest createCollectGroupRequest);
    Result<List<CollectGroup>> getCollectGroup(Integer userId);
    Result<List<CollectVideoResponse>> getCollectVideo(Integer userId);
    Result<List<CollectGroupResponse>> getVideoToCollectGroup(Integer userId, Integer videoId);
}
