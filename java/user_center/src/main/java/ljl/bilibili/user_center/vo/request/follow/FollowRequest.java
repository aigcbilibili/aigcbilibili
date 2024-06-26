package ljl.bilibili.user_center.vo.request.follow;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class FollowRequest {
    int fansId;
    int idolId;
    public Follow toEntity(){
        Follow follow=new Follow();
        BeanUtils.copyProperties(this,follow);
        return follow;
    }
}
