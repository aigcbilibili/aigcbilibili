package ljl.bilibili.user_center.controller.self_center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.user_center.service.self_center.impl.SelfCenterServiceImpl;
import ljl.bilibili.user_center.vo.request.self_center.EditUserCenterPrivilegeRequest;
import ljl.bilibili.user_center.vo.response.self_center.PrivilegeResponse;
import ljl.bilibili.user_center.vo.response.self_center.SelfCenterContentResponse;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@Api(tags = "用户个人主页各模块权限开放状态查看与编辑")
@RequestMapping("/selfCenter")
@CrossOrigin(value = "*")
@Slf4j
public class SelfCenterController {
    @Resource
    SelfCenterServiceImpl selfCenterService;
    @ApiOperation("修改用户个人主页各模块权限开放状态")
    @PostMapping("/editUserPrivilege")
    public Result<Boolean> editUserPrivilege(@RequestBody EditUserCenterPrivilegeRequest editUserCenterPrivilegeRequest){
        log.info("1");
        return selfCenterService.editUserPrivilege(editUserCenterPrivilegeRequest);

    }
    @ApiOperation("获取用户个人主页各模块权限开放状态")
    @GetMapping("/getUserPrivilege/{userId}")
    public Result<PrivilegeResponse> getUserPrivilege(@PathVariable Integer userId){
        return selfCenterService.getUserPrivilege(userId);
    }

    @GetMapping("/getPersonalCenterContent/{selfId}/{visitedId}")
    @ApiOperation("获取个人主页内容")
    public Result<SelfCenterContentResponse> getPersonalVideo(@PathVariable Integer selfId, @PathVariable Integer visitedId){
        log.info("1");
        return selfCenterService.getPersonalCenterContent(selfId,visitedId);
    }
}
