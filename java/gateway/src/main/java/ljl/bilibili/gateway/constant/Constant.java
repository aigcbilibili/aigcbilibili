package ljl.bilibili.gateway.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
//@Component
public class Constant {
    public static final Map<String,String> CAPTCHA =new HashMap<>();

    public static final String HAS_SESSION="hasSession";

    public static final int WIDTH = 160;

    public static final int HEIGHT = 40;

    public static final int CODE_COUNT = 4;

    public static final int LINE_COUNT = 20;
    public static final char[] CODE_SEQUENCE = {'0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9'
    };
    public static final String SESSIONID="sessionId";

    public static final String CAPTCHA_IMAGE="image";

    public static final String CAPTCHA_CODE="code";

    public static final String USERIDENTITY="userId";
    public static final String SAFE_REQUEST_HEADER="aigcBiliBiliHeader";

    public static final String SHORT_TOKEN="shortAuthorization";
    public static final String LONG_TOKEN="longAuthorization";
    public static final int SHORT_TOKEN_EXPIRATION = 30 * 60 * 1000;
    public static final String SECRET_KEY = "labilibili_key";
    public static final String TOKEN_SUBJECT="labilibili";
    public static final int LONG_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;
    public static final String JWT_ROLE="role";
    public static final String JWT_ROLE_NAME="user";
    public static final String TABLE_NAME="table";
    public static final String USER_TABLE_NAME="user";
    public static final String OPERATION_TYPE="type";
    public static final String OPERATION_TYPE_ADD="add";
    public static final String TABLE_IGNORE_USERNAME="userName";
    public static final String TABLE_IGNORE_PASSWORD="password";
    public static final String TABLE_IGNORE_PHONE_NUMBER="phoneNumber";
    public static final String TABLE_IGNORE_MAIL_NUMBER="mailNumber";

}
