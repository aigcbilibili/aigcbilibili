package ljl.bilibili.video.service.audience_reactions.danmaku.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.entity.video.audience_reactions.danmaku.Danmaku;
import ljl.bilibili.mapper.video.audience_reactions.danmaku.DanmakuMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoDataMapper;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.service.audience_reactions.danmaku.DanmakuService;
import ljl.bilibili.video.vo.request.audience_reactions.danmaku.AddDanmakuRequest;
import ljl.bilibili.video.vo.response.audience_reactions.comment.CommentResponse;
import ljl.bilibili.video.vo.response.audience_reactions.danmaku.DanmakuResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 *弹幕
 */
@Service
public class DanmakuServiceImpl implements DanmakuService {
    @Resource
    DanmakuMapper danmakuMapper;
    /**
     *新增弹幕
     */

    @Override
    public Result<Boolean> addDanmaku(AddDanmakuRequest addDanmakuRequest) {
        danmakuMapper.insert(addDanmakuRequest.toEntity());
        return Result.success(true);
    }
    /**
     *获取弹幕
     */
    @Override
    public Result<List<DanmakuResponse>> getDanmaku(Integer videoId) {
        LambdaQueryWrapper<Danmaku> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Danmaku::getVideoId,videoId);
        List<Danmaku> danmakus=danmakuMapper.selectList(queryWrapper);
        List<DanmakuResponse> danmakuResponseList = new ArrayList<>();
        for(Danmaku danmaku : danmakus){
            danmakuResponseList.add(new DanmakuResponse(danmaku));
        }
        return Result.data(danmakuResponseList);
    }

}
