package ljl.bilibili.user_center.vo.response.follow;

import lombok.Data;

@Data
public class FansAndIdolCountResponse {
    int fansCount;
    int idolCount;
    public FansAndIdolCountResponse(int fansCount,int idolCount){
        this.fansCount=fansCount;
        this.idolCount=idolCount;
    }
}
