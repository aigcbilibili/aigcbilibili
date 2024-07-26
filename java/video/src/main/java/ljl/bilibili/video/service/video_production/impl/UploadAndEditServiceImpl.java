package ljl.bilibili.video.service.video_production.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.minio.errors.*;
import ljl.bilibili.client.file.CustomMultipartFile;
import ljl.bilibili.client.notice.SendNoticeClient;
import ljl.bilibili.client.pojo.UploadVideo;
import ljl.bilibili.entity.user_center.user_info.User;
import ljl.bilibili.entity.video.video_production.upload.Video;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import ljl.bilibili.mapper.user_center.user_info.UserMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoDataMapper;
import ljl.bilibili.mapper.video.video_production.upload.VideoMapper;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.pojo.UploadPart;
import ljl.bilibili.video.service.video_production.UploadAndEditService;
import ljl.bilibili.video.vo.request.video_production.DeleteVideoRequest;
import ljl.bilibili.video.vo.request.video_production.EditVideoRequest;
import ljl.bilibili.video.vo.request.video_production.UploadPartRequest;
import ljl.bilibili.video.vo.request.video_production.UploadVideoRequest;
import ljl.bilibili.video.vo.response.video_production.UploadProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static ljl.bilibili.video.constant.Constant.*;
import static ljl.bilibili.video.constant.Constant.uploadPartMap;
//上传视频
@Slf4j
@Service
public class UploadAndEditServiceImpl implements UploadAndEditService {
    @Resource
    MinioServiceImpl minioService;
    @Resource
    VideoMapper videoMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    SendNoticeClient client;
    @Resource
    VideoDataMapper videoDataMapper;
    AtomicBoolean uploadVideoSuccess = new AtomicBoolean(true);
    @Resource
    RedisTemplate objectRedisTemplate;

    /**
     * 上传总片
     */
    @Override
    @Transactional
    public Result<Boolean> uploadTotal(UploadVideoRequest uploadVideoRequest) {
        try {
            //将请求中的视频参数封装一部分
            String path = Files.createTempDirectory(".tmp").toString();
            Video video = uploadVideoRequest.toEntity();
            LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
            String coverFile = uploadVideoRequest.getVideoCover();
            String url = "https://labilibili.com/video/" + uploadVideoRequest.getUrl();
            video.setUrl(url);
            //如果视频封面不为空那么直接上传视频
            if ((coverFile != null&&coverFile!="") && uploadPartMap.get(uploadVideoRequest.getUserId()) == null) {
//                hasCover=true;
                String prefixPath = "https://labilibili.com/video-cover/";
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(coverFile);
                String coverFileName = video.getName() + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
                String imgContentType = "image/jpeg";
                video.setCover(prefixPath + coverFileName);
                CustomMultipartFile coverMultipartFile = new CustomMultipartFile(decodedBytes, coverFileName, imgContentType);
                queryWrapper.eq(Video::getCover, UUID.randomUUID().toString().substring(0, 8) + coverFileName);
                CompletableFuture<Void> uploadImgFuture = CompletableFuture.runAsync(() -> minioService.uploadImgFile(coverFileName, coverMultipartFile.getInputStream(), imgContentType)).handle((result, ex) -> {
                    //异步回调通知异步任务是否执行成功
                    if (ex != null) {
                        uploadVideoSuccess.set(false);
                        log.error("uploadImgFail");
                    }
                    return null;
                });
                //发送转码通知并设置已有封面
                CompletableFuture thenRunFuture = uploadImgFuture.thenRun(() ->
                        client.sendUploadNotice(new UploadVideo().setVideoId(video.getId()).setVideoName(video.getName()).setUrl(url).setHasCover(true)));
                CompletableFuture<Void> sendNoticeFuture = CompletableFuture.runAsync(() -> {
                    User user = userMapper.selectById(uploadVideoRequest.getUserId());
                    //发送生成关注up主动态视频的通知
                    client.dynamicNotice(uploadVideoRequest.toCoverDynamic(user, video));
                    log.info("dynamicUpload");
                }).handle((result, ex) -> {
                    if (ex != null) {
                        log.error("dynamicFail");
                        uploadVideoSuccess.set(false);
                    }
                    return null;
                });
            } else {
                //发送生成关注up主动态视频的通知
                CompletableFuture thenRunFuture = CompletableFuture.runAsync(() ->
                        client.sendUploadNotice(new UploadVideo().setVideoId(video.getId()).setVideoName(video.getName()).setUrl(url).setHasCover(false)));
                CompletableFuture<Void> sendNoticeFuture = CompletableFuture.runAsync(() -> {
                    User user = userMapper.selectById(uploadVideoRequest.getUserId());
                    client.dynamicNotice(uploadVideoRequest.toNoCoverDynamic(user, video));
                }).handle((result, ex) -> {
                    if (ex != null) {
                        log.error("dynamicfail");
                        uploadVideoSuccess.set(false);
                    }
                    return null;
                });
            }
            videoMapper.insert(video);
            videoDataMapper.insert(new VideoData().setVideoId(video.getId()));
            //发送MySQL与es数据同步的消息
            CompletableFuture<Void> sendDBChangeNotice = CompletableFuture.runAsync(() -> {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaTimeModule module = new JavaTimeModule();
                // 设置LocalDateTime的序列化方式
                LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
                objectMapper.registerModule(module);
                Map<String, Object> map = objectMapper.convertValue(video, Map.class);
                map.put(TABLE_NAME, VIDEO_TABLE_NAME);
                map.put(OPERATION_TYPE, OPERATION_TYPE_ADD);
                map.put(VIDEO_ID, map.get(TABLE_ID));
                map.remove(TABLE_ID);
                client.sendDBChangeNotice(map);
            });
            return Result.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("寄");
        }
    }
    /**
     * 编辑视频并发送数据同步消息
     */
    @Override
    public Result<Boolean> edit(EditVideoRequest editVideoRequest) {
        try {
            Map<String, Object> map = editVideoRequest.toMap();
            MultipartFile videoFile = editVideoRequest.getFile();
            ObjectMapper objectMapper = new ObjectMapper();
            JavaTimeModule module = new JavaTimeModule();
            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            objectMapper.registerModule(module);
            map.put(TABLE_NAME, VIDEO_TABLE_NAME);
            map.put(OPERATION_TYPE, OPERATION_TYPE_UPDATE);
            Video video = editVideoRequest.toEntity();
            //如果视频文件不为空，则该视频连视频文件都修改了
            if (videoFile != null) {
                String videoUrl = UUID.randomUUID().toString().substring(0, 10) + editVideoRequest.getName();
                video.setUrl(videoUrl);
                map.put(VIDEO_URL, videoUrl);
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        minioService.uploadVideoFile(videoUrl, videoFile.getInputStream(), videoFile.getContentType());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
                //异步发送转码消息时查询redis进行限流，如果已有三个正在进行的转码则不不发送转码消息等待转码完毕再发送
                CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
                    while (true) {
                        String key = "encode-count";
                        Integer count = (Integer) objectRedisTemplate.opsForValue().get(key);
                        if (count < 3) {
                            client.sendUploadNotice(new UploadVideo().setVideoId(video.getId()).setUrl(videoUrl).setVideoName(video.getName()));
                            count++;
                            objectRedisTemplate.opsForValue().set(key, count);
                            break;
                        } else {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                });
            }
            MultipartFile coverFile = editVideoRequest.getCover();
            //如果视频文件为空则只需要修改视频相关信息
            if (coverFile != null) {
                CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                    String coverUrl = UUID.randomUUID().toString().substring(0, 10) + coverFile.getOriginalFilename();
                    map.put(VIDEO_COVER, coverUrl);
                    try {
                        minioService.uploadImgFile(coverUrl, coverFile.getInputStream(), coverFile.getContentType());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    video.setCover(coverUrl);
                });
            }
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> client.sendDBChangeNotice(map));
            videoMapper.updateById(video);
            return Result.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("寄");
        }
    }

    /**
     * 删除视频并发送数据同步消息
     */
    @Override
    @Transactional
    public Result<Boolean> delete(DeleteVideoRequest deleteVideoRequest) {
        LambdaQueryWrapper<Video> deleteVideoWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<VideoData> deleteVideoDataWrapper = new LambdaQueryWrapper<>();
        deleteVideoWrapper.eq(Video::getUserId, deleteVideoRequest.getUserId());
        deleteVideoWrapper.eq(Video::getId, deleteVideoRequest.getVideoId());
        deleteVideoDataWrapper.eq(VideoData::getVideoId, deleteVideoRequest.getVideoId());
        CompletableFuture<Void> sendDBChangeNotice = CompletableFuture.runAsync(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaTimeModule module = new JavaTimeModule();
            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            objectMapper.registerModule(module);
            Map<String, Object> map = new HashMap<>(3);
            map.put(TABLE_NAME, VIDEO_TABLE_NAME);
            map.put(OPERATION_TYPE, OPERATION_TYPE_DELETE);
            map.put(TABLE_ID, deleteVideoRequest.getVideoId());
            client.sendDBChangeNotice(map);
        });
        videoMapper.delete(deleteVideoWrapper);
        videoDataMapper.delete(deleteVideoDataWrapper);
        return Result.success(true);
    }

    /**
     * 上传视频的分片、合并分片并获取最后截取的封面和存储到minio中的路径方便上传视频相关信息时使用
     */
    @Override
    public Result<List<String>> uploadPart(UploadPartRequest uploadPartRequest) throws IOException, EncoderException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //前端使用框架会在上传分片时将某个传参属性值重复两遍赋值到该属性中，因此需要去掉后续的传参以防映射不对
        int commaIndex = uploadPartRequest.getResumableIdentifier().indexOf(',');
        uploadPartRequest.setResumableIdentifier(uploadPartRequest.getResumableIdentifier().substring(0,commaIndex));
        String resumableIdentifier = uploadPartRequest.getResumableIdentifier();
        String videoName = "";
        String cover = "";
        //当视频上传的分片到最后一片时截取该分片的图片出来并将值传到cover里
        if(uploadPartMap.get(resumableIdentifier)!=null&&uploadPartMap.get(resumableIdentifier).getTotalCount().equals(uploadPartRequest.getResumableTotalChunks()-1)){
            InputStream videoFileInputStream = uploadPartRequest.getFile().getInputStream();
            byte[] bytes=IoUtil.readBytes(videoFileInputStream);
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
            String filePath = Files.createTempDirectory(".tmp").toString();
            String coverFileName=UUID.randomUUID().toString().substring(0,10)+".jpg";
            String videoFileName="video";
            File videoFile=new File(filePath,videoFileName);
            File coverFile = new File(filePath, coverFileName);
            Files.copy(byteArrayInputStream, Paths.get(videoFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            ScreenExtractor screenExtractor = new ScreenExtractor();
            MultimediaObject multimediaObject = new MultimediaObject(videoFile);
            screenExtractor.renderOneImage(multimediaObject, -1, -1, 1000, coverFile, 1);
            InputStream inputStream = new FileInputStream(coverFile);
            cover=Base64.encode(IoUtil.readBytes(inputStream));
            videoFile.delete();
            coverFile.delete();
        }
        //minio中同名文件是会覆盖的，因此若出现两个人上传同名文件后一个人上传的会把前一个上传的覆盖，所以需要给视频加独特后缀区分开
        String name = resumableIdentifier + UUID.randomUUID().toString().substring(0, 10);
        minioService.uploadVideoFile(name, uploadPartRequest.getFile().getInputStream(), VIDEO_TYPE);
        //将用户之前上传的分片信息缓存起来，方便后续断点续传时发送请求确认是否已上传过分片，确认了上传过的片就不会再上传
        Map<Integer, String> newUploadPartMap = uploadPartMap.getOrDefault(resumableIdentifier, new UploadPart()).getPartMap();
        newUploadPartMap.put(uploadPartRequest.getResumableChunkNumber(), name);
        UploadPart uploadPart=uploadPartMap.getOrDefault(resumableIdentifier,new UploadPart());
        uploadPart.setPartMap(newUploadPartMap);
        uploadPartMap.put(resumableIdentifier, uploadPart);
        uploadPartMap.get(resumableIdentifier).setTotalCount(uploadPartMap.get(resumableIdentifier).getTotalCount()+1);
        if (uploadPartMap.get(resumableIdentifier).getTotalCount().equals(uploadPartRequest.getResumableTotalChunks())) {
            log.info("合并");
            videoName = resumableIdentifier + UUID.randomUUID().toString().substring(0, 10);
            minioService.composePart(resumableIdentifier, videoName);
        }
        List<String> list = new ArrayList<>();
        list.add(videoName);
        list.add(cover);
        return Result.data(list);
    }
    @Override
    public ResponseEntity<Result<Boolean>> getProcessor(String resumableIdentifier, Integer resumableChunkNumber) {
        //根据之前存的映射判断，如果上传过了片就不用再上传了
        for (Map.Entry<Integer, String> entry : uploadPartMap.getOrDefault(resumableIdentifier,new UploadPart()).getPartMap().entrySet()) {
            if(entry.getKey().intValue()==(resumableChunkNumber.intValue())){
                return ResponseEntity.ok(Result.data(true));
            }
        }
        return ResponseEntity.noContent().build();
    }
}
