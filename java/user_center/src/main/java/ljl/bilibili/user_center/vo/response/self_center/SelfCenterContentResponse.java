package ljl.bilibili.user_center.vo.response.self_center;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.user_center.vo.response.follow.IdolOrFansListResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelfCenterContentResponse {
    @ApiModelProperty("投稿的视频")
    private List<SelfVideoResponse> selfVideoResponse;
    @ApiModelProperty("收藏的视频")
    private List<SelfCollectResponse> selfCollectResponse;
    @ApiModelProperty("最近点赞视频")
    private List<RemotelyLikeVideoResponse> remotelyLikeVideoResponse;
    @ApiModelProperty("粉丝列表")
    private List<IdolOrFansListResponse> fansListResponse=new ArrayList<>();
    @ApiModelProperty("关注列表")
    private List<IdolOrFansListResponse> idolListResponse=new ArrayList<>();
}
