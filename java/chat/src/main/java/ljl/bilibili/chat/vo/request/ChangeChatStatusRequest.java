package ljl.bilibili.chat.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangeChatStatusRequest {
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("聊天会话另一头的用户id")
    private Integer receiverId;
}
