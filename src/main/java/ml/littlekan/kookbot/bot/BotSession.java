package ml.littlekan.kookbot.bot;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.bukkit.plugin.java.JavaPlugin;
import ml.littlekan.kookbot.ErrorOut;
import ml.littlekan.kookbot.KOOKBot;
import javax.websocket.*;
import lombok.Data;
import com.google.gson.Gson;
import org.bukkit.scheduler.BukkitRunnable;

@ClientEndpoint
public class BotSession {
    private JavaPlugin instance = JavaPlugin.getPlugin(KOOKBot.class);
    private String token;
    private String gurl;
    private WebSocketContainer ws;
    private Session wss;
    private boolean pong = false;
    
    public BotSession(String token){
        this.token = token;
        try {
            if(KOOKBot.getSql().getSession(token) == null){
                Gateway g = new Gateway(token);
                g.get();
                this.gurl = g.getGateway();
            }
            connect();
            runPingPong();
        } catch (SQLException | URISyntaxException | DeploymentException | IOException e) {
            new ErrorOut(e);
        }
    }

    private void connect() throws URISyntaxException, DeploymentException, IOException{
        ws = ContainerProvider.getWebSocketContainer();
        URI wsu = new URI(gurl);
        wss = ws.connectToServer(BotSession.class, wsu);
    }

    private void runPingPong(){
        new BukkitRunnable() {
            private void sleep(long ms) {
                try {Thread.sleep(ms);} catch (InterruptedException e) {}
            }

            private void retry(){}

            @Override
            public void run(){
                sleep(3000);
                while(!KOOKBot.isStop()){
                    String sn = "0";
                    try {
                        sn = new StringBuilder(KOOKBot.getSql().getSession(token).getInt("sn")).toString();
                    } catch (SQLException e) {
                        new ErrorOut(e);
                    }
                    wss.getAsyncRemote().sendText("{\"s\": 5, \"sn\": " + sn + "}");
                    sleep(6000);
                    if(pong) {
                        pong = false;
                        sleep(24000);
                    }else{
                        retry();
                    }
                }
            }
        }.runTaskAsynchronously(instance);
    }

    @OnMessage
    public void onMessage(String msg){
        Gson g = new Gson();
        Bean data = g.fromJson(msg, Bean.class);
        switch (data.getS()){}
    }

    @Data
    private class Bean implements Serializable{
        private int s; // 信令
        private int sn; // 一个sn值，仅在信令为0时传值
        private DataBean d; // 数据，结构见Bean类
        @Data
        private class DataBean implements Serializable{
            private int code; // 状态码，成功为0，错误见kook开发者平台
            private String session_id; // 会话id，多用于hello包和resumeack包
            private String content;
        }
    }
}