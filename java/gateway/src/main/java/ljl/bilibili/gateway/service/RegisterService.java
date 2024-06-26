package ljl.bilibili.gateway.service;

import ljl.bilibili.gateway.vo.request.PasswordRegisterRequest;
import ljl.bilibili.util.Result;

public interface RegisterService {
    Result<Integer> passwordRegister(PasswordRegisterRequest passwordRegisterRequest);
}
