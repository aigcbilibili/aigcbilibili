package ljl.bilibili.video.constant;

import ljl.bilibili.video.pojo.UploadPart;
import ljl.bilibili.video.vo.response.audience_reactions.play.FirstPageVideoResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constant {
    public static final String HAS_PLAY="已经新增过播放记录了";
    public static final String SQL="limit";
    public static final String LIMIT="12";
    public static final String HEADERS_VALUES="attachment; filename=\"your-filename.ext\"";
    public static final String IMAGE_TYPE="images/jpeg";
    public static final String VIDEO_TYPE="video/mp4";
    public static final String TABLE_NAME="table";
    public static final String VIDEO_TABLE_NAME="video";
    public static final String VIDEO_URL="url";
    public static final String VIDEO_COVER="cover";
    public static final String VIDEO_ID="video_id";
    public static final String TABLE_ID="id";
    public static final String OPERATION_TYPE="type";
    public static final String OPERATION_TYPE_ADD="add";
    public static final String OPERATION_TYPE_DELETE="delete";
    public static final String OPERATION_TYPE_UPDATE="update";
    public static final Map<Integer, List<FirstPageVideoResponse>> map=new ConcurrentHashMap<>();
    public static Map<String, UploadPart> uploadPartMap=new HashMap<>();
}
