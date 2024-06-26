package ljl.bilibili.user_center.vo.request.ensemble;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.video.audience_reactions.ensemble.AddToEnsemble;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddToEnsembleRequest {
    @ApiModelProperty("视频id")
    private Integer videoId;
    @ApiModelProperty("合集id")
    private Integer ensembleId;
    public AddToEnsemble toEntity(){
        AddToEnsemble addToEnsemble=new AddToEnsemble();
        BeanUtils.copyProperties(this,addToEnsemble);
        return addToEnsemble;
    }
}
