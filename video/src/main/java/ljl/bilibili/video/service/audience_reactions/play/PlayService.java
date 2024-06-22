package ljl.bilibili.video.service.audience_reactions.play;

import ljl.bilibili.client.pojo.RecommendVideo;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.vo.request.audience_reactions.play.DeleteHistoryVideoRequest;
import ljl.bilibili.video.vo.response.audience_reactions.play.CommendVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.DetailVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.FirstPageVideoResponse;
import ljl.bilibili.video.vo.response.audience_reactions.play.HistoryVideoResponse;

import java.util.List;

public interface PlayService {
    Result<Boolean> addPlayRecord(int videoId, int userId);
    Result<Boolean> deleteHistoryVideo(DeleteHistoryVideoRequest deleteHistoryVideoRequest);
    Result<DetailVideoResponse> getDetailVideo(Integer videoId, Integer userId, String collectGroupId);
    Result<List<CommendVideoResponse>> getRecommendVideo(String videoId);
    Result<List<FirstPageVideoResponse>> getFirstPageVideoResponse(Integer count);
    Result<List<HistoryVideoResponse>> getHistoryVideo(int userId);
}
