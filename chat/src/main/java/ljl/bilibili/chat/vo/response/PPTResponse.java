package ljl.bilibili.chat.vo.response;

import ljl.bilibili.chat.entity.PPTWord;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PPTResponse {
    private String coverImgSrc;
    private List<PPTWord> pptWordList;
}
