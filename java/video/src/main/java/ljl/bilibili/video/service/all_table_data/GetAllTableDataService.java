package ljl.bilibili.video.service.all_table_data;

import ljl.bilibili.entity.notice.comment.CommentNotice;
import ljl.bilibili.entity.notice.dynamic.DynamicToUser;
import ljl.bilibili.entity.notice.like.LikeNotice;
import ljl.bilibili.entity.user_center.user_info.Privilege;
import ljl.bilibili.entity.video.audience_reactions.collect.Collect;
import ljl.bilibili.entity.video.audience_reactions.collect.CollectGroup;
import ljl.bilibili.entity.video.audience_reactions.play.Play;
import ljl.bilibili.entity.video.audience_reactions.like.Like;
import ljl.bilibili.entity.video.audience_reactions.comment.Comment;
import ljl.bilibili.entity.video.audience_reactions.ensemble.AddToEnsemble;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.user_center.ensemble.VideoEnsemble;
import ljl.bilibili.entity.video.audience_reactions.danmaku.Danmaku;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.notice.comment.CommentNoticeMapper;
import ljl.bilibili.mapper.notice.dynamic.DynamicToUserMapper;
import ljl.bilibili.mapper.notice.like.LikeNoticeMapper;
import ljl.bilibili.mapper.user_center.user_info.PrivilegeMapper;
import ljl.bilibili.mapper.video.audience_reactions.collect.CollectGroupMapper;
import ljl.bilibili.mapper.video.audience_reactions.collect.CollectMapper;
import ljl.bilibili.mapper.video.audience_reactions.play.PlayMapper;
import ljl.bilibili.mapper.video.audience_reactions.like.LikeMapper;
import ljl.bilibili.mapper.video.audience_reactions.comment.CommentMapper;
import ljl.bilibili.mapper.video.audience_reactions.ensemble.AddToEnsembleMapper;
import ljl.bilibili.mapper.user_center.user_relationships.FollowMapper;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.mapper.user_center.ensemble.VideoEnsembleMapper;
import ljl.bilibili.mapper.video.audience_reactions.danmaku.DanmakuMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoDataMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
//@AllArgsConstructor

public class GetAllTableDataService {
    @Resource
    LikeNoticeMapper likeNoticeMapper;
    @Resource
    CommentNoticeMapper commentNoticeMapper;
    @Resource
    CollectMapper collectMapper;
    @Resource
    CollectGroupMapper collectGroupMapper;
    @Resource
    PlayMapper playMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    DanmakuMapper danmakuMapper;
    @Resource
    LikeMapper likeMapper;
    @Resource
    FollowMapper followMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    VideoDataMapper videoDataMapper;
    @Resource
    AddToEnsembleMapper addToEnsembleMapper;
    @Resource
    VideoEnsembleMapper videoEnsembleMapper;
    @Resource
    VideoMapper videoMapper;
    @Resource
    PrivilegeMapper privilegeMapper;
    @Resource
    DynamicToUserMapper dynamicToUserMapper;

    public List<Privilege> getPrivilege(){
        return privilegeMapper.selectList(null);
    }
    public List<LikeNotice> getLikeNotice(){
        return likeNoticeMapper.selectList(null);
    }
    public List<CommentNotice> getCommentNotice(){
        return commentNoticeMapper.selectList(null);
    }
    public String getVideo(){
        List<Video> list=videoMapper.selectList(null);
        String response="";
        for(Video video : list){
            response+=video.getName()+",";
        }
        return response;
    }

    public List<VideoEnsemble> getVideoEnsemble(){
        return videoEnsembleMapper.selectList(null);
    }

    public List<AddToEnsemble> getAddToEnsemble(){
        return addToEnsembleMapper.selectList(null);
    }

    public List<VideoData> getVideoData(){
        return videoDataMapper.selectList(null);
    }

    public List<Collect> getCollect() {
    return collectMapper.selectList(null);
    }

    public List<CollectGroup> getCollectGroup() {
        return collectGroupMapper.selectList(null);
    }

    public List<Comment> getComment() {
        return commentMapper.selectList(null);
    }

    public List<Danmaku> getDanmaku() {
        return danmakuMapper.selectList(null);
    }

    public List<Like> getCommentLike() {
        return likeMapper.selectList(null);
    }

    public List<Follow> getFollow() {
        return followMapper.selectList(null);
    }

    public List<User> getUser() {
        return userMapper.selectList(null);
    }

    public List<Play> getHistoryPlay() {
        return playMapper.selectList(null);
    }

    public List<DynamicToUser> getDynamicToUser(){
        return dynamicToUserMapper.selectList(null);
    }
}
