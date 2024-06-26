package ljl.bilibili.user_center.service.self_center;

import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.user_center.vo.request.self_center.EditUserCenterPrivilegeRequest;
import ljl.bilibili.user_center.vo.response.self_center.PrivilegeResponse;
import ljl.bilibili.user_center.vo.response.self_center.RemotelyLikeVideoResponse;
import ljl.bilibili.user_center.vo.response.self_center.SelfCenterContentResponse;
import ljl.bilibili.user_center.vo.response.self_center.SelfVideoResponse;
import ljl.bilibili.util.Result;

import java.util.List;

public interface SelfCenterService {
    Result<SelfCenterContentResponse> getPersonalCenterContent(Integer selfId, Integer visitedId);
    Result<Boolean> editUserPrivilege(EditUserCenterPrivilegeRequest editUserCenterPrivilegeRequest);
    Result<PrivilegeResponse> getUserPrivilege(Integer userId);
}
