package ml.littlekan.kookbot.bot;

import lombok.Data;
import java.io.Serializable;

@Data
public class ResponseBean implements Serializable {
    private int s; // 信令
    private int sn; // 一个sn值，仅在信令为0时传值
    private DataBean d; // 数据，结构见Bean类
    @Data
    public class DataBean implements Serializable{
        private int code; // 状态码，成功为0，错误见kook开发者平台
        private String session_id; // 会话id，多用于hello包和resumeack包
        private String content;
    }
}
