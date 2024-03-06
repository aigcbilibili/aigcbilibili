package ljl.bilibili.chat.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.chat.service.ChatService;
import ljl.bilibili.chat.vo.response.HistoryChatResponse;
import ljl.bilibili.chat.vo.request.AddHistoryChatRequest;
import ljl.bilibili.chat.vo.request.ChatSessionRequest;
import ljl.bilibili.chat.vo.response.ChatSessionResponse;
import ljl.bilibili.chat.vo.request.ChangeChatStatusRequest;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chat")
@Api(tags = "获取历史聊天记录和聊天对象列表")
@Slf4j
public class ChatController {
    @Resource
    ChatService chatService;
    @GetMapping("/getHistoryChat/{userId}/{receiverId}")
    @ApiOperation("获取历史聊天记录")
    public Result<List<HistoryChatResponse>> getHistoryChat(@PathVariable Integer userId, @PathVariable Integer receiverId){
        log.info("1");
     return chatService.getHistoryChat(userId,receiverId);
    }
    @ApiOperation("获取历史聊天会话列表")
    @GetMapping("/getHistoryChatSession/{userId}")
    public Result<List<ChatSessionResponse>> getHistoryChatSession(@PathVariable Integer userId){
        log.info("1");
        return chatService.getHistoryChatSession(userId);
    }
    @ApiOperation("修改聊天记录的状态从未读到已读")
    @PostMapping("/changeChatStatus")
    public Result<Boolean> changeChatStatus(@RequestBody ChangeChatStatusRequest changeChatStatusRequest){
        log.info("1");
        return chatService.changeChatStatus(changeChatStatusRequest);
    }
    @ApiOperation("修改聊天会话的最后聊天时间和最后聊天内容")
    @PostMapping("/changeChatSessionTime")
    public Result<Boolean> changeChatSessionTime(@RequestBody ChatSessionRequest chatSessionRequest){
        log.info("1");
        return chatService.changeChatSessionTime(chatSessionRequest);
    }
    @ApiOperation("新增聊天记录")
    @PostMapping("/addHistoryChat")
    public Result<Boolean> addHistoryChat(@RequestBody AddHistoryChatRequest addHistoryChatRequest){
        log.info("1");
        return chatService.addHistoryChat(addHistoryChatRequest);
    }
    @ApiOperation("/新增聊天会话")
    @PostMapping("/addChatSession")
    public Result<Boolean> addChatSession(@RequestBody ChatSessionRequest chatSessionRequest){
        log.info("1");
        return chatService.addChatSession(chatSessionRequest);
    }
}
