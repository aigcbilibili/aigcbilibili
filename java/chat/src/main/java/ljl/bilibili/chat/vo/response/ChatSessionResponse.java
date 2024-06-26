package ljl.bilibili.chat.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import ljl.bilibili.serializer.RemoveTSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSessionResponse {
    @ApiModelProperty("聊天对象昵称")
    private String nickname;
    @ApiModelProperty("聊天对象头像")
    private String cover;
    @ApiModelProperty("该对象id")
    private String userId;
    @ApiModelProperty("最后一次聊天时间")
    @JsonSerialize(using = RemoveTSerializer.class)
    private LocalDateTime updateTime;
    @ApiModelProperty("最后一次聊天内容")
    private String updateContent;
    @ApiModelProperty("该会话id")
    private Integer sessionId;
    @ApiModelProperty("消息读取状态")
    private Boolean status=true;
    @ApiModelProperty("未读消息数")
    private Integer count=0;
}
