package ljl.bilibili.video.service.video_production.impl;

import io.minio.*;
import ljl.bilibili.video.service.video_production.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public Boolean uploadVideoFile(String fileName, InputStream stream, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileName)
                            .stream(stream, -1, 10485760)
                            .contentType(contentType)
                            .build());

            stream.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("上传失败", e);
        }
    }
    @Override
    public   Boolean uploadImgFile(String fileName, InputStream stream, String contentType){

        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket("video-cover").object(fileName)
                            .stream(stream, -1, 10485760)
                            .contentType(contentType)
                            .build());

            stream.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
return true;
    }

    @Override
    public InputStream getObject(String objectName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            return minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            log.error("错误：" + e.getMessage());
        }
        return null;
    }
    public Boolean createBucket(String name){
        try {

            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            } else {
            }
        } catch (Exception e) {
        }
        return true;
    }
    public Boolean deleteBucket(String name){
        try {

            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
