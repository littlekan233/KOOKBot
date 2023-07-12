package ml.littlekan.kookbot.user;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import ml.littlekan.kookbot.ErrorOut;

public class User {
    public User(String id){
        try {
            URL api = new URL("https://www.kookapp.cn/api/v3/user/view");
            HttpURLConnection conn = (HttpURLConnection) api.openConnection();
            conn.setRequestMethod("GET");
        } catch (IOException e) {
            new ErrorOut(e);
        }
    }
}
