package ljl.bilibili.user_center.controller.self_center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.user_center.service.self_center.impl.SelfCenterServiceImpl;
import ljl.bilibili.user_center.vo.request.self_center.EditUserCenterPrivilegeRequest;
import ljl.bilibili.user_center.vo.response.self_center.RemotelyLikeVideoResponse;
import ljl.bilibili.user_center.vo.response.self_center.SelfVideoResponse;
import ljl.bilibili.user_center.vo.response.self_center.UserPrivilegeResponse;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "用户个人主页各模块权限开放状态查看与编辑")
@RequestMapping("/selfCenter")
@CrossOrigin(value = "*")
@Slf4j
public class SelfCenterController {
    @Resource
    SelfCenterServiceImpl selfCenterService;
    @GetMapping("/getUserPrivilege/{userId}")
    @ApiOperation("获取用户个人主页各模块权限开放状态")
    public Result<UserPrivilegeResponse> getUserPrivilege(@PathVariable Integer userId) {
        log.info("1");
        return selfCenterService.getUserPrivilege(userId);
    }
    @ApiOperation("修改用户个人主页各模块权限开放状态")
    @PostMapping("/editUserPrivilege")
    public Result<Boolean> editUserPrivilege(@RequestBody EditUserCenterPrivilegeRequest editUserCenterPrivilegeRequest){
        log.info("1");
        return   selfCenterService.editUserPrivilege(editUserCenterPrivilegeRequest);

    }

    @GetMapping("/getPersonalVideo/{userId}")
    @ApiOperation("获取个人主页投稿视频")
    public Result<List<SelfVideoResponse>> getPersonalVideo(@PathVariable Integer userId){
        log.info("1");
        return selfCenterService.getPersonalVideo(userId);
    }
    @GetMapping("/getRemotelyLikeVideo/{userId}")
    @ApiOperation("获取个人主页最近点赞视频")
    public Result<List<RemotelyLikeVideoResponse>> getRemotelyLikeVideo(@PathVariable Integer userId){
        log.info("1");
        return selfCenterService.getRemotelyLikeVideo(userId);
    }
}
