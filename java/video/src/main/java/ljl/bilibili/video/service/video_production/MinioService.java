package ljl.bilibili.video.service.video_production;

import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MinioService {
    Boolean uploadVideoFile(String fileName, InputStream stream, String contentType);

    Boolean uploadImgFile(String fileName, InputStream stream, String contentType);

    InputStream getObject(String objectName);
    Boolean composePart(String resumableIdentifier,String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
