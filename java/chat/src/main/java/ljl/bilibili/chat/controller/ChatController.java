package ljl.bilibili.chat.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.chat.service.ChatService;
import ljl.bilibili.chat.vo.response.HistoryChatResponse;
import ljl.bilibili.chat.vo.request.ChatSessionRequest;
import ljl.bilibili.chat.vo.response.ChatSessionResponse;
import ljl.bilibili.chat.vo.request.ChangeChatStatusRequest;
import ljl.bilibili.chat.vo.response.PPTResponse;
import ljl.bilibili.chat.vo.response.TempSessionResponse;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/chat")
@Api(tags = "获取历史聊天记录和聊天对象列表")
@Slf4j
public class ChatController {
    @Resource
    ChatService chatService;

    @GetMapping("/getPPT/{describe}")
    public PPTResponse getPPT(@PathVariable String describe) throws IOException, InterruptedException {
        return chatService.getPPT(describe);
        }

    @GetMapping("/getImage/{text}")
    public String getImage(@PathVariable String text) throws Exception {
        return chatService.getImage(text);
    }
    @GetMapping("/createTempSession/{receiverId}")
    @ApiOperation("创建临时会话")
    public Result<TempSessionResponse> createTempSession(@PathVariable Integer receiverId){
        return chatService.createTempSession(receiverId);
    }
    @GetMapping("/getHistoryChat/{userId}/{receiverId}")
    @ApiOperation("获取历史聊天记录")
    public Result<List<HistoryChatResponse>> getHistoryChat(@PathVariable Integer userId, @PathVariable Integer receiverId){
        
     return chatService.getHistoryChat(userId,receiverId);
    }
    @ApiOperation("获取历史聊天会话列表")
    @GetMapping("/getHistoryChatSession/{userId}")
    public Result<List<ChatSessionResponse>> getHistoryChatSession(@PathVariable Integer userId){
        
        return chatService.getHistoryChatSession(userId);
    }
    @ApiOperation("修改聊天记录的状态从未读到已读")
    @PostMapping("/changeChatStatus")
    public Result<Boolean> changeChatStatus(@RequestBody ChangeChatStatusRequest changeChatStatusRequest){
        
        return chatService.changeChatStatus(changeChatStatusRequest);
    }
    @ApiOperation("修改聊天会话的最后聊天时间和最后聊天内容")
    @PostMapping("/changeChatSessionTime")
    public Result<Boolean> changeChatSessionTime(@RequestBody ChatSessionRequest chatSessionRequest){
        
        return chatService.changeChatSessionTime(chatSessionRequest);
    }
    @ApiOperation("/新增聊天会话和聊天内容")
    @PostMapping("/addChatSessionAndContent")
    public Result<Boolean> addChatSessionAndContent(@RequestBody ChatSessionRequest chatSessionRequest){
        
        return chatService.addChatSessionAndContent(chatSessionRequest);
    }
}
