package ml.littlekan.kookbot;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class BotCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length <= 0) {
            msg(sender, "&aKOOKBot插件正在运行 版本1.0.0-SNAPSHOT >w<");
            msg(sender, "&a输入 &6/kook help &a获取帮助！");
        }else{
            switch (args[0]) {
                case "bind":
                    if(sender instanceof Player){
                        KOOKBot.getBindManager().addBindRequest((Player) sender);
                    }else{
                        msg(sender, "&c只有玩家可以使用哦 ㄟ( ▔, ▔ )ㄏ");
                    }
                case "help":
                    msg(sender, "&7----- &a[KOOKBot]&7 -----");
                    msg(sender, "&6/kook bind &7绑定账户");
                    msg(sender, "&6/kook help &7-- &a显示此帮助");
                    msg(sender, "&6/kook invite &7-- &a获取本服务器的KOOK邀请链接");
                    msg(sender, "&6/kook reload &7-- &a重载插件");
                    msg(sender, "&6/kook reset &7-- &a将插件重置");
                    msg(sender, "&6/kook send <message> &7-- &a发送信息到本服务器的KOOK服务器");
                    break;
                case "invite":
                    String url = JavaPlugin.getPlugin(KOOKBot.class).getConfig().getString("invite-url");
                    msg(sender, "&b此服务器的KOOK链接：&6&n" + url);
                    break;
                default:
                    msg(sender, "&c无效的命令 T_T");
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        List<String> list = new ArrayList<>();
        if(args.length == 0){
            list.add("bind");
            list.add("help");
            list.add("invite");
            list.add("send");
        }
        return null;
    }

    private void msg(CommandSender s, String m){
        String a = ChatColor.translateAlternateColorCodes('&', "&a[KOOKBot]&f " + m);
        s.sendMessage(a);
    }
}
