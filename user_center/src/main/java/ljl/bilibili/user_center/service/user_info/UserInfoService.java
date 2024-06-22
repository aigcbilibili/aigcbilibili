package ljl.bilibili.user_center.service.user_info;

import io.minio.errors.*;
import ljl.bilibili.user_center.vo.request.self_center.EditUserInfoRequest;
import ljl.bilibili.user_center.vo.response.self_center.UserInfoResponse;
import ljl.bilibili.util.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserInfoService {
    Result<UserInfoResponse> getUserInfo(Integer userId1, Integer userId2);
    Result<Boolean> editSelfInfo(MultipartFile file,Integer userId,String nickname,String intro) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
