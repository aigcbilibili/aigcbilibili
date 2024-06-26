package ljl.bilibili.util;

import lombok.Data;
import org.apache.http.HttpStatus;
/**
 *统一响应体
 */
@Data
public class Result<T> {

    private static final int SUCCESS_CODE = HttpStatus.SC_OK;

    private static final int ERROR_CODE = 600;

    private static final int FAILED_CODE = 600;
    private final int code;
    private final String msg;
    private final T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private static <T> Result<T> success(String msg, T data) {
        return new Result<>(SUCCESS_CODE, msg, data);
    }


    public static <T> Result<T> success(String msg) {
        return success(msg, null);
    }
    public static <T> Result<T> success(T data) {
        return success("操作成功◕‿◕",data);
    }

    public static <T> Result<T> data(T data) {
        return success("查询成功＾▽＾", data);
    }

    public static <T> Result<T> failed(T data) {
        return new Result<>(FAILED_CODE, null, data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(ERROR_CODE, msg, null);
    }

}
