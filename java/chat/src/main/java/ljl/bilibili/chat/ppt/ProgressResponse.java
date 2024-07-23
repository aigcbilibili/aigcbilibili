package ljl.bilibili.chat.ppt;

public class ProgressResponse {
    private int code;
    private String desc;
    private Data data;

    // Getters and setters for code
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // Getters and setters for desc
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    // Getters and setters for data
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int process;
        private String pptId;
        private String pptUrl;

        // Getters and setters for process
        public int getProcess() {
            return process;
        }

        public void setProcess(int process) {
            this.process = process;
        }

        // Getters and setters for pptId
        public String getPptId() {
            return pptId;
        }

        public void setPptId(String pptId) {
            this.pptId = pptId;
        }

        // Getters and setters for pptUrl
        public String getPptUrl() {
            return pptUrl;
        }

        public void setPptUrl(String pptUrl) {
            this.pptUrl = pptUrl;
        }
    }
}
