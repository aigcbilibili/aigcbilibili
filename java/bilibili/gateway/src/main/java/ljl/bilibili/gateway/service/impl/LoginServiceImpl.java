package ljl.bilibili.gateway.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.gateway.service.LoginService;
import ljl.bilibili.gateway.util.CaptchaUtil;
import ljl.bilibili.gateway.util.JwtUtil;
import ljl.bilibili.gateway.vo.request.PasswordLoginRequest;
import ljl.bilibili.gateway.vo.request.PhoneNumberLoginRequest;
import ljl.bilibili.mapper.user_center.user_info.PrivilegeMapper;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static ljl.bilibili.gateway.constant.Constant.*;


@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Resource
    UserMapper userMapper;

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    PrivilegeMapper privilegeMapper;
    @Resource
    SendNoticeClient client;
    @Resource
    private PasswordEncoder passwordEncoder;
    /**
     *解密数据库中加密后密码验证用户，登录成功返回长短token与用户id
     **/
    @Override
    public Map<String, Object> passwordLogin(PasswordLoginRequest passwordLoginRequest, String sessionId) {
        String code = (String) redisTemplate.opsForValue().get(sessionId);
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, passwordLoginRequest.getUserName());
        User user = userMapper.selectOne(wrapper);
        Map<String, Object> map = new HashMap<>(2);
        if (user != null && passwordEncoder.matches(passwordLoginRequest.getPassword(),user.getPassword())&&code.equals(passwordLoginRequest.getCaptcha()) ) {
            map.put(USERIDENTITY,user.getId());
            map.put(SHORT_TOKEN, JwtUtil.generateShortToken(user.getId()));
            map.put(LONG_TOKEN, JwtUtil.generateLongToken(user.getId()));
        }

        return map;
    }
    @Override
    @Transactional
    /**
     *手机号登录，若无该用户则新创建一个用户并发送新增用户数据同步消息
     **/
    public Map<String, Object> phoneNumberLogin(PhoneNumberLoginRequest phoneNumberLoginRequest, String sessionId) {
        String code = (String) redisTemplate.opsForValue().get(sessionId);
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhoneNumber, phoneNumberLoginRequest.getPhoneNumber());
        User user = userMapper.selectOne(wrapper);
        Integer id=null;
        Map<String, Object> map = new HashMap<>(2);
        if (user != null && user.getPhoneNumber().equals(phoneNumberLoginRequest.getPhoneNumber()) &&code.equals(phoneNumberLoginRequest.getCaptcha()) ) {
            map.put(USERIDENTITY,user.getId());
            privilegeMapper.insert(new Privilege().setUserId(user.getId()));
            id=user.getId();
        }
        if(user==null){
            User newUser=phoneNumberLoginRequest.toEntity();
            userMapper.insert(newUser);
            id=newUser.getId();
            CompletableFuture<Void> sendDBChangeNotice = CompletableFuture.runAsync(() -> {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> userMap = objectMapper.convertValue(newUser, Map.class);
                userMap.put(TABLE_NAME, USER_TABLE_NAME);
                userMap.put(OPERATION_TYPE, OPERATION_TYPE_ADD);
                userMap.remove(TABLE_IGNORE_USERNAME);
                userMap.remove(TABLE_IGNORE_PASSWORD);
                userMap.remove(TABLE_IGNORE_PHONE_NUMBER);
                client.sendDBChangeNotice(userMap);
            });
            map.put(USERIDENTITY,newUser.getId());
        }
        map.put(SHORT_TOKEN, JwtUtil.generateShortToken(id));
        map.put(LONG_TOKEN, JwtUtil.generateLongToken(id));
        return map;
    }
    @Override
    public Map<String, String> getCaptcha(HttpCookie cookie) throws IOException {
        Map<String, String> captchaMap = new HashMap<>(2);
        StringBuffer code = new StringBuffer();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        for (int i = 0; i < LINE_COUNT; i++) {
            int xs = random.nextInt(WIDTH);
            int ys = random.nextInt(HEIGHT);
            int xe = random.nextInt(WIDTH);
            int ye = random.nextInt(HEIGHT);
            g.setColor(CaptchaUtil.getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }
        for (int i = 0; i < CODE_COUNT; i++) {
            String strRand = String.valueOf(CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)]);
            g.setColor(CaptchaUtil.getRandColor(1, 255));
            g.drawString(strRand, (i + 1) * 30, 30);
            code.append(strRand);
        }
        g.dispose();
        String captcha = CaptchaUtil.imageToBase64(image);
        captchaMap.put(CAPTCHA_IMAGE,captcha);
        captchaMap.put(CAPTCHA_CODE,code.toString());
        if(cookie==null){
            String sessionId=UUID.randomUUID().toString().substring(0,15);
            captchaMap.put(SESSIONID,sessionId);
            redisTemplate.opsForValue().set(sessionId,code.toString());
        }else {
            redisTemplate.opsForValue().set(cookie.getValue(),code.toString());
        }
            return captchaMap;
    }

}
