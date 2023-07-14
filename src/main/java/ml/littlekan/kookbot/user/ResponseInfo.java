package ml.littlekan.kookbot.user;

import lombok.Data;

@Data
public class ResponseInfo {
    private int code;
    private DataBean data;

    @Data
    public static class DataBean {
        private String id;
        private String username;
        private String identify_num;
        private String nickname;
    }
}