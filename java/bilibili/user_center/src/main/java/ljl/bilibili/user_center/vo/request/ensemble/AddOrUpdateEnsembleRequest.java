package ljl.bilibili.user_center.vo.request.ensemble;

import ljl.bilibili.entity.user_center.ensemble.VideoEnsemble;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddOrUpdateEnsembleRequest {
    public Integer userId;
    public Integer id;
    public String name;
    public String intro;
    public VideoEnsemble toEntity(){
        VideoEnsemble videoEnsemble=new VideoEnsemble();
        BeanUtils.copyProperties(this,videoEnsemble);
        return videoEnsemble;
    }
}
