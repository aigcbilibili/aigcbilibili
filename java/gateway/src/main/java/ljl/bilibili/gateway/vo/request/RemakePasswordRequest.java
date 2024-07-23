package ljl.bilibili.gateway.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RemakePasswordRequest {
    private String userName;
    private String password;
    private String makeSurePassword;
}
