package ljl.bilibili.video.controller.all_table_data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.entity.notice.comment.CommentNotice;
import ljl.bilibili.entity.notice.dynamic.DynamicToUser;
import ljl.bilibili.entity.notice.like.LikeNotice;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.entity.video.audience_reactions.collect.Collect;
import ljl.bilibili.entity.video.audience_reactions.collect.CollectGroup;
import ljl.bilibili.entity.video.audience_reactions.play.Play;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.audience_reactions.comment.Comment;
import ljl.bilibili.entity.video.audience_reactions.ensemble.AddToEnsemble;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import ljl.bilibili.entity.user_center.ensemble.VideoEnsemble;
import ljl.bilibili.entity.video.audience_reactions.danmaku.Danmaku;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.video.service.all_table_data.GetAllTableDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "查看所有操作对应的表的数据")
@RequestMapping("/getAllTableData")
@Slf4j
public class GetAllController {
    @Resource
    GetAllTableDataService getAllTableDataService;
    @GetMapping("/getPrivilege")
    @ApiOperation("查看所有个人主页开放权限")
    public List<Privilege> getPrivilege(){
        log.info("1");
        return getAllTableDataService.getPrivilege();
    }
    @GetMapping("/caculate")
    public void caculate(){

    }
    @GetMapping("/getLikeNotice")
    @ApiOperation("查看所有点赞动态")
    public List<LikeNotice> getLikeNotice(){
        log.info("1");
        return getAllTableDataService.getLikeNotice();
    }
    @GetMapping("/getCommentNotice")
    @ApiOperation("查看所有评论动态")
    public List<CommentNotice> getCommentNotice(){
        log.info("1");
        return getAllTableDataService.getCommentNotice();
    }
    @GetMapping("/getVideo")
    @ApiOperation("查看所有视频")
    public String getVideo(){
        return getAllTableDataService.getVideo();
    }
    @GetMapping("/getVideoEnsemble")
    @ApiOperation("查看视频合集")
    public List<VideoEnsemble> getVideoEnsemble(){
        log.info("1");
        return getAllTableDataService.getVideoEnsemble();
    }
    @GetMapping("/getAddToEnsemble")
    @ApiOperation("查看添加视频合集记录")
    public List<AddToEnsemble> getAddToEnsemble(){
        log.info("1");
        return getAllTableDataService.getAddToEnsemble();
    }
    @GetMapping("/getVideoData")
    @ApiOperation("查看视频数据表")
    public List<VideoData> getVideoData() {
        log.info("1");
        return getAllTableDataService.getVideoData();
    }
    @GetMapping("/getCollectGroup")
    @ApiOperation("查看收藏夹表")
    public List<CollectGroup> getCollectGroup() {
        log.info("1");
        return getAllTableDataService.getCollectGroup();
    }

    @GetMapping("/getComment")
    @ApiOperation("查看评论表")
    public List<Comment> getComment() {
        log.info("1");
        return getAllTableDataService.getComment();
    }

    @GetMapping("/getDanmaku")
    @ApiOperation("查看弹幕表")
    public List<Danmaku> getDanmaku() {
        log.info("1");
        return getAllTableDataService.getDanmaku();
    }

    @GetMapping("/getCommentLike")
    @ApiOperation("查看点赞表")
    public List<Like> getCommentLike() {
        log.info("1");
        return getAllTableDataService.getCommentLike();
    }

    @GetMapping("/getFollow")
    @ApiOperation("查看关注表")
    public List<Follow> getFollow() {
        log.info("1");
        return getAllTableDataService.getFollow();
    }

    @GetMapping("/getUser")
    @ApiOperation("查看用户表")
    public List<User> getUser() {
        log.info("1");
        return getAllTableDataService.getUser();
    }

    @GetMapping("/getHistoryPlay")
    @ApiOperation("查看历史播放表")
    public List<Play> getHistoryPlay() {
        log.info("1");
        return getAllTableDataService.getHistoryPlay();
    }

    @GetMapping("/getCollect")
    @ApiOperation("查看收藏表")
    public List<Collect> getCollect() {
        log.info("1");
        return getAllTableDataService.getCollect();
    }
    @GetMapping("/getDynamicToUser")
    @ApiOperation("查看推送至用户的动态表")
    public List<DynamicToUser> getDynamicToUser(){
        return getAllTableDataService.getDynamicToUser();
    }

}
