package ml.littlekan.kookbot;

import ml.littlekan.kookbot.bot.SQL;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.Random;

public class BindManager {
    private String random_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private int verify_code_length = 6;
    private SQL sqli;
    public BindManager(){
        sqli = KOOKBot.getSQL();
    }

    public void addBindRequest(Player p){
        String verify = generateVerifyCode();
        sqli.createLinkRequest(p.getName(), verify);
        msg(p, "&a绑定请求已创建！ヾ(≧▽≦*)o");
        msg(p, "&a请登录您的KOOK，然后在服务器内管理员指定的机器人聊天频道发送以下命令：");
        msg(p, "&a/bind &6" + verify);
    }

    public void bindResponsed(String msgid, String verifycode, String userid){
        sqli.linkRequestAccept(verifycode, userid, msgid);
    }

    private void msg(Player sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[KOOKBot] &f" + msg));
    }

    private String generateVerifyCode(){
        StringBuffer code = new StringBuffer();
        Random r = new Random();
        for(int i = 0; i < verify_code_length; i++){
            code.append(random_chars.charAt(r.nextInt(random_chars.length())));
        }
        return code.toString();
    }
}
