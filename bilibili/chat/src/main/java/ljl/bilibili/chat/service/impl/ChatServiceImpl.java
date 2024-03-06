package ljl.bilibili.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import ljl.bilibili.chat.service.ChatService;
import ljl.bilibili.chat.vo.response.HistoryChatResponse;
import ljl.bilibili.chat.vo.request.AddHistoryChatRequest;
import ljl.bilibili.chat.vo.request.ChatSessionRequest;
import ljl.bilibili.chat.vo.request.ChangeChatStatusRequest;
import ljl.bilibili.chat.vo.response.ChatSessionResponse;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.chat.ChatSession;
import ljl.bilibili.mapper.chat.ChatMapper;
import ljl.bilibili.mapper.chat.ChatSessionMapper;
import ljl.bilibili.util.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 *聊天service
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    ChatSessionMapper chatSessionMapper;
    @Resource
    ChatMapper chatMapper;
    /**
     *修改私聊消息状态
     */
    @Override
    public Result<Boolean> changeChatStatus(ChangeChatStatusRequest changeChatStatusRequest) {
        LambdaUpdateWrapper<Chat> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Chat::getStatus, 1);
        wrapper.eq(Chat::getSenderId, changeChatStatusRequest.getSenderId());
        wrapper.eq(Chat::getReceiverId, changeChatStatusRequest.getUserId());
        chatMapper.update(null, wrapper);
        return Result.success(true);
    }
    /**
     *修改某聊天会话最后聊天时间和最后聊天内容
     */
    @Override
    public Result<Boolean> changeChatSessionTime(ChatSessionRequest chatSessionRequest) {
        LambdaUpdateWrapper<ChatSession> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChatSession::getSenderId, chatSessionRequest.getSenderId());
        wrapper.eq(ChatSession::getReceiverId, chatSessionRequest.getReceiverId());
        wrapper.set(ChatSession::getUpdateTime, LocalDateTime.now());
        wrapper.set(ChatSession::getUpdateContent, chatSessionRequest.getUpdateContent());
        chatSessionMapper.update(null, wrapper);
        return Result.success(true);
    }
    /**
     *获取历史会话列表，若发起方未发消息则发起方可以获取到会话但接收方无法获取
     */
    @Override
    public Result<List<ChatSessionResponse>> getHistoryChatSession(Integer userId) {
        MPJQueryWrapper<ChatSession> selfWrapper = new MPJQueryWrapper<>();
        selfWrapper.eq("sender_id", userId);
        selfWrapper.innerJoin("user u on t.receiver_id=u.id");
        selfWrapper.select("u.cover", "u.nickname", "t.id as sessionId","t.update_time as updateTime", "u.id as userId", "t.update_content");
        List<ChatSessionResponse> selfResponses = chatSessionMapper.selectJoinList(ChatSessionResponse.class, selfWrapper);
        MPJQueryWrapper<ChatSession> receiverWrapper = new MPJQueryWrapper<>();
        receiverWrapper.eq("receiver_id", userId);
        receiverWrapper.eq("status",1);
        receiverWrapper.innerJoin("user u on t.sender_id=u.id");
        receiverWrapper.select("u.cover", "u.nickname", "t.id as sessionId","t.update_time as updateTime", "u.id as userId", "t.update_content");
        List<ChatSessionResponse> receiverResponses = chatSessionMapper.selectJoinList(ChatSessionResponse.class, receiverWrapper);
        selfResponses.addAll(receiverResponses);
        selfResponses = selfResponses.stream()
                .sorted(Comparator.comparing(ChatSessionResponse::getUpdateTime).reversed())
                .collect(Collectors.toList());
        return Result.data(selfResponses);
    }
    /**
     *新增聊天会话，未发消息前属于单向可见性会话
     */
    @Override
    public Result<Boolean> addChatSession(ChatSessionRequest chatSessionRequest) {
        LambdaQueryWrapper<ChatSession> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(ChatSession::getSenderId, chatSessionRequest.getSenderId());
        wrapper1.eq(ChatSession::getReceiverId, chatSessionRequest.getReceiverId());
        LambdaQueryWrapper<ChatSession> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(ChatSession::getSenderId, chatSessionRequest.getReceiverId());
        wrapper2.eq(ChatSession::getReceiverId, chatSessionRequest.getSenderId());
        if (chatSessionMapper.selectOne(wrapper1) == null && chatSessionMapper.selectOne(wrapper2) != null) {
            chatSessionMapper.insert(chatSessionRequest.toEntity());
        }
        return Result.success(true);
    }
    /**
     *获取某会话历史聊天内容
     */
    @Override
    public Result<List<HistoryChatResponse>> getHistoryChat(Integer userId, Integer receiverId) {
        LambdaQueryWrapper<Chat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Chat::getSenderId, userId);
        wrapper.eq(Chat::getReceiverId, receiverId);
        LambdaQueryWrapper<Chat> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Chat::getSenderId, receiverId);
        wrapper1.eq(Chat::getReceiverId, userId);
        List<Chat> list = chatMapper.selectList(wrapper);
        List<Chat> list1 = chatMapper.selectList(wrapper1);
        list.addAll(list1);
        List<HistoryChatResponse> responses = new ArrayList<>();
        for (Chat chat : list) {
            responses.add(new HistoryChatResponse(chat));
        }
        responses = responses.stream()
                .sorted(Comparator.comparing(HistoryChatResponse::getCreateTime).reversed())
                .collect(Collectors.toList());
        return Result.data(responses);
    }
    /**
     *新增聊天内容
     */
    @Override
    public Result<Boolean> addHistoryChat(AddHistoryChatRequest addHistoryChatRequest) {
        chatMapper.insert(addHistoryChatRequest.toEntity());
        LambdaUpdateWrapper<ChatSession> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(ChatSession::getId,addHistoryChatRequest.getSessionId());
        wrapper.set(ChatSession::getStatus,1);
        chatSessionMapper.update(null,wrapper);
        return Result.success(true);
    }
}
