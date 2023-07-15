package ml.littlekan.kookbot.bot;

import ml.littlekan.kookbot.KOOKBot;
import ml.littlekan.kookbot.KOOKMessageSender;
import ml.littlekan.kookbot.user.KOOKUser;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.sql.SQLException;

public class MessageProc {
    public MessageProc(ResponseBean bean){
        SQL sqli = KOOKBot.getSQL();
        ResponseBean.DataBean data = bean.getD();
        FileConfiguration config = KOOKBot.getPluginConfig();

        if(data.getType() != 1
                || data.getExtra().getGuild_id() != config.getString("kook_server_id")
                || data.getTarget_id() != config.getString("kook_channel_id")){
            return;
        }

        if(data.getContent().startsWith("/")){
            String[] parsedcmd = data.getContent().substring(1).split(" ");
            switch (parsedcmd[0]) {
                case "bind":
                    if(sqli.getLinkRequestPlayerName(parsedcmd[1]) == "_$NullPlayer$_") {
                        new KOOKMessageSender("[KOOKBot] 抱歉，此邀请码似乎无效或过期了（-1） ⊙﹏⊙∥", data.getMsg_id()).send();
                    }
                    KOOKBot.getBindManager().bindResponsed(data.getMsg_id(), parsedcmd[1], data.getAuthor_id());
                    break;
                case "send":
                    String format = config.getString("chat_format.mc");
                    String mcname = "<此人未绑定mc账号或数据库异常>";
                    try {
                        if(sqli.getLinkByKOOKUser(data.getAuthor_id()) != null) {
                            mcname = sqli.getLinkByKOOKUser(data.getAuthor_id()).getString("mcname");
                        }
                    } catch (SQLException e) {}

                    format.replace("{kook_name}", new KOOKUser(data.getAuthor_id()).username);
                    format.replace("{mc_name}", mcname);
                    format.replace("{message}", data.getContent().substring(6));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(Component.text(format));
                    }
                    new KOOKMessageSender("[KOOKBot] 发送成功啦 (*^▽^*)", data.getMsg_id()).send();
                    break;
                default: break; // 我有强迫症
            }
        }
    }
}
