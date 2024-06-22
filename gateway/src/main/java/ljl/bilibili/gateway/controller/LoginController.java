package ljl.bilibili.gateway.controller;
import com.aliyuncs.exceptions.ClientException;
import ljl.bilibili.gateway.service.LoginService;
import ljl.bilibili.gateway.util.JwtUtil;
import ljl.bilibili.gateway.util.SendMessage;
import ljl.bilibili.gateway.vo.request.MailLoginRequest;
import ljl.bilibili.gateway.vo.request.PasswordLoginRequest;
import ljl.bilibili.gateway.vo.response.LoginResponse;
import ljl.bilibili.gateway.service.impl.LoginServiceImpl;
import ljl.bilibili.gateway.vo.request.PhoneNumberLoginRequest;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import static ljl.bilibili.gateway.constant.Constant.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(value = "*")
@Slf4j
public class LoginController {
    @Resource
    LoginService loginService;

    /**
     *登录成功返回放在缓存中的短token和放在http-only-cookie中的长token，失败返回失败响应
     */
    @PostMapping("/passwordLogin")
    public Result<LoginResponse> passwordLogin(@RequestBody PasswordLoginRequest passwordLoginRequest, ServerWebExchange exchange) {

        String sessionId=exchange.getRequest().getCookies().getFirst(SESSIONID).getValue();
        Map<String, Object> map = loginService.passwordLogin(passwordLoginRequest,sessionId);
        if (map.size()>0) {
            Integer userId=(Integer) map.get(USERIDENTITY);
            String shortJwt = (String) map.get(SHORT_TOKEN);
            String longJwt=(String)map.get(LONG_TOKEN);
                    exchange.getResponse().getHeaders().add(SHORT_TOKEN, shortJwt);
            ResponseCookie cookie = ResponseCookie.from(LONG_TOKEN, longJwt)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 30)
                    .build();
            exchange.getResponse().addCookie(cookie);
            return Result.data(new LoginResponse().setStatus(true).setUserId(userId));
        }
        return Result.data(new LoginResponse().setStatus(false));
    }
    @PostMapping("/phoneNumberLogin")
    public Result<LoginResponse> phoneNumberLogin(@RequestBody PhoneNumberLoginRequest phoneNumberLoginRequest, ServerWebExchange exchange) {
        Map<String, Object> map = loginService.phoneNumberLogin(phoneNumberLoginRequest);
        if (map != null) {
            Integer userId=(Integer) map.get(USERIDENTITY);
            String shortJwt = (String) map.get(SHORT_TOKEN);
            String longJwt=(String)map.get(LONG_TOKEN);
            exchange.getResponse().getHeaders().add(SHORT_TOKEN, shortJwt);
            ResponseCookie cookie = ResponseCookie.from(LONG_TOKEN, longJwt)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 30)
                    .build();
            exchange.getResponse().addCookie(cookie);
            log.info("手机号登录的id"+userId);
            return Result.data(new LoginResponse().setStatus(true).setUserId(userId));
        }
        return Result.data(new LoginResponse().setStatus(false));
    }
    @PostMapping("/mailLogin")
    public Result<LoginResponse> mailLogin(@RequestBody MailLoginRequest mailLoginRequest, ServerWebExchange exchange) {
        Map<String, Object> map = loginService.mailLogin(mailLoginRequest);
        if (map != null) {
            Integer userId=(Integer) map.get(USERIDENTITY);
            String shortJwt = (String) map.get(SHORT_TOKEN);
            String longJwt=(String)map.get(LONG_TOKEN);
            exchange.getResponse().getHeaders().add(SHORT_TOKEN, shortJwt);
            ResponseCookie cookie = ResponseCookie.from(LONG_TOKEN, longJwt)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 30)
                    .build();
            exchange.getResponse().addCookie(cookie);
            log.info("邮箱登录的id"+userId);
            return Result.data(new LoginResponse().setStatus(true).setUserId(userId));
        }
        return Result.data(new LoginResponse().setStatus(false));
    }
    /**
     *刷新token
     */
    @PostMapping("/refreshToken")
    public Result<Boolean> refreshToken(ServerWebExchange exchange){
        log.info("刷新token");
        String shortToken= exchange.getRequest().getHeaders().getFirst(SHORT_TOKEN);
        log.info(shortToken);
        String longToken=exchange.getRequest().getCookies().getFirst(LONG_TOKEN).getValue();
        exchange.getResponse().getHeaders().set(SHORT_TOKEN, JwtUtil.refreshToken(shortToken,0));
        ResponseCookie cookie = ResponseCookie.from(LONG_TOKEN, JwtUtil.refreshToken(longToken,1))
                .httpOnly(true)
                .path("/")
                .maxAge(LONG_TOKEN_EXPIRATION )
                .build();
        exchange.getResponse().addCookie(cookie);
        return Result.success(true);
    }
    /**
     *获取验证码，并将sessionId返回给客户端，后续刷新验证码时根据sessionId更换redis中验证码值
     */
    @GetMapping("/getCaptcha")
    public Result<String> getCaptcha(ServerWebExchange exchange) throws IOException {
        HttpCookie cookie=exchange.getRequest().getCookies().getFirst(SESSIONID);

        Map<String,String> map=  loginService.getCaptcha(cookie);
        if( map.get(SESSIONID)!=null){
            ResponseCookie cookies = ResponseCookie.from(SESSIONID, map.get(SESSIONID))
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 30)
                    .build();
            exchange.getResponse().addCookie(cookies);
        }
       return Result.data(map.get(CAPTCHA_IMAGE));
    }
    /**
     *发送手机验证码
     */
    @GetMapping("/phoneNumberCaptcha/{phoneNumber}")
    public Result<Boolean> sendPhoneNumberCaptcha(@PathVariable String phoneNumber) throws ClientException {
        return loginService.sendPhoneNumberCaptcha(phoneNumber);
    }

    @GetMapping("/mailNumberCaptcha/{mailNumber}")
    public Result<Boolean> sendMailNumberCaptcha(@PathVariable String mailNumber){

        return loginService.sendMailNumberCaptcha(mailNumber);
    }
}
