package ljl.bilibili.video.service.audience_reactions.danmaku;

import ljl.bilibili.util.Result;
import ljl.bilibili.video.vo.request.audience_reactions.danmaku.AddDanmakuRequest;
import ljl.bilibili.video.vo.response.audience_reactions.danmaku.DanmakuResponse;

import java.util.List;

public interface DanmakuService{
    Result<Boolean> addDanmaku(AddDanmakuRequest addDanmakuRequest);
    Result<List<DanmakuResponse>> getDanmaku(Integer videoId);
}
