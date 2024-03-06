package ljl.bilibili.search.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class EsIndexRequest {
    @JsonProperty("indexName")
    private String indexName;
    private Map<String,String> properties;
}
