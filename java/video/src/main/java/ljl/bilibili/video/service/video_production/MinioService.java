package ljl.bilibili.video.service.video_production;

import java.io.InputStream;

public interface MinioService {
     Boolean uploadVideoFile(String fileName, InputStream stream, String contentType);

    Boolean uploadImgFile(String fileName, InputStream stream, String contentType);

      InputStream getObject(String objectName);
}
