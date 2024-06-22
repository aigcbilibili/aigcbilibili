package ljl.bilibili.chat.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class ImageResponse {
    public Header header;
    public Payload payload;

    public static class Header {
        public int code;
        public String message;
        public String sid;
        public int status;
    }

    public static class Payload {
        public Choices choices;

        public static class Choices {
            public int status;
            public int seq;
            public List<Text> text;

            public static class Text {
                public String content;
                public int index;
                public String role;
            }
        }
    }
}
