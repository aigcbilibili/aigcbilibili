package ljl.bilibili.video.controller.audience_reactions.collect;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.entity.video.audience_reactions.collect.CollectGroup;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.collect.CollectService;
import ljl.bilibili.video.service.audience_reactions.collect.impl.CollectServiceImpl;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectGroupRequest;
import ljl.bilibili.video.vo.request.audience_reactions.collect.CollectRequest;
import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectGroupResponse;
import ljl.bilibili.video.vo.response.audience_reactions.collect.CollectVideoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "收藏和收藏夹的增删改查")
@RequestMapping("/collect")
@CrossOrigin(value = "*")
@Slf4j
public class CollectController {
    @Resource
    CollectService collectService;
    @PostMapping("/collect")
    @ApiOperation("收藏视频")
    public Result<Boolean> collect(@RequestBody List<CollectRequest> collectRequest) {
        log.info("收藏视频");
        log.info(collectRequest.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
       return collectService.collect(collectRequest);

    }
    @GetMapping("/getVideoToCollectGroup/{userId}/{videoId}")
    @ApiOperation("获取视频页面收藏夹")
    public Result<List<CollectGroupResponse>> getVideoToCollectGroup(@PathVariable Integer userId, @PathVariable Integer videoId){
        return collectService.getVideoToCollectGroup(userId,videoId);
    }

    @PostMapping("/editCollectGroup")
    @ApiOperation("创建、修改收藏夹")
    public Result<Boolean> editCollectGroup(@RequestBody CollectGroupRequest createCollectGroupRequest) {
        log.info("创建修改收藏夹");
        log.info(createCollectGroupRequest.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return collectService.editCollectGroup(createCollectGroupRequest);
    }
    @PostMapping("/deleteCollectGroup")
    @ApiOperation("删除收藏夹")
    public Result<Boolean> deleteCollectGroup(@RequestBody CollectGroupRequest createCollectGroupRequest) {
        log.info("删除收藏夹");
        log.info(createCollectGroupRequest.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return collectService.deleteCollectGroup(createCollectGroupRequest);
    }
    @GetMapping("/getCollectGroup/{userId}")
    @ApiOperation("获取首页收藏夹列表")
    public Result<List<CollectGroup>> getCollectGroup(@PathVariable Integer userId) {
        log.info("获取收藏夹列表");
        log.info(userId.toString());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        return collectService.getCollectGroup(userId);
    }
    @GetMapping("/getCollectVideo/{collectGroupId}")
    @ApiOperation("获取首页某个收藏夹下的收藏夹视频")
    public Result<List<CollectVideoResponse>> getCollectVideo(@PathVariable Integer collectGroupId) {
        return collectService.getCollectVideo(collectGroupId);
    }
}
