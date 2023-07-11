package ml.littlekan.kookbot.bot;

import com.google.gson.Gson;
import ml.littlekan.kookbot.ErrorOut;
import ml.littlekan.kookbot.KOOKBot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

@ClientEndpoint
public class BotSession {
    private JavaPlugin instance = JavaPlugin.getPlugin(KOOKBot.class);
    private String token;
    private String gurl;
    private WebSocketContainer ws;
    private Session wss;
    private boolean pong = false;
    private SQL sql = KOOKBot.getSQL();
    
    public BotSession(String token){
        this.token = token;
        try {
            if(sql.getSession(token) == null){
                Gateway g = new Gateway(token);
                g.get();
                this.gurl = g.getGateway();
            }else{
                ResultSet session = sql.getSession(token);
                String url = session.getString("wsurl");
                String sn = new StringBuilder(session.getInt("sn")).toString();
                String sid = session.getString("session_id");
                this.gurl = url + "&resume=1&sn=" + sn + "&session_id=" + sid;
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

            private void retry(){
                //
            }

            @Override
            public void run(){
                sleep(3000);
                while(!KOOKBot.isStop()){
                    String sn = "0";
                    try {
                        sn = new StringBuilder(sql.getSession(token).getInt("sn")).toString();
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
    public void onMessage(String msg) throws SQLException, SessionException{
        Gson g = new Gson();
        ResponseBean data = g.fromJson(msg, ResponseBean.class);
        switch (data.getS()){
            case 1:
                if(data.getD().getCode() == 0){
                    sql.setSession(token, gurl, data.getD().getSession_id(), 0);
                    break;
                }else{
                    throw new SessionException("机器人溜号了！(#ﾟДﾟ)", new ResponseException(data.getD().getCode()), wss);
                }
            case 0:
                sql.snUpdate(token, data.getSn());
                new MessageProc(data, token);
                break;
            case 3:
                pong = true;
                break;
            case 5:
                throw new SessionException("完了！机器人电话打不通了！Σ(っ °Д °;)っ 明明上次还通的啊！", new ResponseException(data.getD().getCode()), wss);
            case 6:
                instance.getLogger().info("");
        }
    }
}