package ljl.bilibili.video.vo.response.audience_reactions.play;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.client.pojo.RecommendVideo;
import ljl.bilibili.serializer.RelativeTimeSerializer;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data

public class CommendVideoResponse {

    Integer videoId;

    Integer authorId;

    String videoName;

    String authorName;
    String intro;
    @JsonSerialize(using = RemoveTSerializer.class)
    LocalDateTime createTime;
    String url;
    String length;
    String cover;

    Integer playCount;

    Integer danmakuCount;

    Integer collectCount;
    public CommendVideoResponse(RecommendVideo recommendVideo){
        BeanUtils.copyProperties(recommendVideo,this);
    }
}
