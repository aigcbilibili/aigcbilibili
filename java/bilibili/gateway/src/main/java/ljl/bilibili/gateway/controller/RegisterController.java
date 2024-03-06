package ljl.bilibili.gateway.controller;
import ljl.bilibili.gateway.service.RegisterService;
import ljl.bilibili.gateway.vo.request.PasswordRegisterRequest;
import ljl.bilibili.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController
@RequestMapping("/register")
@CrossOrigin(value = "*")
public class RegisterController {
    @Resource
    RegisterService registerService;
    @PostMapping("/passwordRegister")
    public Result<Integer> passwordRegister(@RequestBody PasswordRegisterRequest passwordRegisterRequest){
        return registerService.passwordRegister(passwordRegisterRequest);
    }
}
