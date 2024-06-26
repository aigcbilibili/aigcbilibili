package ljl.bilibili.chat.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PPTWord {
    private String ThemeName;
    private String text;
    private String index;
}
