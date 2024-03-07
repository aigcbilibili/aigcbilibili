package ljl.bilibili.user_center.service.user_info;

import io.minio.errors.*;
import ljl.bilibili.user_center.vo.request.self_center.EditUserInfoRequest;
import ljl.bilibili.user_center.vo.response.UserInfoResponse;
import ljl.bilibili.util.Result;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserInfoService {
    Result<UserInfoResponse> getUserInfo(Integer userId1, Integer userId2);
    Result<Boolean> editSelfInfo(EditUserInfoRequest editUserInfoRequest) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
