package ml.littlekan.kookbot;

import java.net.URL;

import com.google.gson.Gson;

import lombok.Data;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KOOKMessageSender {
    private String channelid;
    private String token;
    private String msg;
    private String quotemsgid;
    public KOOKMessageSender(String msg, String quote){
        this.channelid = KOOKBot.getPluginConfig().getString("kook_channel_id");
        this.token = KOOKBot.getPluginConfig().getString("token");
        this.msg = msg;
        this.quotemsgid = quote;
    }

    public void send(){
        try {
            URL api = new URL("https://www.kookapp.cn/api/v3/message/create");
            HttpURLConnection conn = (HttpURLConnection) api.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bot " + this.token);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            PrintWriter pw = new PrintWriter(osw);
            pw.print("{\"target_id\": \"" + channelid + "\", \"quote\": \"" + quotemsgid + "\", \"content\": \"" + msg + "\"}");
            pw.flush();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            Response r = new Gson().fromJson(isr, Response.class);
            conn.getInputStream().close();
            conn.getOutputStream().close();
            if(r.getCode() != 0){}
        } catch (IOException e) {
            new ErrorOut(e);
        }
    }

    @Data
    private class Response {
        private int code;
    }
}
