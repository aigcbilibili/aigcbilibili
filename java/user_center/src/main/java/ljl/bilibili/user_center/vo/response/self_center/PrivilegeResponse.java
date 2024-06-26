package ljl.bilibili.user_center.vo.response.self_center;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PrivilegeResponse {
    Integer collectGroup;
    Integer remotelyLike;
    Integer fansList;
    Integer idolList;
}
