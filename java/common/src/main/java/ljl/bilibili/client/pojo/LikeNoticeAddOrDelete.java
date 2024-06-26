package ljl.bilibili.client.pojo;

import ljl.bilibili.entity.notice.like.LikeNotice;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LikeNoticeAddOrDelete {

    private Integer id;

    private Integer senderId;

    private Integer videoId;

    private Integer commentId;

    private LocalDateTime createTime;

    private Integer status;

    private Integer type;
    public LikeNotice toNotice(){
        LikeNotice likeNotice=new LikeNotice();
        BeanUtils.copyProperties(this,likeNotice);
        return likeNotice;
    }
}
