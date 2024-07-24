package ljl.bilibili.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.gateway.constant.Constant;
import ljl.bilibili.gateway.service.RegisterService;
import ljl.bilibili.gateway.vo.request.PasswordRegisterRequest;
import ljl.bilibili.mapper.user_center.user_info.PrivilegeMapper;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    UserMapper userMapper;
    @Resource
    PrivilegeMapper privilegeMapper;
    @Resource
    SendNoticeClient client;
    @Resource
    private PasswordEncoder passwordEncoder;
    /**
     *账号密码注册，并发送新增用户的数据同步消息
     */
    @Transactional
    @Override
    public Result<Integer> passwordRegister(PasswordRegisterRequest passwordRegisterRequest){
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,passwordRegisterRequest.getUserName());
        if(userMapper.selectOne(wrapper)!=null){
            return Result.error("该用户已存在");
        }else {
            User user =passwordRegisterRequest.toEntity();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insert(user);
//            CompletableFuture<Void> sendDBChangeNotice = CompletableFuture.runAsync(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.convertValue(user, Map.class);
            map.put(Constant.TABLE_NAME, Constant.USER_TABLE_NAME);
            map.put(Constant.OPERATION_TYPE, Constant.OPERATION_TYPE_ADD);
            map.remove(Constant.TABLE_IGNORE_USERNAME);
            map.remove(Constant.TABLE_IGNORE_PASSWORD);
            map.remove(Constant.TABLE_IGNORE_PHONE_NUMBER);
            map.remove(Constant.TABLE_IGNORE_MAIL_NUMBER);
            client.sendDBChangeNotice(map);
//            });
            privilegeMapper.insert(new Privilege().setUserId(user.getId()));
            return Result.data(user.getId());
        }

    }
}
