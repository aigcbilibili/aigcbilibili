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
    public static Map<String, UploadPart> uploadPartMap=new HashMap<>();

    /**
     * 判断是否恶意文件、上传视频到minio、新增视频与视频数据记录、推送视频动态、发送数据同步消息
     */
    @Override
    @Transactional
    public Result<Boolean> uploadTotal(UploadVideoRequest uploadVideoRequest) {
        try {
            String path = Files.createTempDirectory(".tmp").toString();
            File file = new File(path, "test");
            Video video = uploadVideoRequest.toEntity();
            LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
            MultipartFile videoFile = null;
            String coverFile = uploadVideoRequest.getVideoCover();
            String url = "https://labilibili.com/video/" + uploadVideoRequest.getUrl();
            video.setUrl(url);
            if (coverFile != null && uploadPartMap.get(uploadVideoRequest.getUserId()) == null) {
//                hasCover=true;
                String prefixPath = "https://labilibili.com/video-cover/";
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(coverFile);
                String coverFileName = video.getName() + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
                String imgContentType = "image/jpeg";
                video.setCover(prefixPath + coverFileName);
                CustomMultipartFile coverMultipartFile = new CustomMultipartFile(decodedBytes, coverFileName, imgContentType);
                CompletableFuture.runAsync(() -> log.info(""));
                CompletableFuture.runAsync(() -> {
                    log.info("");
                    log.info("");
                });
                queryWrapper.eq(Video::getCover, UUID.randomUUID().toString().substring(0, 8) + coverFileName);
                CompletableFuture<Void> uploadImgFuture = CompletableFuture.runAsync(() -> minioService.uploadImgFile(coverFileName, coverMultipartFile.getInputStream(), imgContentType)).handle((result, ex) -> {
                    if (ex != null) {
                        uploadVideoSuccess.set(false);
                        log.error("uploadimgfail");
                    }
                    return null;
                });
                log.info("封面" + video.getCover());


                CompletableFuture thenRunFuture = uploadImgFuture.thenRun(() ->
                        client.sendUploadNotice(new UploadVideo().setVideoId(video.getId()).setVideoName(video.getName()).setUrl(url).setHasCover(true)));
                CompletableFuture<Void> sendNoticeFuture = CompletableFuture.runAsync(() -> {
                    User user = userMapper.selectById(uploadVideoRequest.getUserId());
                    client.dynamicNotice(uploadVideoRequest.toCoverDynamic(user, video));
                    log.info("dynamicupload");
                }).handle((result, ex) -> {
                    if (ex != null) {
                        log.error("dynamicfail");
                        uploadVideoSuccess.set(false);
                    }
                    return null;
                });
            } else {

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
            log.info(video.getCover());
            videoMapper.insert(video);
            log.info(video.getCover());
            videoDataMapper.insert(new VideoData().setVideoId(video.getId()));
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

//            CompletableFuture.allOf(uploadVideoFuture, sendNoticeFuture).join();
//            if (!uploadVideoSuccess.get()) {
//                log.error("lose");
//            } else {
//                // 所有任务成功
//                log.info("ok");
//            }
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
     * 上传视频时获取视频封面
     */
    @Override
    public Result<List<String>> uploadPart(UploadPartRequest uploadPartRequest) throws IOException, EncoderException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String resumableIdentifier = uploadPartRequest.getResumableIdentifier();
        String videoName = "";
        String cover = "";
        if(uploadPartRequest.getResumableChunkNumber()==1){
            String path = Files.createTempDirectory(".tmp").toString();
            File file = new File(path, "test");
            InputStream videoFileInputStream = uploadPartRequest.getFile().getInputStream();
            byte[] bytes=IoUtil.readBytes(videoFileInputStream);
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
            Files.copy(byteArrayInputStream, Paths.get(file.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            if (!FileTypeUtil.getType(file).equals("mp4")) {
                file.delete();
                return Result.error("上传恶意文件");
            } else {
                file.delete();
                log.info("文件无问题");
            }
        }
        if(uploadPartMap.get(resumableIdentifier).getTotalCount().equals(uploadPartRequest.getResumableTotalChunks()-1)){
            InputStream videoFileInputStream = uploadPartRequest.getFile().getInputStream();
            byte[] bytes=IoUtil.readBytes(videoFileInputStream);
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
            String filePath = Files.createTempDirectory(".tmp").toString();
            String coverFileName="coverFileName.jpg";
            String videoFileName="video";
            File videoFile=new File(filePath,videoFileName);
            File coverFile1 = new File(filePath, coverFileName);
            Files.copy(byteArrayInputStream, Paths.get(videoFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            ScreenExtractor screenExtractor = new ScreenExtractor();
            MultimediaObject multimediaObject = new MultimediaObject(videoFile);
            screenExtractor.renderOneImage(multimediaObject, -1, -1, 1000, coverFile1, 1);
            InputStream inputStream1 = new FileInputStream(coverFile1);
            cover=Base64.encode(IoUtil.readBytes(inputStream1));
            videoFile.delete();
            coverFile1.delete();
        }
        String name = resumableIdentifier + UUID.randomUUID().toString().substring(0, 10);
        minioService.uploadVideoFile(name, uploadPartRequest.getFile().getInputStream(), VIDEO_TYPE);
        log.info(uploadPartMap.toString());
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
            cover="https://labilibili.com/video/山海2549ec9d-c";
        }
        List<String> list = new ArrayList<>();
        list.add(videoName);
        list.add(cover);
        return Result.data(list);
    }
    @Override
    public ResponseEntity<Result<Boolean>> getProcessor(String resumableIdentifier, Integer resumableChunkNumber) {
        List<Integer> indexList = new ArrayList<>();
        if(uploadPartMap.getOrDefault(resumableIdentifier,new UploadPart()).getPartMap().size()>0){
            for (Map.Entry<Integer, String> entry : uploadPartMap.get(resumableIdentifier).getPartMap().entrySet()) {
                indexList.add(entry.getKey());
            }
        }
        if(!indexList.contains(resumableChunkNumber)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Result.data(true));
    }
}
