package ljl.bilibili.client.file;

import cn.hutool.core.io.IoUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.util.StringUtils;
/**
 *实现MultiPartFile接口用于远程调用时传递Inputstream流
 */
public class CustomMultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private final String fileName;
    private final String contentType;

    public CustomMultipartFile(InputStream inputStream, String fileName, String contentType) throws IOException {
        this.fileContent = IoUtil.readBytes(inputStream);
        this.fileName = fileName;
        this.contentType = contentType;
    }
    public CustomMultipartFile(byte[] fileContent, String fileName, String contentType) throws IOException {
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return StringUtils.cleanPath(fileName);
    }

    @Override
    public String getOriginalFilename() {
        return StringUtils.cleanPath(fileName);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() {
        return fileContent;
    }

    @Override
    public InputStream getInputStream()  {
      InputStream inputStream=IoUtil.toStream(getBytes());
      return inputStream;
    }

    @Override
    public void transferTo(java.io.File dest) throws IllegalStateException {
        throw new UnsupportedOperationException("transfer to file not supported");
    }
}
