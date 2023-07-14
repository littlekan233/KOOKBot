package ml.littlekan.kookbot.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.Gson;
import java.net.HttpURLConnection;
import ml.littlekan.kookbot.ErrorOut;
import ml.littlekan.kookbot.KOOKBot;
import ml.littlekan.kookbot.bot.ResponseException;

public class KOOKUser {
    public String username;
    public String nickname;
    public String identify;
    public String fullname;
    
    public KOOKUser(String id){
        try {
            URL api = new URL("https://www.kookapp.cn/api/v3/user/view?user_id=" + id + "&guild_id=" + KOOKBot.getPluginConfig().getString("kook_server_id"));
            HttpURLConnection conn = (HttpURLConnection) api.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bot " + KOOKBot.getPluginConfig().getString("token"));
            conn.connect();
            InputStream is = conn.getInputStream();
            ResponseInfo res = new Gson().fromJson(new InputStreamReader(is), ResponseInfo.class);
            if(res.getCode() == 0){
                ResponseInfo.DataBean data = res.getData();
                username = data.getUsername();
                identify = data.getIdentify_num();
                nickname = data.getNickname();
                fullname = username + "#" + identify;
            } else {
                throw new ResponseException(res.getCode());
            }
        } catch (IOException | ResponseException e) {
            new ErrorOut(e);
        }
    }
}
