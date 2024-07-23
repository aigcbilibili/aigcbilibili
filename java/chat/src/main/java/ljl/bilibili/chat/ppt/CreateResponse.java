package ljl.bilibili.chat.ppt;

public class CreateResponse {
    private boolean flag;
    private int code;
    private String desc;
    private Integer count;
    private Data data;

    // Getter and Setter for flag
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    // Getter and Setter for code
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // Getter and Setter for desc
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    // Getter and Setter for count
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    // Getter and Setter for data
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String sid;
        private String coverImgSrc;
        private String title;
        private String subTitle;
        private String Outline;

        // Getter and Setter for sid
        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        // Getter and Setter for coverImgSrc
        public String getCoverImgSrc() {
            return coverImgSrc;
        }

        public void setCoverImgSrc(String coverImgSrc) {
            this.coverImgSrc = coverImgSrc;
        }

        // Getter and Setter for title
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        // Getter and Setter for subTitle
        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getOutline() {
            return Outline;
        }

        public void setOutline(String outline) {
            this.Outline = outline;
        }
    }
}
