package ljl.bilibili.user_center.service.ensemble;

import ljl.bilibili.entity.user_center.ensemble.VideoEnsemble;
import ljl.bilibili.user_center.vo.request.ensemble.AddOrUpdateEnsembleRequest;
import ljl.bilibili.user_center.vo.request.ensemble.AddToEnsembleRequest;
import ljl.bilibili.user_center.vo.request.ensemble.DeleteFromEnsembleRequest;
import ljl.bilibili.user_center.vo.response.ensemble.AllVideoInEnsembleResponse;
import ljl.bilibili.user_center.vo.response.ensemble.EnsembleVideoResponse;
import ljl.bilibili.util.Result;

import java.util.List;

public interface EnsembleService {
    Result<List<VideoEnsemble>> getEnsembleList(Integer userId);
    Result<Boolean> addOrUpdateEnsemble(AddOrUpdateEnsembleRequest addOrUpdateEnsembleRequest);
    Result<List<EnsembleVideoResponse>> getEnsembleVideo(int userId);
    Result<AllVideoInEnsembleResponse> getAllVideoInEnsemble(Integer userId, Integer videoId);
    Result<Boolean> addToEnsemble(AddToEnsembleRequest addToEnsembleRequest);
    Result<Boolean> deleteFromEnsemble(DeleteFromEnsembleRequest deleteFromEnsembleRequest);
}
