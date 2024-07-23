package ljl.bilibili.gateway.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.gateway.service.ReMakePasswordService;
import ljl.bilibili.gateway.vo.request.RemakePasswordRequest;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.util.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReMakePasswordServiceImpl implements ReMakePasswordService {
    @Resource
    UserMapper userMapper;
    @Resource
    PasswordEncoder passwordEncoder;
    @Override
    public Result<Boolean> remakePassword(RemakePasswordRequest remakePasswordRequest) {
        LambdaQueryWrapper<User> selectWrapper=new LambdaQueryWrapper<>();
        LambdaUpdateWrapper<User> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getPassword,passwordEncoder.encode(remakePasswordRequest.getPassword()));
        updateWrapper.eq(User::getUserName,remakePasswordRequest.getUserName());
        selectWrapper.eq(User::getUserName,remakePasswordRequest.getUserName());
        User user =userMapper.selectOne(selectWrapper);
        if(user !=null&&remakePasswordRequest.getPassword().equals(remakePasswordRequest.getMakeSurePassword())){
            userMapper.update(user,updateWrapper);
            return Result.success(true);
        }
        return Result.error("用户不存在或密码与确认密码不一致");
    }
}
