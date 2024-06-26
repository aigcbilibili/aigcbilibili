package ljl.bilibili.chat.controller;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.chat.entity.BigModelToken;
import ljl.bilibili.chat.entity.PPTDetail;
import ljl.bilibili.chat.entity.PPTWord;
import ljl.bilibili.chat.example.*;
import ljl.bilibili.chat.handler.PPTHandler;
import ljl.bilibili.chat.service.ChatService;
import ljl.bilibili.chat.vo.response.HistoryChatResponse;
import ljl.bilibili.chat.vo.request.AddHistoryChatRequest;
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
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ljl.bilibili.chat.constant.Constant.*;

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
    @ApiOperation("/新增聊天会话和聊天内容")
    @PostMapping("/addChatSessionAndContent")
    public Result<Boolean> addChatSessionAndContent(@RequestBody ChatSessionRequest chatSessionRequest){
        log.info("1");
        return chatService.addChatSessionAndContent(chatSessionRequest);
    }
}
