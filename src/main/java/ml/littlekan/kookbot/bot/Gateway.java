package ml.littlekan.kookbot.bot;

import java.net.URL;
import lombok.Data;
import lombok.Getter;
import com.google.gson.Gson;
import ml.littlekan.kookbot.ErrorOut;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;

public class Gateway {
    private String token;
    @Getter private String gateway;
    public Gateway(String token){
        this.token = token;
    }
    public void get(){
        URL api;
        HttpURLConnection conn;
        try {
            api = new URL("https://www.kookapp.cn/api/v3/gateway/index?compress=0");
            conn = (HttpURLConnection) api.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bot " + token);
            conn.connect();
            InputStream is = conn.getInputStream();
            Gson gson = new Gson();
            Bean data = gson.fromJson(new InputStreamReader(is), Bean.class);
            int code = data.getCode();
            if(code == 0) this.gateway = data.getData().getUrl();
            conn.disconnect();
        } catch (IOException e) {
            new ErrorOut(e);
        }
    }

    @Data
    public class Bean implements Serializable{
        private int code;
        private String message;
        private DataBean data;
        @Data
        private class DataBean{
            private String url;
        }
    }
}