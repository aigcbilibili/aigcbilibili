package ljl.bilibili.gateway.service;

import com.aliyuncs.exceptions.ClientException;
import ljl.bilibili.gateway.vo.request.MailLoginRequest;
import ljl.bilibili.gateway.vo.request.PasswordLoginRequest;
import ljl.bilibili.gateway.vo.request.PhoneNumberLoginRequest;
import ljl.bilibili.util.Result;
import org.springframework.http.HttpCookie;

import java.io.IOException;
import java.util.Map;

public interface LoginService {
    Map<String, Object> phoneNumberLogin(PhoneNumberLoginRequest phoneNumberLoginRequest);

    Map<String, Object> passwordLogin(PasswordLoginRequest passwordLoginRequest, String sessionId);

    Map<String, String> getCaptcha(HttpCookie cookie) throws IOException;
    Map<String,Object> mailLogin(MailLoginRequest mailLoginRequest);

     Result<Boolean> sendPhoneNumberCaptcha( String phoneNumber) throws ClientException;
     Result<Boolean> sendMailNumberCaptcha(String mailNumber);

}
