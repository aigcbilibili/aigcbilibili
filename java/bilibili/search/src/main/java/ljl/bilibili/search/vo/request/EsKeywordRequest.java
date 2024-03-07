package ljl.bilibili.search.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EsKeywordRequest {
    String searchWord;
}
