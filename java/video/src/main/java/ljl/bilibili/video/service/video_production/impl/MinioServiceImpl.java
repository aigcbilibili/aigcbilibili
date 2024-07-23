package ljl.bilibili.video.service.video_production.impl;

import io.minio.*;
import io.minio.errors.*;
import io.swagger.models.auth.In;
import ljl.bilibili.video.pojo.UploadPart;
import ljl.bilibili.video.service.video_production.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ljl.bilibili.video.service.video_production.impl.UploadAndEditServiceImpl.uploadPartMap;

@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String VIDEO_BUCKET_NAME;
    @Override
    public Boolean composePart(String resumableIdentifier,String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<ComposeSource> composeSourceList=new ArrayList<>();
        for(Map.Entry<Integer,String> entry : uploadPartMap.get(resumableIdentifier).getPartMap().entrySet()){
            composeSourceList.add(ComposeSource.builder().bucket("video").object(entry.getValue()).build());
        }
//        composeSourceList.add(ComposeSource.builder().bucket("video").object("test").build());
//        composeSourceList.add(ComposeSource.builder().bucket("video").object("3333e5f74529-7").build());
        minioClient.composeObject(ComposeObjectArgs.builder().sources(composeSourceList).object(name).bucket("video").build());
        return true;
    }
    @Override
    public Boolean uploadVideoFile(String fileName, InputStream stream, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(VIDEO_BUCKET_NAME).object(fileName)
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
                    .bucket(VIDEO_BUCKET_NAME)
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
