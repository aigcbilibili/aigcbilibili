package ljl.bilibili.chat.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BigModelToken {
    private String owner;
    private String appId;
    private String apiSecret;
    private String apiKey;
    private Boolean usedStatus;
}
