package ljl.bilibili.chat.service;

import ljl.bilibili.chat.vo.request.AddHistoryChatRequest;
import ljl.bilibili.chat.vo.request.ChangeChatStatusRequest;
import ljl.bilibili.chat.vo.request.ChatSessionRequest;
import ljl.bilibili.chat.vo.response.ChatSessionResponse;
import ljl.bilibili.chat.vo.response.HistoryChatResponse;
import ljl.bilibili.chat.vo.response.PPTResponse;
import ljl.bilibili.chat.vo.response.TempSessionResponse;
import ljl.bilibili.util.Result;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

public interface ChatService {
    Result<Boolean> changeChatStatus(ChangeChatStatusRequest changeChatStatusRequest);

    Result<Boolean> changeChatSessionTime(ChatSessionRequest chatSessionRequest);

    Result<List<ChatSessionResponse>> getHistoryChatSession(Integer userId);

    Result<Boolean> addChatSessionAndContent(ChatSessionRequest chatSessionRequest);

    Result<List<HistoryChatResponse>> getHistoryChat(Integer userId, Integer receiverId);



    Result<TempSessionResponse> createTempSession(Integer receiverId);
    PPTResponse getPPT(String describe) throws IOException;
     String getImage( String text) throws Exception;
}
