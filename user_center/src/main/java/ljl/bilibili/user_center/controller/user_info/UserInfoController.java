package ljl.bilibili.user_center.controller.user_info;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.user_center.service.user_info.impl.UserInfoServiceImpl;
import ljl.bilibili.user_center.vo.request.self_center.EditUserInfoRequest;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ljl.bilibili.user_center.vo.response.self_center.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/userInfo")
@Api(tags = "用户信息查看与编辑")
@CrossOrigin(value = "*")
@Slf4j
public class UserInfoController {
    @Resource
    UserInfoServiceImpl userInfoService;

    @GetMapping("/getUserInfo/{selfId}/{visitedId}")
    @ApiOperation("获取用户信息")
    public Result<UserInfoResponse> getUserInfo(@PathVariable Integer selfId,@PathVariable Integer visitedId) {
        log.info("1");
        return userInfoService.getUserInfo(selfId,visitedId);
    }
    //    @GetMapping("/getFansAndIdolCount/{userId}")
//    @ApiOperation("登录后获取用户粉丝数和关注数")
//    public UserInfoResponse getFansAndIdolCount(@PathVariable Integer userId) {
//        return userInfoService.selectUserInfo(userId);
//    }
    @PostMapping("/editUserInfo")
    @ApiOperation("编辑个人信息")
    public Result<Boolean> editUserInfo(@RequestParam("file") MultipartFile file,@RequestParam("userId") Integer userId,@RequestParam("nickname") String nickname,@RequestParam("intro") String intro) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("1");
        return userInfoService.editSelfInfo(file,userId,nickname,intro);
    }
}
