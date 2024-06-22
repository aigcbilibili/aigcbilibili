package ljl.bilibili.gateway.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.gateway.service.ReMakePasswordService;
import ljl.bilibili.gateway.vo.request.RemakePasswordRequest;
import ljl.bilibili.gateway.service.impl.ReMakePasswordServiceImpl;
import ljl.bilibili.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "忘记密码")
@RestController
@RequestMapping("/forgetPassword")
public class ReMakePasswordController {
    @Resource
    ReMakePasswordService reMakePasswordService;
    @ApiOperation("密码重开")
    @PostMapping("/remakePassword")
    public Result<Boolean> remakePassword(@RequestBody RemakePasswordRequest remakePasswordRequest) {
        return reMakePasswordService.remakePassword(remakePasswordRequest);
    }
}
