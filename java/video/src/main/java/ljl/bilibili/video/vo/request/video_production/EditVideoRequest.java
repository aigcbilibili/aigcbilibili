package ljl.bilibili.video.vo.request.video_production;

import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.video.video_production.upload.Video;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Data
public class EditVideoRequest {
    @ApiModelProperty("视频id")
    private Integer id;
    @ApiModelProperty("上传的视频文件")
    private MultipartFile file;
    @ApiModelProperty("视频名称")
    private String name;
    @ApiModelProperty("视频介绍")
    private String intro;
    @ApiModelProperty("作者id")
    private Integer userId;
    @ApiModelProperty("视频封面")
    private MultipartFile cover;

    public Video toEntity() {
        Video video = new Video();
        BeanUtils.copyProperties(this, video);
        return video;
    }
    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        if(name!=null){
            map.put("name",name);
        }
        if(intro!=null){
            map.put("intro",intro);
        }
        map.put("video_id",id);
        map.put("user_id",userId);
        return map;
    }
}
