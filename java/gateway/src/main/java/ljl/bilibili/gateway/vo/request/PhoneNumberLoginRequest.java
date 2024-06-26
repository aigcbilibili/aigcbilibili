package ljl.bilibili.gateway.vo.request;

import ljl.bilibili.entity.user_center.user_info.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class PhoneNumberLoginRequest {
    private String phoneNumber;
    private String captcha;
    public User toEntity(){
        User user =new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }
}
