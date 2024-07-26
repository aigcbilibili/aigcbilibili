package ljl.bilibili.chat.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)

public class PPTDetail {
    private String ThemeName;
    private Boolean generateEnding;
    private String text;
}
