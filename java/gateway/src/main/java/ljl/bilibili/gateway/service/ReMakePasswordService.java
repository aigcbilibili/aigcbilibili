package ljl.bilibili.gateway.service;

import ljl.bilibili.gateway.vo.request.RemakePasswordRequest;
import ljl.bilibili.util.Result;

public interface ReMakePasswordService {
    Result<Boolean> remakePassword(RemakePasswordRequest remakePasswordRequest);
}
