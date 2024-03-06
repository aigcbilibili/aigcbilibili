package ljl.bilibili.gateway.service;

import ljl.bilibili.gateway.vo.request.PasswordLoginRequest;
import ljl.bilibili.gateway.vo.request.PhoneNumberLoginRequest;
import org.springframework.http.HttpCookie;

import java.io.IOException;
import java.util.Map;

public interface LoginService {
    Map<String, Object> phoneNumberLogin(PhoneNumberLoginRequest phoneNumberLoginRequest, String sessionId);

    Map<String, Object> passwordLogin(PasswordLoginRequest passwordLoginRequest, String sessionId);

    Map<String, String> getCaptcha(HttpCookie cookie) throws IOException;

}
