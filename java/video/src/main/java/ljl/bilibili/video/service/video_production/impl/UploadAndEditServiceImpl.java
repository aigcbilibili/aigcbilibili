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
import ljl.bilibili.video.service.video_production.UploadAndEditService;
import ljl.bilibili.video.vo.request.video_production.DeleteVideoRequest;
import ljl.bilibili.video.vo.request.video_production.EditVideoRequest;
import ljl.bilibili.video.vo.request.video_production.UploadVideoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Override
    @Transactional
    public Result<Boolean> upload(UploadVideoRequest uploadVideoRequest) {
        try {
            String path = Files.createTempDirectory(".tmp").toString();
            File file = new File(path, "test");
            Video video = uploadVideoRequest.toEntity();
            LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
            MultipartFile videoFile = uploadVideoRequest.getFile();
            String coverFile = uploadVideoRequest.getVideoCover();
            InputStream videoFileInputStream = videoFile.getInputStream();
            byte[] bytes = IoUtil.readBytes(videoFileInputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Files.copy(byteArrayInputStream, Paths.get(file.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            if (!FileTypeUtil.getType(file).equals("mp4")) {
                file.delete();
                return Result.error("上传恶意文件");
            } else {
                log.info("文件无问题");
            }
            String fileName = uploadVideoRequest.getName() + UUID.randomUUID().toString().substring(0, 10);
            String url = "https://labilibili.com/video/" + fileName;
            video.setUrl(url);
            String contentType = "video/mp4";
            if (coverFile != null) {
                String prefixPath = "https://labilibili.com/video-cover/";
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(coverFile);
                String coverFileName = video.getName() + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
                String imgContentType = "image/jpeg";
                video.setCover(prefixPath + coverFileName);
                CustomMultipartFile coverMultipartFile = new CustomMultipartFile(decodedBytes, coverFileName, imgContentType);

                queryWrapper.eq(Video::getCover, UUID.randomUUID().toString().substring(0, 8) + coverFileName);
                CompletableFuture<Void> uploadImgFuture = CompletableFuture.runAsync(() -> minioService.uploadImgFile(coverFileName, coverMultipartFile.getInputStream(), imgContentType)).handle((result, ex) -> {
                    if (ex != null) {
                        uploadVideoSuccess.set(false);
                        log.error("uploadimgfail");
                    }
                    return null;
                });
                log.info("封面" + video.getCover());
                CompletableFuture<Void> uploadVideoFuture = CompletableFuture.runAsync(() -> {
                    log.info("111");
                    minioService.uploadVideoFile(fileName, new ByteArrayInputStream(bytes), contentType);
                }).handle((result, ex) -> {
                    if (ex != null) {
                        uploadVideoSuccess.set(false);
                        log.error("uploadvideofail");
                    }
                    return null;
                });

                CompletableFuture<Void> thenRunFuture = uploadVideoFuture.thenRun(() ->
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
                CompletableFuture<Void> uploadVideoFuture = CompletableFuture.runAsync(() -> {
                    log.info("111");
                    minioService.uploadVideoFile(fileName, new ByteArrayInputStream(bytes), contentType);
                }).handle((result, ex) -> {
                    if (ex != null) {
                        uploadVideoSuccess.set(false);
                        log.error("uploadvideofail");
                    }
                    return null;
                });
                CompletableFuture<Void> thenRunFuture = uploadVideoFuture.thenRun(() ->
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
            LambdaUpdateWrapper<Video> update = new LambdaUpdateWrapper<>();
            update.set(Video::getCover, video.getCover());
            update.eq(Video::getId, video.getId());
            videoMapper.update(null, update);
            videoDataMapper.insert(new VideoData().setVideoId(video.getId()));
            CompletableFuture<Void> sendDBChangeNotice = CompletableFuture.runAsync(() -> {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaTimeModule module = new JavaTimeModule();
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

    @Override
    public Result<String> getVideoCover(MultipartFile multipartFile) throws IOException, EncoderException {
        String filePath = Files.createTempDirectory(".tmp").toString();
        String coverFileName = "coverFileName.jpg";
        String videoFileName = "video";
        File videoFile = new File(filePath, videoFileName);
        File coverFile1 = new File(filePath, coverFileName);
        Files.copy(multipartFile.getInputStream(), Paths.get(videoFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
        ScreenExtractor screenExtractor = new ScreenExtractor();
        MultimediaObject multimediaObject = new MultimediaObject(videoFile);
        screenExtractor.renderOneImage(multimediaObject, -1, -1, 1000, coverFile1, 1);
        InputStream inputStream1 = new FileInputStream(coverFile1);
        String img = Base64.encode(IoUtil.readBytes(inputStream1));
        videoFile.delete();
        coverFile1.delete();
        return Result.data(img);
    }
}
