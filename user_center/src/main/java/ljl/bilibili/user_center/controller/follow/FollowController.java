package ljl.bilibili.user_center.controller.follow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.user_center.service.follow.impl.FollowServiceImpl;
import ljl.bilibili.user_center.vo.response.follow.IdolOrFansListResponse;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ljl.bilibili.user_center.vo.request.follow.FollowRequest;
import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "关注与取关up主")
@RequestMapping("/follow")
@Slf4j
public class FollowController {
    @Resource
    FollowServiceImpl followService;
    @PostMapping("/follow")
    @ApiOperation("关注用到，自己的id和要关注的用户的id")
    public Result<Boolean> follow(@RequestBody FollowRequest followRequest) {
        log.info("1");
        return followService.follow(followRequest);
    }

    @ApiOperation("取关用到自己的id和要取消关注的用户的id")
    @PostMapping("/recallFollow")
    public Result<Boolean> recallFollow(@RequestBody FollowRequest followRequest) {
        log.info("1");
        return followService.recallFollow(followRequest);
    }
}
