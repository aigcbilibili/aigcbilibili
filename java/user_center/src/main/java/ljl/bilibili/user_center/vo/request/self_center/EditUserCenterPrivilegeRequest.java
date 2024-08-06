package ljl.bilibili.user_center.vo.request.self_center;

import ljl.bilibili.entity.user_center.user_info.Privilege;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class EditUserCenterPrivilegeRequest {
    int userId;
    int collectGroup;
    int remotelyLike;
    int fansList;
    int idolList;
    public Privilege toEntity(){
        Privilege privilege=new Privilege();
        BeanUtils.copyProperties(this,privilege);
        return privilege;
    }
}
