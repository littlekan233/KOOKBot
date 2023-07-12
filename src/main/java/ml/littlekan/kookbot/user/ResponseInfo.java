package ml.littlekan.kookbot.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseInfo {
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private String id;
        private String username;
        private String identify_num;
    }
}