package ljl.bilibili.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yulichang.query.MPJQueryWrapper;
import ljl.bilibili.chat.entity.BigModelToken;
import ljl.bilibili.chat.entity.NoticeCount;
import ljl.bilibili.chat.entity.PPTWord;
import ljl.bilibili.chat.example.*;
import ljl.bilibili.chat.handler.PPTHandler;
import ljl.bilibili.chat.mapper.ChatsMapper;
import ljl.bilibili.chat.service.ChatService;
import ljl.bilibili.chat.vo.response.HistoryChatResponse;
import ljl.bilibili.chat.vo.request.AddHistoryChatRequest;
import ljl.bilibili.chat.vo.request.ChatSessionRequest;
import ljl.bilibili.chat.vo.request.ChangeChatStatusRequest;
import ljl.bilibili.chat.vo.response.ChatSessionResponse;
import ljl.bilibili.chat.vo.response.PPTResponse;
import ljl.bilibili.chat.vo.response.TempSessionResponse;
import ljl.bilibili.entity.chat.Chat;
import ljl.bilibili.entity.chat.ChatSession;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.mapper.chat.ChatMapper;
import ljl.bilibili.mapper.chat.ChatSessionMapper;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ljl.bilibili.chat.constant.Constant.imageTokenSet;
import static ljl.bilibili.chat.constant.Constant.pptTokenSet;

/**
 *聊天service
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Resource
    ChatSessionMapper chatSessionMapper;
    @Resource
    ChatMapper chatMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ChatsMapper chatsMapper;
    /**
     *修改私聊消息状态
     */
    @Override
    public Result<Boolean> changeChatStatus(ChangeChatStatusRequest changeChatStatusRequest) {
        LambdaUpdateWrapper<Chat> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Chat::getStatus, 1);
        wrapper.eq(Chat::getSenderId, changeChatStatusRequest.getReceiverId());
        wrapper.eq(Chat::getReceiverId, changeChatStatusRequest.getUserId());
        chatMapper.update(null, wrapper);
        return Result.success(true);
    }
    /**
     *修改某聊天会话最后聊天时间和最后聊天内容
     */
    @Override
    public Result<TempSessionResponse> createTempSession(Integer receiverId){
        User u=userMapper.selectById(receiverId);
        return Result.data(new TempSessionResponse().setCover(u.getCover()).setNickName(u.getNickname()));
    }
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
//        List<ChatSessionResponse> selfResponses=new ArrayList<>();
//        ChatSessionResponse chatSessionResponse;
//        if(userId==3){
//             chatSessionResponse=new ChatSessionResponse();
//            chatSessionResponse.setSessionId(7);
//            chatSessionResponse.setCover("https://labilibili.com/user-cover/Alla.jpg");
//            chatSessionResponse.setNickname("Alla");
//            chatSessionResponse.setUpdateTime(LocalDateTime.now());
//            chatSessionResponse.setUpdateContent("1");
//            chatSessionResponse.setUserId("1");
//
//        }else {
//            chatSessionResponse=new ChatSessionResponse();
//            chatSessionResponse.setSessionId(7);
//            chatSessionResponse.setCover("https://labilibili.com/user-cover/default.png");
//            chatSessionResponse.setNickname("新用户2b7ac077-2");
//            chatSessionResponse.setUpdateTime(LocalDateTime.now());
//            chatSessionResponse.setUpdateContent("1");
//            chatSessionResponse.setUserId("3");
//        }
//        selfResponses.add(chatSessionResponse);
//        MPJQueryWrapper<ChatSession> selfWrapper = new MPJQueryWrapper<>();
//        selfWrapper.eq("sender_id", userId);
//        MPJQueryWrapper<ChatSession> otherWrapper = new MPJQueryWrapper<>();
//        selfWrapper.eq("receiver_id", userId);
//        selfWrapper.innerJoin("user u on t.receiver_id=u.id");
//        otherWrapper.eq("sender_id",userId);
//        otherWrapper.innerJoin("user u on t.sender_id=u.id");
//        selfWrapper.select("u.cover", "u.nickname", "t.id as sessionId","t.update_time as updateTime", "u.id as userId", "t.update_content");
//        otherWrapper.select("u.cover", "u.nickname", "t.id as sessionId","t.update_time as updateTime", "u.id as userId", "t.update_content");
//        List<ChatSessionResponse> selfResponses = chatSessionMapper.selectJoinList(ChatSessionResponse.class, selfWrapper);
//        List<ChatSessionResponse> otherResponses = chatSessionMapper.selectJoinList(ChatSessionResponse.class, otherWrapper);
        List<ChatSessionResponse> selfResponses=chatsMapper.getSelfSession(userId);
        List<ChatSessionResponse> otherResponses=chatsMapper.getOtherSession(userId);
        selfResponses.addAll(otherResponses);
        List<Integer> idList=new ArrayList<>();
        for(ChatSessionResponse sessionResponse : selfResponses){
            idList.add(sessionResponse.getSessionId());
        }
        if(idList.size()>0){
            List<NoticeCount> noticeCounts=chatsMapper.getNoticeCounts(idList,userId);
            for(ChatSessionResponse sessionResponse : selfResponses){
                for(NoticeCount noticeCount : noticeCounts){
                    if(noticeCount.getSessionId().equals(sessionResponse.getSessionId())){
                        if(noticeCount.getNoticeCount()>0){
                            sessionResponse.setCount(noticeCount.getNoticeCount());
                            sessionResponse.setStatus(false);
                            break;
                        }
                    }
                }
            }
        }
        if(selfResponses.size()>0){
            selfResponses = selfResponses.stream()
                    .sorted(Comparator.comparing(ChatSessionResponse::getUpdateTime).reversed())
                    .collect(Collectors.toList());
        }
        return Result.data(selfResponses);
    }
    /**
     *新增聊天会话和内容
     */
    @Override
    public Result<Boolean> addChatSessionAndContent(ChatSessionRequest chatSessionRequest) {
        LambdaQueryWrapper<ChatSession> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(ChatSession::getSenderId, chatSessionRequest.getSenderId());
        wrapper1.eq(ChatSession::getReceiverId, chatSessionRequest.getReceiverId());
        LambdaQueryWrapper<ChatSession> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(ChatSession::getSenderId, chatSessionRequest.getReceiverId());
        wrapper2.eq(ChatSession::getReceiverId, chatSessionRequest.getSenderId());
        ChatSession c1=chatSessionMapper.selectOne(wrapper1);
        ChatSession c2=chatSessionMapper.selectOne(wrapper2);
        ChatSession chatSession=chatSessionRequest.toSessionEntity();
        chatSession.setUpdateTime(LocalDateTime.now());
        if ( c1== null && c2 == null) {
            chatSessionMapper.insert(chatSession);
        }else {
            chatsMapper.updateChatSession(chatSessionRequest.getUpdateContent(), LocalDateTime.now(),chatSessionRequest.getSenderId(),chatSessionRequest.getReceiverId());
        }
        chatMapper.insert(chatSessionRequest.toChatEntity().setContent(chatSession.getUpdateContent()));
        return Result.success(true);
    }
    /**
     *获取某会话历史聊天内容
     */
    @Override
    public Result<List<HistoryChatResponse>> getHistoryChat(Integer userId, Integer receiverId) {
        LambdaQueryWrapper<Chat> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Chat::getSenderId, userId);
        wrapper1.eq(Chat::getReceiverId, receiverId);
        LambdaQueryWrapper<Chat> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Chat::getReceiverId, userId);
        wrapper2.eq(Chat::getSenderId, receiverId);
        List<Chat> list1 = chatMapper.selectList(wrapper1);
        List<Chat> list2 = chatMapper.selectList(wrapper2);
        list1.addAll(list2);
        List<HistoryChatResponse> responses = new ArrayList<>();
        for (Chat chat : list1) {
            responses.add(new HistoryChatResponse(chat));
        }
        responses = responses.stream()
                .sorted(Comparator.comparing(HistoryChatResponse::getCreateTime).reversed())
                .collect(Collectors.toList());
        return Result.data(responses);
    }
    public PPTResponse getPPT(String describe) throws IOException {
        long timestamp = System.currentTimeMillis() / 1000;
        String ts = String.valueOf(timestamp);
        String appId = "", apiSecret = "", apiKey = "";
        String owner="";
        for (BigModelToken token : pptTokenSet) {
            if (!token.getUsedStatus()) {
                owner=token.getOwner();
                appId = token.getAppId();
                apiSecret = token.getApiSecret();
                apiKey = token.getApiKey();
                token.setUsedStatus(true);
                log.info(token.getOwner());
                break;
            }
        }
        // 获得鉴权信息
        ApiAuthAlgorithm auth = new ApiAuthAlgorithm();
        String signature = auth.getSignature(appId, apiSecret, timestamp);
        System.out.println(signature);
        ApiClient client = new ApiClient("https://zwapi.xfyun.cn");
        // 大纲生成
        String outlineResp = client.createOutline(appId, ts, signature, describe);
        System.out.println(outlineResp);
        CreateResponse outlineResponse = JSON.parseObject(outlineResp, CreateResponse.class);
        ObjectMapper objectMapper = new ObjectMapper();
        OutlineVo outlineVo = objectMapper.readValue(outlineResponse.getData().getOutline(), OutlineVo.class);
        List<String> chapterList = new ArrayList<>();
        Map<String, Object> chapterMap = new HashMap<>();
        int i = 1, j = 1;
        String prefix="主题是"+outlineVo.getTitle()+",副主题是"+outlineVo.getSubTitle()+",大纲是";
        for(OutlineVo.Chapter chapter : outlineVo.getChapters()){
            prefix=prefix+chapter.chapterTitle+",";
            if(chapter.chapterContents.size()>0){
                for(OutlineVo.Chapter chapter1 : chapter.chapterContents){
                    prefix=prefix+chapter1.chapterTitle+",";
                }
            }
        }
        for (OutlineVo.Chapter chapter : outlineVo.getChapters()) {
            chapterMap.put(String.valueOf(i), chapter.chapterTitle);
            chapterList.add(String.valueOf(i));
            for (OutlineVo.Chapter chapter1 : chapter.chapterContents) {
                chapterList.add(i + "." + j);
                PPTHandler pptHandler = new PPTHandler(chapter1.chapterTitle,appId,apiKey,apiSecret);
                chapterMap.put((i + "." + j), pptHandler);
                j++;
                try {
                    pptHandler.send(prefix+"现在的详细解说内容是:"+chapter1.chapterTitle);
                    while (pptHandler.pptDetail.getGenerateEnding() == false) {
                        Thread.sleep(200);
                        log.info(pptHandler.pptDetail.getThemeName() + "还没打印完");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            i++;
            j = 1;
//        }
        }
        int te = 0;
//        while(true){
//            Set<PPTHandler> set=new HashSet<>();
//            Boolean flag=true;
//            for(Map.Entry<String,Object> entry : chapterMap.entrySet()){
//                if(entry.getValue() instanceof PPTHandler){
//                    if(((PPTHandler) entry.getValue()).pptDetail.getGenerateEnding()==false){
//                        flag=false;
//                    }else {
//                        set.add((PPTHandler) entry.getValue());
//                        if(set.size()>10){
//                            log.info(String.valueOf(set.size()));
//                        }
//                    }
//                }
//            }
//            if(flag==false){
//                Thread.sleep(200);
//            }else {
//                break;
//            }
//        }
        List<PPTWord> pptWords = new ArrayList<>();
        Pattern pattern = Pattern.compile("^[1-9]$");
        for (String index : chapterList) {
            if (pattern.matcher(index).matches()) {
                pptWords.add(new PPTWord().setIndex(index).setThemeName((String) chapterMap.get(index)));
            } else {
                PPTHandler pptHandler = (PPTHandler) chapterMap.get(index);
                pptWords.add(new PPTWord().setIndex(index).setThemeName(pptHandler.pptDetail.getThemeName()).setText(pptHandler.pptDetail.getText()));
            }
            if (chapterMap.get(index) instanceof String) {
                log.info((String) chapterMap.get(index));
            } else {
                PPTHandler pptHandler = (PPTHandler) chapterMap.get(index);
                log.info(pptHandler.pptDetail.getText());
            }

        }
        for(BigModelToken token : pptTokenSet){
            if(token.getOwner().equals(owner)){
                token.setUsedStatus(false);
                break;
            }
        }
        return new PPTResponse().setPptWordList(pptWords).setCoverImgSrc(outlineResponse.getData().getCoverImgSrc());
    }
    public String getImage(String text) throws Exception {
        String url="https://spark-api.cn-huabei-1.xf-yun.com/v2.1/tti";
        String appId="",apiSecret="",apiKey="";
        log.info(imageTokenSet.size()+"");
        for(BigModelToken token : imageTokenSet){
            log.info(token.toString());
            if(!token.getUsedStatus()){
                appId=token.getAppId();
                apiSecret=token.getApiSecret();
                apiKey=token.getApiKey();
                token.setUsedStatus(true);
                log.info(token.getOwner());
                break;
            }
        }
        String authUrl= MyUtil.getAuthUrl(url,apiKey,apiSecret);
        String json = "{\n" +
                "  \"header\": {\n" +
                "    \"app_id\": \"" + appId + "\",\n" +
                "    \"uid\": \"" + UUID.randomUUID().toString().substring(0, 15) + "\"\n" +
                "  },\n" +
                "  \"parameter\": {\n" +
                "    \"chat\": {\n" +
                "      \"domain\": \"s291394db\",\n" +
                "      \"temperature\": 0.5,\n" +
                "      \"max_tokens\": 4096,\n" +
                "      \"width\": 1024,\n" +
                "      \"height\": 1024\n" +
                "    }\n" +
                "  },\n" +
                "  \"payload\": {\n" +
                "    \"message\": {\n" +
                "      \"text\": [\n" +
                "        {\n" +
                "          \"role\": \"user\",\n" +
                "           \"content\": \"" + text + "\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String res = MyUtil.doPostJson(authUrl, null, json);
//        System.out.println(res);
        return res;
    }
}
