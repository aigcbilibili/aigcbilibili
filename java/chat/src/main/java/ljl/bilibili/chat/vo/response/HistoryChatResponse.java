package ljl.bilibili.chat.vo.response;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class HistoryChatResponse {
    @ApiModelProperty("聊天的内容")
    private String content;
    @ApiModelProperty("这条消息的发送者id")
    private Integer senderId;
    @JsonSerialize(using = RemoveTSerializer.class)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("该条聊天记录的id")
    private Integer id;
    @ApiModelProperty("该条记录的已读和未读状态")
    private Integer status;
    public HistoryChatResponse(Chat chat){
        BeanUtils.copyProperties(chat,this);
    }
}
