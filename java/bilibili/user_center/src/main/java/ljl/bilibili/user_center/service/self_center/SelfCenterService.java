package ljl.bilibili.user_center.service.self_center;

import ljl.bilibili.user_center.vo.request.self_center.EditUserCenterPrivilegeRequest;
import ljl.bilibili.user_center.vo.response.self_center.RemotelyLikeVideoResponse;
import ljl.bilibili.user_center.vo.response.self_center.SelfVideoResponse;
import ljl.bilibili.user_center.vo.response.self_center.UserPrivilegeResponse;
import ljl.bilibili.util.Result;

import java.util.List;

public interface SelfCenterService {
    Result<List<SelfVideoResponse>> getPersonalVideo(Integer userId);
    Result<List<RemotelyLikeVideoResponse>> getRemotelyLikeVideo(Integer userId);
    Result<UserPrivilegeResponse> getUserPrivilege(int userId);
    Result<Boolean> editUserPrivilege(EditUserCenterPrivilegeRequest editUserCenterPrivilegeRequest);
}
