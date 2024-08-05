package ljl.bilibili.chat.ppt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class OutlineVo {
    private String coverImgSrc;
    // 大纲ID
    @JsonProperty("id")
    private Long id;

    // 主标题
    @JsonProperty("title")
    private String title;

    // 副标题
    @JsonProperty("subTitle")
    private String subTitle;

    // 二级标题
    @JsonProperty("chapters")
    private List<Chapter> chapters;

    // 参考文件相关，暂未开放能力
    @JsonProperty("fileUrl")
    private String fileUrl = "";
    @JsonProperty("fileType")
    Integer fileType = 0;
    @JsonProperty("end")
    private String end = "";
    @JsonProperty("fileId")
    private String fileId;
    @JsonProperty("registerFrom")
    private String registerFrom;

    public static class Chapter {
        // 大纲ID，若为新增大纲则设为NULL
        @JsonProperty("id")
        Long id;
        // 章节、子章节标题名称
        @JsonProperty("chapterTitle")
        public String chapterTitle;

        // 参考文件相关，能力暂未开放

        @JsonProperty("fileUrl")
        String fileUrl = "";
        @JsonProperty("fileType")
        Integer fileType = 0;
        @JsonProperty("chartFlag")
        Boolean chartFlag = false;
        @JsonProperty("searchFlag")
        Boolean searchFlag = false;
        @JsonProperty("chapterContents")
        public List<Chapter> chapterContents = null;
    }
}