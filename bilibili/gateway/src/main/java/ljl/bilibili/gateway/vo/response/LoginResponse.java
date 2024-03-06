package ljl.bilibili.gateway.vo.response;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {
    private Integer userId;
    private Boolean status;

}
