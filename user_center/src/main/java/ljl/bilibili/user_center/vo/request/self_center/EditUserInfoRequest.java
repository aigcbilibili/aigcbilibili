package ljl.bilibili.user_center.vo.request.self_center;

import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.user_center.constant.Constant;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Data
public class EditUserInfoRequest {
    private Integer id;
    private String nickname;
    private MultipartFile coverImg;
    private String intro;
    public User toEntity(){
        User user =new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }
    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put(Constant.TABLE_NAME, Constant.USER_TABLE_NAME);
        map.put(Constant.OPERATION_TYPE, Constant.OPERATION_TYPE_UPDATE);
        map.put(Constant.TABLE_ID,id);
        if(nickname!=null){
            map.put(Constant.USER_NICKNAME,nickname);
        }
        if(intro!=null){
            map.put(Constant.USER_INTRO,intro);
        }
        return map;
    }
}
