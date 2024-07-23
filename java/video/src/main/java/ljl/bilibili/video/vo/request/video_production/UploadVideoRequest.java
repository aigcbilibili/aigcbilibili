package ljl.bilibili.video.vo.request.video_production;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.notice.dynamic.Dynamic;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.video.video_production.upload.Video;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadVideoRequest {
    @ApiModelProperty("视频路径")
    private String url;
    @ApiModelProperty("视频名称")
    private String name;
    @ApiModelProperty("视频介绍")
    private String intro;
    @ApiModelProperty("作者id")
    private int userId;
    @ApiModelProperty("视频封面")
    private String videoCover;
    public Video toEntity() {
        Video video = new Video();
        BeanUtils.copyProperties(this, video);
        return video;
    }


    public Dynamic toNoCoverDynamic(User user, Video video){
        return new Dynamic().setVideoId(video.getId()).setVideoName(video.getName())
                .setAuthorId(user.getId());
    }
    public Dynamic toCoverDynamic(User user, Video video){
        return new Dynamic().setVideoId(video.getId()).setVideoCover(video.getCover()).setVideoName(video.getName())
                .setAuthorId(user.getId());
    }
}
