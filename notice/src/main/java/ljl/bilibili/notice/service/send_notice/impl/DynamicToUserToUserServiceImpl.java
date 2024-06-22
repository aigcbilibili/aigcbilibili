package ljl.bilibili.notice.service.send_notice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ljl.bilibili.entity.notice.dynamic.DynamicToUser;
import ljl.bilibili.mapper.notice.dynamic.DynamicToUserMapper;
import ljl.bilibili.notice.service.send_notice.DynamicToUserService;
import org.springframework.stereotype.Service;

@Service
public class DynamicToUserToUserServiceImpl extends ServiceImpl<DynamicToUserMapper, DynamicToUser> implements DynamicToUserService {
}
