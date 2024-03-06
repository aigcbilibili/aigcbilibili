package ljl.bilibili.gateway.vo.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PasswordLoginRequest {
    private String userName;
    private String password;
    private String captcha;
}
