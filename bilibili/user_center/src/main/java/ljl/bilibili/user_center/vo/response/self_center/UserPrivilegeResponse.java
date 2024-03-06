package ljl.bilibili.user_center.vo.response.self_center;

import ljl.bilibili.entity.user_center.user_info.Privilege;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserPrivilegeResponse {
    int collectGroup;
    int remotelyLike;
    int fansList;
    int idolList;

    public UserPrivilegeResponse(Privilege privilege){
        BeanUtils.copyProperties(privilege,this);
    }
}
