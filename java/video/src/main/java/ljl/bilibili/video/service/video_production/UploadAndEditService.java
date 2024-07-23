package ljl.bilibili.video.service.video_production;
import io.minio.errors.*;
import ljl.bilibili.util.Result;
import ljl.bilibili.video.vo.request.video_production.DeleteVideoRequest;
import ljl.bilibili.video.vo.request.video_production.EditVideoRequest;
import ljl.bilibili.video.vo.request.video_production.UploadPartRequest;
import ljl.bilibili.video.vo.request.video_production.UploadVideoRequest;
import ljl.bilibili.video.vo.response.video_production.UploadProcessorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UploadAndEditService {
    Result<Boolean> uploadTotal(UploadVideoRequest uploadVideoRequest) throws IOException;

    Result<Boolean> edit(EditVideoRequest editVideoRequest);
    Result<Boolean> delete(DeleteVideoRequest deleteVideoRequest);

    Result<List<String>> uploadPart(@ModelAttribute UploadPartRequest uploadPartRequest) throws IOException, EncoderException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    ResponseEntity<Result<Boolean>> getProcessor(String resumableIdentifier,Integer resumableChunkNumber);
}
