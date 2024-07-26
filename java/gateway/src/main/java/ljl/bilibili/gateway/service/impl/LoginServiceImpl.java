package ljl.bilibili.gateway.service.impl;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.gateway.service.LoginService;
import ljl.bilibili.gateway.util.CaptchaUtil;
import ljl.bilibili.gateway.util.JwtUtil;
import ljl.bilibili.gateway.util.SendMessage;
import ljl.bilibili.gateway.vo.request.MailLoginRequest;
import ljl.bilibili.gateway.vo.request.PasswordLoginRequest;
import ljl.bilibili.gateway.vo.request.PhoneNumberLoginRequest;
import ljl.bilibili.mapper.user_center.user_info.PrivilegeMapper;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static ljl.bilibili.gateway.constant.Constant.*;

/**
 *登录
 */
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
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    public JavaMailSender mailSender;
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
        //如果用户存在且密码匹配上了验证码也和图像验证码值一致就登录成功并返回长短token和userId给controller层
        if (user != null &&passwordEncoder.matches(passwordLoginRequest.getPassword(),user.getPassword())&&code.equals(passwordLoginRequest.getCaptcha()) ) {
            map.put(USERIDENTITY, user.getId());
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
    public Map<String, Object> phoneNumberLogin(PhoneNumberLoginRequest phoneNumberLoginRequest) {
        String code = (String) redisTemplate.opsForValue().get(phoneNumberLoginRequest.getPhoneNumber());
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhoneNumber, phoneNumberLoginRequest.getPhoneNumber());
        User user = userMapper.selectOne(wrapper);
        Integer id=null;
        Map<String, Object> map = new HashMap<>(2);
        //如果用户存在且手机接收的验证码和传来的验证码对上了就返回userId和长短token
        if (user != null && passwordEncoder.matches(phoneNumberLoginRequest.getPhoneNumber(),user.getPhoneNumber()) &&code.equals(phoneNumberLoginRequest.getCaptcha()) ) {
            map.put(USERIDENTITY, user.getId());
        }
        //不存在则直接创建一个新用户
        if(user ==null){
            User newUser =phoneNumberLoginRequest.toEntity().setCover("https://labilibili.com/user-cover/default.png");
            passwordEncoder.encode(newUser.getPassword());
            userMapper.insert(newUser);
            privilegeMapper.insert(new Privilege().setUserId(newUser.getId()));
            id= newUser.getId();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> userMap = objectMapper.convertValue(newUser, Map.class);
                userMap.put(TABLE_NAME, USER_TABLE_NAME);
                userMap.put(OPERATION_TYPE, OPERATION_TYPE_ADD);
                userMap.remove(TABLE_IGNORE_USERNAME);
                userMap.remove(TABLE_IGNORE_PASSWORD);
                userMap.remove(TABLE_IGNORE_PHONE_NUMBER);
                client.sendDBChangeNotice(userMap);
            map.put(USERIDENTITY, newUser.getId());
        }
        map.put(SHORT_TOKEN, JwtUtil.generateShortToken(id));
        map.put(LONG_TOKEN, JwtUtil.generateLongToken(id));
        return map;
    }
    /**
     *邮箱号登录，若无该用户则新创建一个用户并发送新增用户数据同步消息
     **/
    @Override
    public Map<String, Object> mailLogin(MailLoginRequest mailLoginRequest) {
        String code = (String) redisTemplate.opsForValue().get(mailLoginRequest.getMailNumber());
        log.info("邮箱验证码"+code);
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getMailNumber, mailLoginRequest.getMailNumber());
        User user = userMapper.selectOne(wrapper);
        Integer id=null;
        Map<String, Object> map = new HashMap<>(2);
        log.info(mailLoginRequest.getMailNumber());
        log.info(code);
        log.info(mailLoginRequest.getCaptcha());
        //同手机号
        if (user != null && passwordEncoder.matches(mailLoginRequest.getMailNumber(),user.getMailNumber()) &&code.equals(mailLoginRequest.getCaptcha()) ) {
            id=user.getId();
            map.put(USERIDENTITY, id);
            log.info(id.toString());

        }
        if(user ==null){
            User newUser =mailLoginRequest.toEntity().setCover("https://labilibili.com/user-cover/default.png");
            passwordEncoder.encode(newUser.getMailNumber());
            userMapper.insert(newUser);
            privilegeMapper.insert(new Privilege().setUserId(newUser.getId()));
            id= newUser.getId();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> userMap = objectMapper.convertValue(newUser, Map.class);
                userMap.put(TABLE_NAME, USER_TABLE_NAME);
                userMap.put(OPERATION_TYPE, OPERATION_TYPE_ADD);
                userMap.remove(TABLE_IGNORE_USERNAME);
                userMap.remove(TABLE_IGNORE_PASSWORD);
                userMap.remove(TABLE_IGNORE_PHONE_NUMBER);
                client.sendDBChangeNotice(userMap);
            map.put(USERIDENTITY, newUser.getId());
        }
        map.put(SHORT_TOKEN, JwtUtil.generateShortToken(id));
        map.put(LONG_TOKEN, JwtUtil.generateLongToken(id));
        return map;
    }
    //获取图像验证码
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
    /**
     *发送手机验证码
     **/
    @Override
    public Result<Boolean> sendPhoneNumberCaptcha( String phoneNumber) throws ClientException{
        Random random=new Random();
        String number=""+random.nextInt(10)+random.nextInt(10)+random.nextInt(10)+random.nextInt(10)+random.nextInt(10)+random.nextInt(10);
        SendMessage.sendSms(phoneNumber,number);
        redisTemplate.opsForValue().set(phoneNumber,number);
        return Result.data(true);
    }
    /**
     *发送邮箱验证码
     **/
    @Override
    public Result<Boolean> sendMailNumberCaptcha(String mailNumber){
        SimpleMailMessage message = new SimpleMailMessage();
        Random random=new Random();
        message.setTo(mailNumber);
        message.setSubject("验证码");
        String captcha=""+random.nextInt(10)+random.nextInt(10)+random.nextInt(10)+random.nextInt(10)+random.nextInt(10)+random.nextInt(10);
        redisTemplate.opsForValue().set(mailNumber,captcha);
        message.setText("aigcbilibilibili验证码:"+captcha);
        message.setFrom(from);
        log.info("send");
        log.info(from);
        mailSender.send(message);
        return Result.data(true);
    }

}
